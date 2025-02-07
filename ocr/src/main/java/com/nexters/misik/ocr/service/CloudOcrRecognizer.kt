package com.nexters.misik.ocr.service

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import com.nexters.misik.ocr.BuildConfig
import com.nexters.misik.ocr.model.OcrResult
import com.nexters.misik.ocr.util.BitmapUtils.preprocessImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

class CloudOcrRecognizer : OcrRecognizer {

    override suspend fun recognizeText(imagePath: String): OcrResult {
        return withContext(Dispatchers.IO) {
            try {
                Timber.d("Processing image: $imagePath")

                // Bitmap 로드 및 전처리
                val base64Image = encodeToBase64(imagePath)
                Timber.d("Base64 encoding completed.")

                // 요청 JSON 생성
                val requestJson = createCloudVisionRequest(base64Image)
                Timber.d("Generated OCR request: $requestJson")

                // Google Vision API 요청 전송
                val responseJson = sendOcrRequest(requestJson)
                Timber.d("Response JSON: $responseJson")

                // OCR 결과 추출
                val recognizedText = responseJson["responses"]
                    .asJsonArray[0].asJsonObject["fullTextAnnotation"]
                    ?.asJsonObject?.get("text")?.asString ?: "No Text Found"

                Timber.i("Recognized Text: $recognizedText")
                OcrResult(text = recognizedText, blocks = listOf(recognizedText))
            } catch (e: Exception) {
                Timber.e(e, "OCR processing failed")
                OcrResult(text = "Error: ${e.localizedMessage}", blocks = emptyList())
            }
        }
    }

    /**
     * **Base64 인코딩 함수 (이미지 전처리 적용)**
     */
    private fun encodeToBase64(imagePath: String): String {
        val file = File(imagePath)

        if (!file.exists()) {
            Timber.e("File not found: $imagePath")
            throw IllegalArgumentException("File does not exist at path: $imagePath")
        }

        // Bitmap 로드 및 전처리 (흑백 + 리사이징 + 대비 조정)
        val bitmap = BitmapFactory.decodeFile(file.absolutePath)
        val processedBitmap = preprocessImage(bitmap)

        // Bitmap을 Base64로 변환
        val outputStream = ByteArrayOutputStream()
        processedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        val imageBytes: ByteArray = outputStream.toByteArray()

        return Base64.encodeToString(imageBytes, Base64.NO_WRAP)
    }

    /**
     * **Google Vision API 요청 JSON 생성**
     */
    private fun createCloudVisionRequest(base64Image: String): String {
        val requestObject = JsonObject().apply {
            add(
                "image",
                JsonObject().apply {
                    addProperty("content", base64Image)
                },
            )
            add(
                "features",
                JsonArray().apply {
                    add(
                        JsonObject().apply {
                            addProperty("type", "TEXT_DETECTION")
                            addProperty("maxResults", 10) // 결과 개수 지정 (선택 사항)
                        },
                    )
                },
            )
            add(
                "imageContext",
                JsonObject().apply {
                    add(
                        "languageHints",
                        JsonArray().apply {
                            add(JsonPrimitive("ko")) // 한글 OCR
                            add(JsonPrimitive("en")) // 영어 OCR
                        },
                    )
                },
            )
        }

        val requestBody = JsonObject().apply {
            add(
                "requests",
                JsonArray().apply {
                    add(requestObject)
                },
            )
        }

        return requestBody.toString().also { Timber.d("Request JSON: $it") }
    }

    /**
     * **Google Vision API 요청 전송**
     */
    private fun sendOcrRequest(requestJson: String): JsonObject {
        val url =
            URL("https://vision.googleapis.com/v1/images:annotate?key=${BuildConfig.CLOUD_VISION_API_KEY}")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.setRequestProperty("Content-Type", "application/json")
        connection.doOutput = true

        try {
            Timber.d("Sending OCR request to $url")
            connection.outputStream.write(requestJson.toByteArray(Charsets.UTF_8))
            connection.outputStream.flush()

            Timber.d("Response Code: ${connection.responseCode}")
            val response = connection.inputStream.bufferedReader().use { it.readText() }
            Timber.d("Raw Response: $response")

            return com.google.gson.JsonParser.parseString(response).asJsonObject
        } catch (e: Exception) {
            Timber.e(e, "OCR request failed")
            throw e
        } finally {
            connection.disconnect()
        }
    }
}
