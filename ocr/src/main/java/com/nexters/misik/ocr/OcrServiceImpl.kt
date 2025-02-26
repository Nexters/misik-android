package com.nexters.misik.ocr

import com.nexters.misik.core.domain.ocr.OcrService
import com.nexters.misik.ocr.exception.CloudOcrException
import com.nexters.misik.ocr.service.CloudOcrRecognizer
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OcrServiceImpl @Inject constructor(
    private val cloudOcrService: CloudOcrRecognizer,
) : com.nexters.misik.core.domain.ocr.OcrService {

    override suspend fun extractText(imagePath: String): String {
        Timber.plant(Timber.DebugTree())
        return try {
            cloudOcrService.recognizeText(imagePath).text
        } catch (e: CloudOcrException) {
            throw e
        } catch (e: Exception) {
            throw CloudOcrException(
                "Unexpected error",
                e,
            )
        }
    }
}
