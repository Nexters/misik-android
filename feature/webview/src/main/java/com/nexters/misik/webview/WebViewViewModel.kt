package com.nexters.misik.webview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexters.misik.core.domain.ReviewRepository
import com.nexters.misik.feature.webview.BuildConfig
import com.nexters.misik.webview.util.JsResponseUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class WebViewViewModel @Inject constructor(
    private val reviewRepository: com.nexters.misik.core.domain.ReviewRepository,
) : ViewModel() {
    private val _state = MutableStateFlow<WebViewState>(WebViewState.PageLoading)
    val state: StateFlow<WebViewState> = _state

    private val _responseJs = MutableStateFlow<String?>(null)
    val responseJs: StateFlow<String?> = _responseJs

    private val _keyboardHeight = MutableStateFlow(0) // 키보드 높이 상태
    val keyboardHeight: StateFlow<Int> = _keyboardHeight.asStateFlow()

    fun updateKeyboardHeight(height: Int) {
        _keyboardHeight.value = height
    }

    fun initializeJs(){
        _responseJs.value = null
    }

    fun sendIntent(intent: WebViewIntent) {
        when (intent) {
            is WebViewIntent.Share -> {
                Timber.d("WebViewIntent: Share")
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
                responseOcrParsed(intent.ocrText)
            }

            else -> {
                Timber.d("WebViewIntent: else")
            }
        }
    }

    private fun copyToClipboard(review: String) {
        _state.value = WebViewState.CopyToClipBoard(review)
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

    fun getVersionUpdateStatus() {
        viewModelScope.launch {
            _state.value = WebViewState.PageLoading
            reviewRepository.getVersionUpdateStatus(
                appVersion = BuildConfig.VERSION_NAME,
                appPlatform = "ANDROID",
            )
                .onSuccess { data ->
                    if (data != null) {
                        val url = data.url
                        when (data.statusCode) {
                            200 -> {
                                if (url != null) {
                                    _state.value = WebViewState.CheckIsUpdateRequired(url)
                                }
                            }

                            426 -> {
                                if (url != null) {
                                    _state.value = WebViewState.CheckIsUpdateRequired(url)
                                }
                            }
                        }

                        Timber.d("getVersionUpdateStatus_Success", url)
                    }
                }
                .onFailure { exception ->
                    _state.value = WebViewState.PageLoading
                    Timber.d("getVersionUpdateStatus_Failure", exception.message)
                }
        }
    }

    private fun responseOcrParsed(ocrText: String?) {
        viewModelScope.launch {
            ocrText?.let {
                _state.value = WebViewState.ParseOcrText(ocrText)
                _responseJs.value = JsResponseUtil.makeResponse("receiveScanResult", ocrText)
            } ?: run {
                _responseJs.value = JsResponseUtil.makeFailureResponse("receiveScanResult")
            }
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
                    _responseJs.value =
                        JsResponseUtil.makeReviewResponse("receiveGeneratedReview", reviewText)

                    Timber.d("getReview_Success", " ${data.isSuccess} $reviewText ${data.id}")
                }
                .onFailure { exception ->
                    _responseJs.value =
                        JsResponseUtil.makeFailureResponse("receiveGeneratedReview")
                    Timber.d("getReview_Failure", exception.message)
                }
        }
    }
}
