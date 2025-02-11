package com.nexters.misik.webview

import com.nexters.misik.domain.ParsedEntity
sealed class WebViewState {
    data object PageLoading : WebViewState()
    data object PageLoaded : WebViewState()
    data class ParseOcrText(val ocrText: ParsedEntity) : WebViewState()
    data class GenerateReview(val id: Long) : WebViewState()
    data class CompleteReview(val review: String) : WebViewState()
    data class CopyToClipBoard(val review: String) : WebViewState()
    data class ResponseJS(val response: String) : WebViewState()
    data class Error(val message: String) : WebViewState()
}
