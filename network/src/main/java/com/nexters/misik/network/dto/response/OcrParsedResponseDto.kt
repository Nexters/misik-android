package com.nexters.misik.network.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OcrParsedResponseDto(
    @SerialName("parsed")
    val parsed: List<OcrParsedItemDto>,
) {
    companion object {
        fun createMock(): OcrParsedResponseDto {
            return OcrParsedResponseDto(
                parsed = listOf(
                    OcrParsedItemDto("품명", "카야토스트+음료세트"),
                    OcrParsedItemDto("품명", "아메리카노"),
                    OcrParsedItemDto("품명", "샌드위치"),
                ),
            )
        }
    }
}

@Serializable
data class OcrParsedItemDto(
    @SerialName("key")
    val key: String,
    @SerialName("value")
    val value: String,
)
