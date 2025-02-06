package com.nexters.misik.ocr.service

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.HttpsCallableResult
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import com.nexters.misik.ocr.BuildConfig
import com.nexters.misik.ocr.model.OcrResult
import com.nexters.misik.ocr.util.BitmapUtils.scaleBitmapDown
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

class CloudOcrRecognizer @Inject constructor(
    private val firebaseFunctions: FirebaseFunctions,
) : OcrRecognizer {

    override suspend fun recognizeText(imagePath: String): OcrResult {
        return withContext(Dispatchers.IO) {
            try {
                val base64Image = encodeToBase64(imagePath)
                val requestJson = createCloudVisionRequest(base64Image)
                val responseJson = sendOcrRequest(requestJson)

                val recognizedText = responseJson["responses"]
                    .asJsonArray[0].asJsonObject["fullTextAnnotation"]
                    ?.asJsonObject?.get("text")?.asString ?: "No Text Found"

                Timber.i("Recognized Text: $recognizedText")
                OcrResult(text = recognizedText, blocks = listOf(recognizedText))
            } catch (e: Exception) {
                Timber.e(e)
                OcrResult(text = "Error: ${e.localizedMessage}", blocks = emptyList())
            }
        }
    }

    private fun sendOcrRequest(requestJson: String): JsonObject {
        val url =
            URL("https://vision.googleapis.com/v1/images:annotate?key=${BuildConfig.CLOUD_VISION_API_KEY}")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.setRequestProperty("Content-Type", "application/json")
        connection.doOutput = true
        connection.outputStream.write(requestJson.toByteArray(Charsets.UTF_8))

        val response = connection.inputStream.bufferedReader().use { it.readText() }
        connection.disconnect()

        return com.google.gson.JsonParser.parseString(response).asJsonObject
    }

    private fun ocrResult(response: HttpsCallableResult): OcrResult {
        val resultJson = response.getData() as Map<*, *>
        Timber.i("Response JSON: $resultJson")
        val recognizedText = resultJson["textAnnotations"]?.toString() ?: "No Text Found"
        Timber.i("Recognized Text: $recognizedText")
//        val jsonResponse = JsonParser.parseString(response.getData().toString()).asJsonObject
//        val recognizedText = jsonResponse["fullTextAnnotation"].asJsonObject["text"].asString
        return OcrResult(text = recognizedText, blocks = listOf(recognizedText))
    }

    private fun createCloudVisionRequest(base64Image: String): String {
        val request = JsonObject()
        val image = JsonObject().apply { add("content", JsonPrimitive(base64Image)) }
        val feature = JsonObject().apply { add("type", JsonPrimitive("TEXT_DETECTION")) }

        request.add("image", image)
        request.add("features", JsonArray().apply { add(feature) })
        request.add(
            "imageContext",
            JsonObject().apply {
                add(
                    "languageHints",
                    JsonArray().apply {
                        add("ko") // 한글
                        add("en") // 영어
                    },
                )
            },
        )

        return request.toString()
    }

    private fun encodeToBase64(imagePath: String): String {
        val file = File(imagePath)
        val bitmap = BitmapFactory.decodeFile(file.absolutePath)
        val resizedBitmap = scaleBitmapDown(bitmap, 1024)

        val byteArrayOutputStream = ByteArrayOutputStream()
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val imageBytes: ByteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(imageBytes, Base64.NO_WRAP)
    }
}
