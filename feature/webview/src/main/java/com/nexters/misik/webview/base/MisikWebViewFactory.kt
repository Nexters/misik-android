package com.nexters.misik.webview.base

import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import com.nexters.misik.webview.bridge.WebInterface

object MisikWebViewFactory {
    @SuppressLint("SetJavaScriptEnabled")
    fun create(
        context: Context,
        webInterface: WebInterface,
        onWebError: (String) -> Unit,

    ): WebView {
        return WebView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            )
            settings.javaScriptEnabled = true
            settings.cacheMode = WebSettings.LOAD_NO_CACHE

            addJavascriptInterface(webInterface, "AndroidBridge")
            webViewClient = MisikWebViewClient(
                onWebError = onWebError,
            )
            webChromeClient = MisikWebChromeClient()
        }
    }
}
