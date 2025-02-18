package com.nexters.misik.data.model

data class OcrParsedResponse(
    val parsed: List<OcrParsedItem>,
)

data class OcrParsedItem(
    val key: String,
    val value: String,
)
