package com.nexters.misik.webview.base

import android.os.Message
import android.webkit.WebChromeClient
import android.webkit.WebView
import timber.log.Timber

class MisikWebChromeClient : WebChromeClient() {
    override fun onCreateWindow(
        view: WebView?,
        isDialog: Boolean,
        isUserGesture: Boolean,
        resultMsg: Message?,
    ): Boolean {
        Timber.i("onCreateWindow $resultMsg")
        return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg)
    }
}
