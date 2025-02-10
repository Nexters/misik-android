package com.nexters.misik.webview

sealed class WebViewState {
    data object PageLoading : WebViewState()
    data object PageLoaded : WebViewState()
    data class GenerateReview(val id: Long) : WebViewState()
    data class CompleteReview(val review: String) : WebViewState()
    data class ResponseJS(val response: String) : WebViewState()
    data class Error(val message: String) : WebViewState()
}
