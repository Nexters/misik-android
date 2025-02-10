package com.nexters.misik.webview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexters.misik.domain.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class WebViewViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository,
) : ViewModel() {
    private val _state = MutableStateFlow<WebViewState>(WebViewState.PageLoading)
    val state: StateFlow<WebViewState> = _state

    private val _responseJs = MutableStateFlow<String?>(null)
    val responseJs: StateFlow<String?> = _responseJs

    fun sendIntent(intent: WebViewIntent) {
        when (intent) {
            is WebViewIntent.Share -> {
                Timber.d("WebViewIntent: Share -> ${intent.content}")
                // 공유 기능 실행
            }

            is WebViewIntent.CreateReview -> {
                Timber.d("WebViewIntent: CreateReview -> ${intent.ocrText}")
                generateReview(intent)
            }

            is WebViewIntent.Copy -> {
                Timber.d("WebViewIntent: Copy -> ${intent.review}")
                copyToClipboard(intent.review)
            }

            is WebViewIntent.HandleOcrResult -> {
                Timber.d("WebViewIntent: HandleOcrResult -> ${intent.ocrText}")
                intent.ocrText?.let { parsingOcr(intent.ocrText) }
            }

            else -> {
                Timber.d("WebViewIntent: else")
            }
        }
    }

    private fun copyToClipboard(review: String) {
        // TODO
    }

    fun onEvent(event: WebViewEvent) {
        Timber.i("onEvent: $event")
        when (event) {
            WebViewEvent.LoadPage -> {
                _state.value = WebViewState.PageLoading
            }

            WebViewEvent.PageLoaded -> {
                _state.value = WebViewState.PageLoaded
            }

            is WebViewEvent.JsResponse -> {
                _state.value = WebViewState.ResponseJS(event.response)
            }

            is WebViewEvent.JsError -> {
                _state.value = WebViewState.Error(event.error)
            }
        }
    }

    private fun parsingOcr(ocrText: String) {
        viewModelScope.launch {
            _state.value = WebViewState.PageLoading
            reviewRepository.getOcrParsedResponse(ocrText)
                .onSuccess { data ->
                    if (data != null) {
                        _responseJs.value = makeResponse("receiveScanResult", data)
                        Timber.d("parsingOcr_Success", data.toString())
                    }
                    _state.value = WebViewState.PageLoaded
                }
                .onFailure { exception ->
                    _state.value = WebViewState.PageLoaded
                    Timber.d("parsingOcr_Failure", exception.message)
                }
        }
    }

    private fun makeResponse(functionName: String, response: String): String {
        val escapedResponse = JSONObject.quote(response)
        return ("javascript:window.response.$functionName('$escapedResponse')").apply {
            Timber.i(
                this,
            )
        }
    }

    private fun generateReview(intent: WebViewIntent.CreateReview) {
        viewModelScope.launch {
            reviewRepository.generateReview(
                ocrText = intent.ocrText,
                hashTags = intent.hashTags,
                reviewStyle = intent.reviewStyle,
            )
                .onSuccess { data ->
                    if (data != null) {
                        _state.value = WebViewState.GenerateReview(data)
                        getReview(data)
                        Timber.d("generateReview_Success", data.toString())
                    }
                }
                .onFailure { exception ->
                    Timber.d("generateReview_Failure", exception.message)
                }
        }
    }

    private fun getReview(id: Long) {
        viewModelScope.launch {
            reviewRepository.getReview(id)
                .onSuccess { data ->
                    val reviewText = data?.review ?: return@launch
                    _state.value = WebViewState.CompleteReview(reviewText)
                    // TODO : receiveGenerateReview 웹으로 보내기
                    Timber.d("getReview_Success", " ${data.isSuccess} $reviewText ${data.id}")
                }
                .onFailure { exception ->
                    Timber.d("getReview_Failure", exception.message)
                }
        }
    }
}
