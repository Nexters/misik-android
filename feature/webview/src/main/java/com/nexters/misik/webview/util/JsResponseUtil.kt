package com.nexters.misik.webview.util

import org.json.JSONObject
import timber.log.Timber

object JsResponseUtil {
    private const val RESPONSE = "response"
    private const val RESPONSE_FAILURE_MSG = "error"

    fun makeResponse(functionName: String, response: String): String {
        val escapedResponse = JSONObject.quote(response)
        return "javascript:window.$RESPONSE.$functionName($escapedResponse)".also {
            Timber.i("Generated JS: $it")
        }
    }

    fun makeFailureResponse(functionName: String): String {
        return makeResponse(functionName, RESPONSE_FAILURE_MSG)
    }

    fun makeReviewResponse(functionName: String, reviewText: String?): String {
        val jsonResponse = JSONObject().apply {
            put("result", reviewText ?: RESPONSE_FAILURE_MSG)
        }
        return makeResponse(functionName, jsonResponse.toString())
    }

    fun makeKeyboardHeightResponse(functionName: String, keyboardHeight: String?): String {
        val jsonResponse = JSONObject().apply {
            put("height", keyboardHeight ?: RESPONSE_FAILURE_MSG)
        }
        return makeResponse(functionName, jsonResponse.toString())
    }
}
