package com.nexters.misik.domain

data class ParsedEntity(
    val parsed: List<ParsedOcr>,
)

data class ParsedOcr(
    val key: String?,
    val value: String?,
)
