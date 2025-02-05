package com.nexters.misik.data.service

import com.nexters.misik.data.dto.GenerateReviewRequestDto
import com.nexters.misik.data.dto.GetReviewResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ReviewService {
    // 리뷰 생성 API
    @POST("reviews")
    suspend fun generateReview(
        @Body request: GenerateReviewRequestDto,
    ): Long

    // 리뷰 조회 API
    @GET("reviews/{id}")
    suspend fun getReview(
        @Path("id") id: Long,
    ): GetReviewResponseDto
}
