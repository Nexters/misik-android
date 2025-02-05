package com.nexters.misik.webview.bridge

import android.webkit.JavascriptInterface
import com.google.gson.Gson
import com.nexters.misik.webview.WebViewIntent
import com.nexters.misik.webview.bridge.dto.request.CopyRequest
import com.nexters.misik.webview.bridge.dto.request.CreateReviewRequest
import com.nexters.misik.webview.bridge.dto.request.toIntent

class WebInterface(
    private val eventCallback: (WebViewIntent) -> Unit,
) {
    private val gson = Gson()

    @JavascriptInterface
    fun openCamera() {
        eventCallback(WebViewIntent.OpenCamera)
    }

    @JavascriptInterface
    fun openGallery() {
        eventCallback(WebViewIntent.OpenGallery)
    }

    @JavascriptInterface
    fun share(content: String) {
        eventCallback(WebViewIntent.Share(content))
    }

    @JavascriptInterface
    fun createReview(json: String) {
        val request = gson.fromJson(json, CreateReviewRequest::class.java)
        eventCallback(request.toIntent())
    }

    @JavascriptInterface
    fun copy(json: String) {
        val request = gson.fromJson(json, CopyRequest::class.java)
        eventCallback(request.toIntent())
    }
}
