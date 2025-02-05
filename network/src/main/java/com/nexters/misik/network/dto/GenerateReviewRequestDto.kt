package com.nexters.misik.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenerateReviewRequestDto(
    @SerialName("ocrText")
    val ocrText: String?,
    @SerialName("hashTag")
    val hashTag: List<String>?,
    @SerialName("reviewStyle")
    val reviewStyle: String?,
) {
    companion object {
        fun createMock(): GenerateReviewRequestDto {
            return GenerateReviewRequestDto(
                ocrText = "청담커피•앤•토스트 전화번호: 02-554-2458•주소: 서울특별시 강남구 • 테헤란로 313•지하 1층• 2024-07-29 NO: 2-267 •품명 • 단가 수량 카야토스트 음료세트 3,000 admin 금액 6.5 100% • 리얼 토마토 생과일주스 (3500) 소계 1품목 1건 6,500",
                hashTag = listOf("특별한 메뉴가 있어요"),
                reviewStyle = "CUTE",
            )
        }
    }
}


