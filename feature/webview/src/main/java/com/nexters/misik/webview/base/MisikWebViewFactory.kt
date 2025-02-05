package com.nexters.misik.webview.base

import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewGroup
import android.webkit.WebView
import com.nexters.misik.webview.WebViewEvent
import com.nexters.misik.webview.bridge.WebInterface

object MisikWebViewFactory {
    @SuppressLint("SetJavaScriptEnabled")
    fun create(
        context: Context,
        webInterface: WebInterface,
        onEvent: (WebViewEvent) -> Unit,
    ): WebView {
        return WebView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            )
            settings.javaScriptEnabled = true

            addJavascriptInterface(webInterface, "AndroidBridge")

            webViewClient = MisikWebViewClient(onEvent)
            webChromeClient = MisikWebChromeClient()

            loadUrl("https://misik-web.vercel.app/")
        }
    }
}