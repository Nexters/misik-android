package com.nexters.misik.webview

import com.nexters.misik.domain.ReviewEntity

data class WebViewState(
    val isLoading: Boolean = false,
    val content: String? = null,
    val error: String? = null,
    val reviewId: Long = 0,
    val review: ReviewEntity? = null,
)
