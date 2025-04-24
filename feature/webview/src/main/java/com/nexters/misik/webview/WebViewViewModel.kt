package com.nexters.misik.webview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexters.misik.core.domain.ReviewRepository
import com.nexters.misik.feature.webview.BuildConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class WebViewViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository,
) : ViewModel() {
    private val _state = MutableStateFlow<WebViewState>(WebViewState.PageLoading)
    val state: StateFlow<WebViewState> = _state

    private val _sideEffect = MutableSharedFlow<UiSideEffect>()
    val sideEffect: SharedFlow<UiSideEffect> = _sideEffect

    private val _keyboardHeight = MutableStateFlow(0)
    val keyboardHeight: StateFlow<Int> = _keyboardHeight.asStateFlow()

    fun updateKeyboardHeight(height: Int) {
        _keyboardHeight.value = height
    }

    fun sendIntent(intent: WebViewIntent) {
        when (intent) {
            is WebViewIntent.OpenCamera, is WebViewIntent.OpenGallery -> {}
            is WebViewIntent.Share -> Timber.d("Share: ${intent.shareText}")
            is WebViewIntent.Copy -> _state.value = WebViewState.CopyToClipboard(intent.review)
            is WebViewIntent.HandleOcrResult -> handleOcr(intent.ocrText)
            is WebViewIntent.CreateReview -> generateReview(intent)
            is WebViewIntent.WebViewLoadFailed ->
                _state.value =
                    WebViewState.Error(intent.errorMessage)
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

    private fun handleOcr(ocrText: String?) {
        viewModelScope.launch {
            ocrText?.let {
                _state.value = WebViewState.ParseOcrText(it)
                _sideEffect.emit(UiSideEffect.SendJs("receiveScanResult", it))
            } ?: run {
                _sideEffect.emit(UiSideEffect.SendJs("receiveScanResult", ""))
            }
        }
    }

    private fun generateReview(intent: WebViewIntent.CreateReview) {
        viewModelScope.launch {
            reviewRepository.generateReview(
                ocrText = intent.ocrText,
                hashTags = intent.hashTags,
                reviewStyle = intent.reviewStyle,
            ).onSuccess { id ->
                if (id != null) {
                    _state.value = WebViewState.GenerateReview(id)
                    getReview(id)
                }
            }.onFailure {
                Timber.e(it)
            }
        }
    }

    private fun getReview(id: Long) {
        viewModelScope.launch {
            reviewRepository.getReview(id)
                .onSuccess { data ->
                    val review = data?.review ?: return@launch
                    _state.value = WebViewState.CompleteReview(review)
                    _sideEffect.emit(UiSideEffect.SendJs("receiveGeneratedReview", review))
                }
                .onFailure {
                    _sideEffect.emit(UiSideEffect.SendJs("receiveGeneratedReview", ""))
                }
        }
    }
}
