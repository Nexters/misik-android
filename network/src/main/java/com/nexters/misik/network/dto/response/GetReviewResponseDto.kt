package com.nexters.misik.network.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetReviewResponseDto(
    @SerialName("isSuccess")
    val isSuccess: Boolean?,
    @SerialName("id")
    val id: String?,
    @SerialName("review")
    val review: String?,

)
