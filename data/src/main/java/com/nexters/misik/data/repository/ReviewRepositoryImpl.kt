package com.nexters.misik.data.repository

import com.nexters.misik.data.datasource.RemoteDataSource
import com.nexters.misik.data.mapper.ReviewMapper.toDomain
import com.nexters.misik.domain.ReviewEntity
import com.nexters.misik.domain.ReviewRepository
import com.nexters.misik.network.dto.request.GenerateReviewRequestDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class ReviewRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
) : ReviewRepository {

    override suspend fun generateReview(
        ocrText: String,
        hashTags: List<String>,
        reviewStyle: String,
    ): Result<Long> = runCatching {
        val requestDto = GenerateReviewRequestDto(
            ocrText = ocrText,
            hashTag = hashTags,
            reviewStyle = reviewStyle,
        )
        remoteDataSource.generateReview(GenerateReviewRequestDto.createMock())
    }

    override suspend fun getReview(id: Long): Result<ReviewEntity?> = runCatching {
        remoteDataSource.getReview(id).toDomain()
    }

    override suspend fun getOcrParsedResponse(text: String): Result<String> = runCatching {
        withContext(Dispatchers.IO) {
            remoteDataSource.getOcrParsedResponse(text)
                ?: throw IOException("Failed to fetch OCR response")
        }
    }
}
