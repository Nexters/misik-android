package com.nexters.misik.webview

/*data class WebViewState(
    val isLoading: Boolean = false,
    val content: String? = null,
    val error: String? = null,
    val reviewId: Long = 0,
    val review: ReviewEntity? = null,
)*/

sealed class WebViewState {
    data object PageLoading : WebViewState()
    data object PageLoaded : WebViewState()

    //    data class ResponseJS(val response: String) : WebViewState()
    data class Error(val message: String) : WebViewState()
}