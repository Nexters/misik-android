package com.nexters.misik.domain

interface ReviewRepository {
    // 리뷰 생성
    suspend fun generateReview(ocrText: String, hashTags: List<String>, reviewStyle: String): Result<Long?>

    // 리뷰 조회
    suspend fun getReview(id: Long): Result<ReviewEntity?>

    suspend fun getOcrParsedResponse(text: String): Result<String?>
}
