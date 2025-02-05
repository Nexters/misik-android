package com.nexters.misik.data.datasource

import com.nexters.misik.network.dto.GenerateReviewRequestDto
import com.nexters.misik.network.dto.GetReviewResponseDto
import com.nexters.misik.network.service.ReviewService
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val reviewService: ReviewService,
) {
    suspend fun generateReview(request: GenerateReviewRequestDto): Long =
        reviewService.generateReview(request)

    suspend fun getReview(id: Long): GetReviewResponseDto =
        reviewService.getReview(id)
}
