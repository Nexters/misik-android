package com.nexters.misik.ocr.service

import com.nexters.misik.ocr.model.OcrResult

interface OcrRecognizer {
    suspend fun recognizeText(imagePath: String): OcrResult
}
