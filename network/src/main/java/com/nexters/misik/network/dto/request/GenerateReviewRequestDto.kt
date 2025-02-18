package com.nexters.misik.network.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenerateReviewRequestDto(
    @SerialName("ocrText")
    val ocrText: String?,
    @SerialName("hashTag")
    val hashTag: List<String>?,
    @SerialName("reviewStyle")
    val reviewStyle: String?,
)
