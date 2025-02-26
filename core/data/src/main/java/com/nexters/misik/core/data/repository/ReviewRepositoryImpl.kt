package com.nexters.misik.core.data.repository

import com.nexters.misik.core.data.datasource.RemoteDataSource
import com.nexters.misik.core.data.mapper.ReviewMapper.toDomain
import com.nexters.misik.core.domain.ParsedEntity
import com.nexters.misik.core.domain.ReviewEntity
import com.nexters.misik.core.domain.UpdateUrl
import com.nexters.misik.core.network.dto.request.GenerateReviewRequestDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ReviewRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
) : com.nexters.misik.core.domain.ReviewRepository {

    override suspend fun generateReview(
        ocrText: String,
        hashTags: List<String>,
        reviewStyle: String,
    ): Result<Long> = runCatching {
        withContext(Dispatchers.IO) {
            val requestDto = GenerateReviewRequestDto(
                ocrText = ocrText,
                hashTag = hashTags,
                reviewStyle = reviewStyle,
            )
            remoteDataSource.generateReview(requestDto)
        }
    }

    override suspend fun getReview(id: Long): Result<ReviewEntity?> =
        runCatching {
            remoteDataSource.getReview(id).toDomain()
        }

    override suspend fun getOcrParsedResponse(text: String): Result<ParsedEntity?> =
        runCatching {
            withContext(Dispatchers.IO) {
                remoteDataSource.getOcrParsedResponse(text).toDomain()
            }
        }

    override suspend fun getVersionUpdateStatus(
        appVersion: String,
        appPlatform: String,
    ): Result<UpdateUrl?> = runCatching {
        remoteDataSource.getVersionUpdateStatus(appVersion, appPlatform).toDomain()
    }
}
