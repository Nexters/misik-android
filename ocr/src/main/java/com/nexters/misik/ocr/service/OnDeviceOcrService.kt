package com.nexters.misik.ocr.service

import android.graphics.BitmapFactory
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import com.nexters.misik.ocr.model.OcrResult
import com.nexters.misik.ocr.util.BitmapUtils.scaleBitmapDown
import jakarta.inject.Inject
import timber.log.Timber
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class OnDeviceOcrService @Inject constructor() : OcrService {
    private val recognizer =
        TextRecognition.getClient(KoreanTextRecognizerOptions.Builder().build())

    override suspend fun recognizeText(imagePath: String): OcrResult {
        val bitmap = BitmapFactory.decodeFile(imagePath)
        val resizedBitmap = scaleBitmapDown(bitmap, 640)
        val inputImage = InputImage.fromBitmap(resizedBitmap, 0)

        return suspendCoroutine { continuation ->
            recognizer.process(inputImage)
                .addOnSuccessListener { visionText ->
                    Timber.i("Recognized Text: ${visionText.text}")
                    continuation.resume(
                        OcrResult(
                            text = visionText.text,
                            blocks = visionText.textBlocks.map { it.text },
                        ),
                    )
                }
                .addOnFailureListener { e ->
                    continuation.resumeWithException(e)
                }
        }
    }
}
