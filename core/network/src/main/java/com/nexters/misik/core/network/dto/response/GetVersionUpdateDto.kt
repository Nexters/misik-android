package com.nexters.misik.core.network.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetVersionUpdateDto(
    @SerialName("url")
    val url: String?,
)
