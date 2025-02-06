package com.nexters.misik.ocr

import com.nexters.misik.domain.ocr.OcrService
import com.nexters.misik.ocr.service.CloudOcrRecognizer
import com.nexters.misik.ocr.service.OnDeviceOcrRecognizer
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OcrServiceImpl @Inject constructor(
    private val onDeviceOcrService: OnDeviceOcrRecognizer,
    private val cloudOcrService: CloudOcrRecognizer,
) : OcrService {
    private val useCloud = false

    override suspend fun extractText(imagePath: String): String {
        Timber.plant(Timber.DebugTree())
        return if (useCloud) {
            cloudOcrService.recognizeText(imagePath).text
        } else {
            onDeviceOcrService.recognizeText(imagePath).text
        }
    }
}
