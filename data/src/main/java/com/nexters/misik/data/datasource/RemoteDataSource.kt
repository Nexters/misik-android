package com.nexters.misik.data.datasource

import com.nexters.misik.data.mapper.ReviewMapper.toModel
import com.nexters.misik.data.model.Review
import com.nexters.misik.network.dto.request.GenerateReviewRequestDto
import com.nexters.misik.network.dto.request.OcrParseRequestDto
import com.nexters.misik.network.service.ReviewService
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val reviewService: ReviewService,
) {
    suspend fun generateReview(request: GenerateReviewRequestDto): Long =
        reviewService.generateReview(request)

    suspend fun getReview(id: Long): Review =
        reviewService.getReview(id).toModel()

    suspend fun getOcrParsedResponse(text: String): String {
        try {
            val response = reviewService.getOcrParsedResponse(OcrParseRequestDto(text))

            if (!response.isSuccessful) {
                throw HttpException(response)
            }
            return response.body()?.string()
                ?: throw IllegalStateException("Response body is null")
        } catch (e: IOException) {
            throw IOException("Network error occurred", e)
        } catch (e: Exception) {
            throw RuntimeException("Unexpected error occurred", e)
        }
    }
}
