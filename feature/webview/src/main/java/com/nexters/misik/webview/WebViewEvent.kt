package com.nexters.misik.webview

sealed class WebViewEvent {
    data object LoadPage : WebViewEvent() // 페이지 로딩 이벤트
    data object PageLoaded : WebViewEvent() // 페이지 로딩 완료 이벤트
    data class JsResponse(val response: String) : WebViewEvent() // JavaScript 응답 이벤트
    data class JsError(val error: String) : WebViewEvent()
}
