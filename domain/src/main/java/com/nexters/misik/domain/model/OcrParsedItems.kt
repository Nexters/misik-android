package com.nexters.misik.domain.model

data class OcrParsedItems(
    val parsed: List<OcrParsedItem>,
)

data class OcrParsedItem(
    val key: String,
    val value: String,
)