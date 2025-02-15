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
}
