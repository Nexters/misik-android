package com.nexters.misik.core.data.model

data class OcrParsedResponse(
    val parsed: List<OcrParsedItem>,
)

data class OcrParsedItem(
    val key: String,
    val value: String,
)
