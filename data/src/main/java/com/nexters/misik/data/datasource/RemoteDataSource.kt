package com.nexters.misik.data.datasource

import com.nexters.misik.data.mapper.OcrParsedMapper.toExternalModel
import com.nexters.misik.data.mapper.ReviewMapper.toModel
import com.nexters.misik.data.model.OcrParsedResponse
import com.nexters.misik.data.model.Review
import com.nexters.misik.network.dto.request.GenerateReviewRequestDto
import com.nexters.misik.network.dto.request.OcrParseRequestDto
import com.nexters.misik.network.service.ReviewService
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val reviewService: ReviewService,
) {
    suspend fun generateReview(request: GenerateReviewRequestDto): Long =
        reviewService.generateReview(request)

    suspend fun getReview(id: Long): Review =
        reviewService.getReview(id).toModel()

    suspend fun getOcrParsedResponse(text: String): OcrParsedResponse =
        reviewService.getOcrParsedResponse(OcrParseRequestDto(text)).toExternalModel()
}
