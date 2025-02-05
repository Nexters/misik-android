package com.nexters.misik.data.mapper

import com.nexters.misik.domain.ReviewEntity
import com.nexters.misik.network.dto.GetReviewResponseDto

object ReviewMapper {
    fun GetReviewResponseDto.toReviewEntity(): ReviewEntity {
        return ReviewEntity(
            id = id,
            isSuccess = isSuccess,
            review = review,
        )
    }
}
