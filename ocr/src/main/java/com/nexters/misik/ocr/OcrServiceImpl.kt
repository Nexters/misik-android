package com.nexters.misik.ocr

import com.nexters.misik.domain.ocr.OcrService
import javax.inject.Inject

class OcrServiceImpl @Inject constructor(
//    private val ocrEngine: OcrEngine
) : OcrService {
    override suspend fun extractText(imagePath: String): String {
        return ""//ocrEngine.processImage(imagePath)
    }
}