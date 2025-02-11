package com.nexters.misik.webview.base

import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.nexters.misik.webview.WebViewEvent
import timber.log.Timber

class MisikWebViewClient(
    private val onEvent: (WebViewEvent) -> Unit,
) : WebViewClient() {

    private var lastFinishedUrl: String? = null

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        if (view?.url != url) {
            Timber.d("iframe 로딩 무시: $url")
            return
        }

        if (url == lastFinishedUrl) {
            Timber.d("🔄 onPageFinished 중복 호출 방지: $url")
            return
        }
        Timber.d("onPageFinished: $url\n ${view?.url}\n $lastFinishedUrl")
        lastFinishedUrl = url
        onEvent(WebViewEvent.PageLoaded)
    }

    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest?,
        error: WebResourceError?,
    ) {
        super.onReceivedError(view, request, error)
        Timber.d("onReceivedError ${request?.url}")
        onEvent(WebViewEvent.JsError("Error loading page: ${error?.description}"))
    }

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        Timber.d("shouldOverrideUrlLoading ${request?.url} $request")
        return super.shouldOverrideUrlLoading(view, request)
    }
}
