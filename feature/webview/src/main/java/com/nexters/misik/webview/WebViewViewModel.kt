package com.nexters.misik.webview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexters.misik.domain.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class WebViewViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository,
) : ViewModel() {
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
                _state.value =
                    _state.value.copy(isLoading = false, content = event.response, error = null)
            }

            is WebViewEvent.JsError -> {
                _state.value = _state.value.copy(isLoading = false, error = event.error)
            }
        }
    }

    fun generateReview() {
        viewModelScope.launch {
            reviewRepository.generateReview(
                ocrText = "청담커피•앤•토스트 전화번호: 02-554-2458•주소: 서울특별시 강남구 • 테헤란로 313•지하 1층• 2024-07-29 NO: 2-267 •품명 • 단가 수량 카야토스트 음료세트 3,000 admin 금액 6.5 100% • 리얼 토마토 생과일주스 (3500) 소계 1품목 1건 6,500",
                hashTags = listOf("특별한 메뉴가 있어요"),
                reviewStyle = "CUTE",
            )
                .onSuccess { data ->
                    if (data != null) {
                        _state.update {
                            it.copy(reviewId = data)
                        }
                        Timber.d("generateReview_Success", data.toString())
                    }
                }
                .onFailure { exception ->
                    Timber.d("generateReview_Failure", exception.message)
                }
        }
    }
}
