package com.nexters.misik.webview

import android.webkit.JavascriptInterface
import com.nexters.misik.domain.WebBridgeContract
import org.json.JSONObject
import javax.inject.Inject

class WebInterface @Inject constructor(
    private val bridge: WebBridgeContract,
) {

    @JavascriptInterface
    fun openCamera() {
        bridge.openCamera()
    }

    @JavascriptInterface
    fun openGallery() {
        bridge.openGallery()
    }

    @JavascriptInterface
    fun share(content: String) {
        bridge.share(content)
    }

    @JavascriptInterface
    fun createReview(json: String) {
        val jsonObject = JSONObject(json)
        val ocrText = jsonObject.getString("ocrText")
        val hashTags = jsonObject.getJSONArray("hashTag").let { array ->
            List(array.length()) { array.getString(it) }
        }
        val reviewStyle = jsonObject.getString("reviewStyle")
        bridge.createReview(ocrText, hashTags, reviewStyle)
    }

    @JavascriptInterface
    fun copy(json: String) {
        val jsonObject = JSONObject(json)
        val review = jsonObject.getString("review")
        bridge.copy(review)
    }
}
