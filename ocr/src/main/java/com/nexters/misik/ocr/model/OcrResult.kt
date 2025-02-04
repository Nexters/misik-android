package com.nexters.misik.ocr.model

data class OcrResult(
    val text: String,
    val blocks: List<String>,
)