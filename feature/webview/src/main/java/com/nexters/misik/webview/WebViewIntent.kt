package com.nexters.misik.webview

sealed interface WebViewIntent {
    data object OpenCamera : WebViewIntent
    data object OpenGallery : WebViewIntent
    data class Share(val content: String) : WebViewIntent
    data class CreateReview(
        val ocrText: String,
        val hashTags: List<String>,
        val reviewStyle: String,
    ) : WebViewIntent

    data class Copy(val review: String) : WebViewIntent
}