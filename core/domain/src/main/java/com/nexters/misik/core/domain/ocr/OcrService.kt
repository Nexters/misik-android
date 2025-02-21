package com.nexters.misik.core.domain.ocr

interface OcrService {
    suspend fun extractText(imagePath: String): String
}
