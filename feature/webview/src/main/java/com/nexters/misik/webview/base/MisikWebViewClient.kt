package com.nexters.misik.webview.base

import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient

class MisikWebViewClient(
    private val onWebError: (String) -> Unit,
) : WebViewClient() {
    private var lastFinishedUrl: String? = null

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        if (view?.url != url || url == lastFinishedUrl) return
        lastFinishedUrl = url
    }

    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest?,
        error: WebResourceError?,
    ) {
        super.onReceivedError(view, request, error)
        onWebError("Error loading page: ${error?.description}")
    }
}
