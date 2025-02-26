package com.nexters.misik.core.data.datasource

import com.nexters.misik.core.data.mapper.ReviewMapper.toModel
import com.nexters.misik.core.data.model.OcrParsedResponse
import com.nexters.misik.core.data.model.Review
import com.nexters.misik.core.data.model.VersionUpdateUrl
import com.nexters.misik.core.network.dto.request.GenerateReviewRequestDto
import com.nexters.misik.core.network.dto.request.OcrParseRequestDto
import com.nexters.misik.core.network.service.ReviewService
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val reviewService: ReviewService,
) {
    suspend fun generateReview(request: GenerateReviewRequestDto): Long =
        reviewService.generateReview(request)

    suspend fun getReview(id: Long): Review =
        reviewService.getReview(id).toModel()

    suspend fun getOcrParsedResponse(text: String): OcrParsedResponse =
        reviewService.getOcrParsedResponse(OcrParseRequestDto(text)).toModel()

    suspend fun getVersionUpdateStatus(
        appVersion: String,
        appPlatform: String,
    ): VersionUpdateUrl =
        reviewService.getUpdateStatus(appVersion = appVersion, appPlatform = appPlatform).toModel()
}
