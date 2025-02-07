package com.nexters.misik.data.mapper

import com.nexters.misik.data.model.OcrParsedItem
import com.nexters.misik.data.model.OcrParsedResponse
import com.nexters.misik.domain.model.OcrParsedItems
import com.nexters.misik.network.dto.response.OcrParsedItemDto
import com.nexters.misik.network.dto.response.OcrParsedResponseDto

object OcrParsedMapper {

    fun OcrParsedResponseDto.toExternalModel(): OcrParsedResponse {
        return OcrParsedResponse(
            parsed = this.parsed.map { it.toExternalModel() },
        )
    }

    private fun OcrParsedItemDto.toExternalModel(): OcrParsedItem {
        return OcrParsedItem(
            key = this.key,
            value = this.value,
        )
    }

    fun OcrParsedResponse.toDomain(): OcrParsedItems {
        return OcrParsedItems(
            parsed = this.parsed.map { it.toDomain() },
        )
    }

    private fun OcrParsedItem.toDomain(): com.nexters.misik.domain.model.OcrParsedItem {
        return com.nexters.misik.domain.model.OcrParsedItem(
            key = this.key,
            value = this.value,
        )
    }
}