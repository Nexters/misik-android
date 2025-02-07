package com.nexters.misik.webview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nexters.misik.domain.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
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
            is WebViewIntent.OpenCamera -> {
                Timber.d("WebViewIntent: OpenCamera")
                // 카메라 실행 로직 (UI 이벤트 발생 가능)
            }

            is WebViewIntent.OpenGallery -> {
                Timber.d("WebViewIntent: OpenGallery")
                // 갤러리 실행 로직 (UI 이벤트 발생 가능)
            }

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
        }
    }

    private fun copyToClipboard(review: String) {
        // TODO
    }

    fun onEvent(event: WebViewEvent) {
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
            reviewRepository.getOcrParsedResponse(ocrText)
                .onSuccess { data ->
                    if (data != null) {
                        _state.update {
                            it.copy(ocrParsedItems = data)
                        }
                        Timber.d("parsingOcr_Success", data.toString())
                    }
                }
                .onFailure { exception ->
                    Timber.d("parsingOcr_Failure", exception.message)
                }
        }
        // API 요청

        // API 응답

        // WebView로 전달

    }

    private fun generateReview(intent: WebViewIntent.CreateReview) {
        viewModelScope.launch {
            reviewRepository.generateReview(
                ocrText = intent.ocrText,
                hashTags = intent.hashTags,
                reviewStyle = intent.reviewStyle,
            )
                .onSuccess { data ->
                    /*if (data != null) {
                        _state.update {
                            it.copy(reviewId = data)
                        }
                        Timber.d("generateReview_Success", data.toString())
                    }*/
                }
                .onFailure { exception ->
                    Timber.d("generateReview_Failure", exception.message)
                }
        }
    }

    fun getReview() {
        viewModelScope.launch {
            reviewRepository.getReview(
                id = 674907886775732982,
            )
                .onSuccess { data ->
                    /*if (data != null) {
                        _state.update {
                            it.copy(review = data)
                        }
                        Timber.d("getReview_Success", " ${data.isSuccess} ${data.review} ${data.id}")
                    }*/
                }
                .onFailure { exception ->
                    Timber.d("getReview_Failure", exception.message)
                }
        }
    }
}
