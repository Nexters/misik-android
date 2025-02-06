package com.nexters.misik.webview.base

import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.nexters.misik.webview.WebViewEvent

class MisikWebViewClient(
    private val onEvent: (WebViewEvent) -> Unit,
) : WebViewClient() {

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        onEvent(WebViewEvent.PageLoaded)
    }

    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest?,
        error: WebResourceError?,
    ) {
        super.onReceivedError(view, request, error)
        onEvent(WebViewEvent.JsError("Error loading page: ${error?.description}"))
    }
}
