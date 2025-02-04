package com.nexters.misik.domain.ocr

interface OcrService {
    suspend fun extractText(imagePath: String): String
}