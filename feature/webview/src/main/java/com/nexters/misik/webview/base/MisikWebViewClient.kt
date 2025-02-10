package com.nexters.misik.webview.base

import android.util.Log
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.nexters.misik.webview.WebViewEvent

class MisikWebViewClient(
    private val onEvent: (WebViewEvent) -> Unit,
) : WebViewClient() {

    private var lastFinishedUrl: String? = null

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        Log.d("Web", "onPageFinished 호출됨: $url, WebView ID: ${view?.hashCode()}")
        if (view?.url != url) {
            Log.d("Web", "iframe 로딩 무시: $url")
            return
        }

        // 중복 호출 방지
        if (url == lastFinishedUrl) {
            Log.d("Web", "🔄 onPageFinished 중복 호출 방지: $url")
            return
        }
        Log.d("Web", "onPageFinished 정상 호출: $url\n ${view?.url}\n $lastFinishedUrl")
        lastFinishedUrl = url
        onEvent(WebViewEvent.PageLoaded)
    }

    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest?,
        error: WebResourceError?,
    ) {
        super.onReceivedError(view, request, error)
        Log.d("Web", "onReceivedError ${request?.url}")
        onEvent(WebViewEvent.JsError("Error loading page: ${error?.description}"))
    }

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        Log.d("Web", "shouldOverrideUrlLoading ${request?.url} $request")
        return super.shouldOverrideUrlLoading(view, request)
    }
}
