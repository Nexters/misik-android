package com.nexters.misik.ocr.service

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.google.android.gms.tasks.Task
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.HttpsCallableResult
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.JsonPrimitive
import com.nexters.misik.ocr.model.OcrResult
import com.nexters.misik.ocr.util.BitmapUtils.scaleBitmapDown
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File

class CloudOcrService @Inject constructor(
    private val firebaseFunctions: FirebaseFunctions,
) : OcrService {

    override suspend fun recognizeText(imagePath: String): OcrResult {
        try {
            val base64Image = encodeToBase64(imagePath)
            val requestJson = createCloudVisionRequest(base64Image)

            val response: HttpsCallableResult = withContext(Dispatchers.IO) {
                firebaseFunctions
                    .getHttpsCallable("annotateImage")
                    .call(requestJson)
                    .await()
            }

//            annotateImage(requestJson)
//                .addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                        Timber.i("Response success JSON: $task")
//
//                    } else {
//                        Timber.i("Response failed JSON: $task")
//                    }
//                }

            return ocrResult(response) //OcrResult(text = "Success", blocks = emptyList())
        } catch (e: Exception) {
            Timber.e(e)
            return OcrResult(text = "Error: ${e.localizedMessage}", blocks = emptyList())
        }
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

    private fun annotateImage(requestJson: String): Task<JsonElement> {
        return firebaseFunctions
            .getHttpsCallable("annotateImage")
            .call(requestJson)
            .continueWith { task ->
                // This continuation runs on either success or failure, but if the task
                // has failed then result will throw an Exception which will be
                // propagated down.
                val result = task.result?.getData() as Map<*, *>
                val recognizedText = result["textAnnotations"]?.toString() ?: "No Text Found"
                Timber.i("Recognized Text: $recognizedText")
                JsonParser.parseString(Gson().toJson(result))
            }
    }

    private fun createCloudVisionRequest(base64Image: String): String {
        val request = JsonObject()

        // 이미지 데이터를 추가
        val image = JsonObject()
        image.add("content", JsonPrimitive(base64Image))
        request.add("image", image)

        // OCR 요청 유형 (TEXT_DETECTION 또는 DOCUMENT_TEXT_DETECTION)
        val feature = JsonObject()
        feature.add("type", JsonPrimitive("TEXT_DETECTION"))

        val features = JsonArray()
        features.add(feature)
        request.add("features", features)

        // (선택 사항) 언어 힌트 추가
        val imageContext = JsonObject()
        val languageHints = JsonArray()
        languageHints.add("ko")
        languageHints.add("en") // 영어 OCR
        imageContext.add("languageHints", languageHints)
        request.add("imageContext", imageContext)

        return request.toString()
    }

    private fun encodeToBase64(imagePath: String): String {
        val file = File(imagePath)
        val bitmap = BitmapFactory.decodeFile(file.absolutePath)
        val resizedBitmap = scaleBitmapDown(bitmap, 640)

        val byteArrayOutputStream = ByteArrayOutputStream()
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val imageBytes: ByteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(imageBytes, Base64.NO_WRAP)
    }
}
