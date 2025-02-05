package com.nexters.misik.data.mapper

import com.nexters.misik.data.model.Review
import com.nexters.misik.domain.ReviewEntity
import com.nexters.misik.network.dto.GetReviewResponseDto

object ReviewMapper {
    fun GetReviewResponseDto.toModel(): Review {
        return Review(
            id = id,
            isSuccess = isSuccess,
            review = review,
        )
    }

    fun Review.toDomain(): ReviewEntity {
        return ReviewEntity(
            id = this.id,
            review = this.review,
            isSuccess = this.isSuccess,
        )
    }
}
