package com.nexters.misik.data.mapper

import com.nexters.misik.data.model.OcrParsedItem
import com.nexters.misik.data.model.OcrParsedResponse
import com.nexters.misik.data.model.Review
import com.nexters.misik.data.model.VersionUpdateUrl
import com.nexters.misik.domain.ParsedEntity
import com.nexters.misik.domain.ParsedOcr
import com.nexters.misik.domain.ReviewEntity
import com.nexters.misik.domain.UpdateUrl
import com.nexters.misik.network.dto.response.GetReviewResponseDto
import com.nexters.misik.network.dto.response.GetVersionUpdateDto
import com.nexters.misik.network.dto.response.OcrParsedResponseDto
import retrofit2.Response

object ReviewMapper {
    fun GetReviewResponseDto.toModel(): Review {
        return Review(
            id = id,
            isSuccess = isSuccess,
            review = review,
        )
    }

    fun OcrParsedResponseDto.toModel(): OcrParsedResponse {
        return OcrParsedResponse(
            parsed = parsed.map {
                OcrParsedItem(
                    key = it.key,
                    value = it.value,
                )
            },
        )
    }

    fun Response<GetVersionUpdateDto>.toModel(): VersionUpdateUrl {
        return VersionUpdateUrl(
            statusCode = this.code(),
            url = this.body()?.url ?: "",
        )
    }

    fun Review.toDomain(): ReviewEntity {
        return ReviewEntity(
            id = this.id,
            review = this.review,
            isSuccess = this.isSuccess,
        )
    }

    fun OcrParsedResponse.toDomain(): ParsedEntity {
        return ParsedEntity(
            parsed = parsed.map {
                ParsedOcr(
                    key = it.key,
                    value = it.value,
                )
            },
        )
    }

    fun VersionUpdateUrl.toDomain(): UpdateUrl {
        return UpdateUrl(
            statusCode = this.statusCode,
            url = this.url,
        )
    }
}
