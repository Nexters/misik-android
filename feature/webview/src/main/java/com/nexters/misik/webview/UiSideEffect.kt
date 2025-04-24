package com.nexters.misik.webview

sealed interface UiSideEffect {
    data class SendJs(val function: String, val value: String) : UiSideEffect
}
