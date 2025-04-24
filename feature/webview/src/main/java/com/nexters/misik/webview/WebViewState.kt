package com.nexters.misik.webview

sealed interface WebViewState {
    data object PageLoading : WebViewState
    data object PageLoaded : WebViewState
    data class ParseOcrText(val ocrText: String) : WebViewState
    data class CheckIsUpdateRequired(val url: String) : WebViewState
    data class GenerateReview(val id: Long) : WebViewState
    data class CompleteReview(val review: String) : WebViewState
    data class CopyToClipboard(val review: String) : WebViewState
    data class Error(val message: String) : WebViewState
}
