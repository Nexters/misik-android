package com.nexters.misik.webview

data class WebViewState(
    val isLoading: Boolean = false,
    val content: String? = null,
    val error: String? = null,
)
