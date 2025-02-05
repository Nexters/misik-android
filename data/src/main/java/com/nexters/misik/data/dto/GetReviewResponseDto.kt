package com.nexters.misik.data.dto

import com.nexters.misik.domain.ReviewEntity
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

) {
    fun toDomain() = ReviewEntity(
        id = id,
        isSuccess = isSuccess,
        review = review,
    )
}
