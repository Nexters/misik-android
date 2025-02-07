package com.nexters.misik.network.service

import com.nexters.misik.network.dto.request.GenerateReviewRequestDto
import com.nexters.misik.network.dto.request.OcrParseRequestDto
import com.nexters.misik.network.dto.response.GetReviewResponseDto
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

    // OCR Parsing API
    @POST("reviews/ocr-parsing")
    suspend fun getOcrParsedResponse(
        @Body request: OcrParseRequestDto,
    ): String
}
