package com.nexters.misik.ocr

import com.nexters.misik.domain.ocr.OcrService
import com.nexters.misik.ocr.service.CloudOcrService
import com.nexters.misik.ocr.service.OnDeviceOcrService
import timber.log.Timber
import javax.inject.Inject

class OcrServiceImpl @Inject constructor(
    private val onDeviceOcrService: OnDeviceOcrService,
    private val cloudOcrService: CloudOcrService,
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