package com.nexters.misik.webview.bridge.dto.request

import com.nexters.misik.webview.WebViewIntent

data class CopyRequest(
    val review: String,
)

fun CopyRequest.toIntent(): WebViewIntent.Copy {
    return WebViewIntent.Copy(
        review = this.review,
    )
}