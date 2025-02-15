package com.nexters.misik.network.service

import com.nexters.misik.network.dto.request.GenerateReviewRequestDto
import com.nexters.misik.network.dto.request.OcrParseRequestDto
import com.nexters.misik.network.dto.response.GetReviewResponseDto
import com.nexters.misik.network.dto.response.GetVersionUpdateDto
import com.nexters.misik.network.dto.response.OcrParsedResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
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
    ): OcrParsedResponseDto

    // 앱 버전 확인 후 url 반환 API
    @GET("/webview/home")
    suspend fun getUpdateStatus(
        @Header("app-version") appVersion: String,
        @Header("app-platform") appPlatform: String
    ): Response<GetVersionUpdateDto>

}
