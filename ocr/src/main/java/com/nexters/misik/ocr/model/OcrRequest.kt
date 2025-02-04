package com.nexters.misik.ocr.model

data class OcrRequest(
    val imageContent: String,
    val languageHints: List<String> = listOf("en"),
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "image" to mapOf("content" to imageContent),
            "features" to listOf(mapOf("type" to "TEXT_DETECTION")),
            "imageContext" to mapOf("languageHints" to languageHints),
        )
    }
}
