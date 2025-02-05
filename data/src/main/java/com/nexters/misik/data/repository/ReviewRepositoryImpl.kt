package com.nexters.misik.data.repository

import com.nexters.misik.data.datasource.RemoteDataSource
import com.nexters.misik.data.mapper.ReviewMapper.toReviewEntity
import com.nexters.misik.domain.ReviewEntity
import com.nexters.misik.domain.ReviewRepository
import com.nexters.misik.network.dto.GenerateReviewRequestDto
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
        remoteDataSource.generateReview(requestDto)
    }

    override suspend fun getReview(id: Long): Result<ReviewEntity?> = runCatching {
        val responseDto = remoteDataSource.getReview(id)
        responseDto.toReviewEntity()
    }
}
