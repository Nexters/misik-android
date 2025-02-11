package com.nexters.misik.webview.bridge

import android.webkit.JavascriptInterface
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.nexters.misik.webview.WebViewIntent
import com.nexters.misik.webview.bridge.dto.request.CopyRequest
import com.nexters.misik.webview.bridge.dto.request.CreateReviewRequest
import com.nexters.misik.webview.bridge.dto.request.toIntent
import timber.log.Timber

class WebInterface(
    private val eventCallback: (WebViewIntent) -> Unit,
) {
    private val gson = Gson()

    @JavascriptInterface
    fun openCamera() {
        Timber.i("openCamera")
        eventCallback(WebViewIntent.OpenCamera)
    }

    @JavascriptInterface
    fun openGallery() {
        Timber.i("openGallery")
        eventCallback(WebViewIntent.OpenGallery)
    }

    @JavascriptInterface
    fun share(content: String) {
        Timber.i("share: $content")
        eventCallback(WebViewIntent.Share(content))
    }

    @JavascriptInterface
    fun createReview(json: String) {
        try {
            val request = gson.fromJson(json, CreateReviewRequest::class.java)

            val intent = WebViewIntent.CreateReview(
                ocrText = request.ocrText,
                hashTags = request.hashTag,
                reviewStyle = request.reviewStyle
            )

            eventCallback(intent)
        } catch (e: JsonSyntaxException) {
        }
    }



    @JavascriptInterface
    fun copy(json: String) {
        Timber.i("copy: $json")
        val request = gson.fromJson(json, CopyRequest::class.java)
        eventCallback(request.toIntent())
    }
}
