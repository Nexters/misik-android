package com.nexters.misik.webview

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class WebViewViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(WebViewState())
    val state: StateFlow<WebViewState> get() = _state

    fun onEvent(event: WebViewEvent) {
        when (event) {
            WebViewEvent.LoadPage -> {
                _state.value = _state.value.copy(isLoading = true, error = null)
            }
            WebViewEvent.PageLoaded -> {
                _state.value = _state.value.copy(isLoading = false, error = null)
            }
            is WebViewEvent.JsResponse -> {
                _state.value = _state.value.copy(isLoading = false, content = event.response, error = null)
            }
            is WebViewEvent.JsError -> {
                _state.value = _state.value.copy(isLoading = false, error = event.error)
            }
        }
    }
}
