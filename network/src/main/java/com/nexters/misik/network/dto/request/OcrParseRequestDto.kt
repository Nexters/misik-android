package com.nexters.misik.network.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OcrParseRequestDto(
    @SerialName("text")
    val text: String,
) 
