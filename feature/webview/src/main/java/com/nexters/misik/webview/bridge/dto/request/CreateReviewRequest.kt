package com.nexters.misik.webview.bridge.dto.request

import com.nexters.misik.webview.WebViewIntent

data class CreateReviewRequest(
    val ocrText: String,
    val hashTag: List<String>,
    val reviewStyle: String,
)

fun CreateReviewRequest.toIntent(): WebViewIntent.CreateReview {
    return WebViewIntent.CreateReview(
        ocrText = this.ocrText,
        hashTags = this.hashTag,
        reviewStyle = this.reviewStyle,
    )
}