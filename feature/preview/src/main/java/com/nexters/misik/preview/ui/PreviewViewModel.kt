package com.nexters.misik.preview.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexters.misik.core.domain.ReviewRepository
import com.nexters.misik.core.domain.ocr.OcrService
import com.nexters.misik.preview.util.GsonUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PreviewViewModel @Inject constructor(
    private val ocrService: com.nexters.misik.core.domain.ocr.OcrService,
    private val reviewRepository: com.nexters.misik.core.domain.ReviewRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<PreviewState>(PreviewState.Idle)
    val state: StateFlow<PreviewState> = _state

    private val _imageUri = MutableStateFlow<String?>(null)
    val imageUri: StateFlow<String?> = _imageUri

    private val _extractedText = MutableStateFlow<String?>(null)
    val extractedText: StateFlow<String?> = _extractedText

    fun handleIntent(intent: PreviewIntent) {
        when (intent) {
            is PreviewIntent.LoadImage -> {
                _imageUri.value = intent.imagePath
                processImage(intent.imagePath)
            }
        }
    }

    private fun processImage(imagePath: String) {
        _state.value = PreviewState.Loading

        viewModelScope.launch {
            try {
                val extractedText = extractText(imagePath)
                _extractedText.value = extractedText

                val ocrParsedResult = parsingOcr(extractedText)
                _state.value = PreviewState.Success(ocrParsedResult)
            } catch (e: Exception) {
                _state.value = PreviewState.Error(message = "OCR error: ${e.message}")
            }
        }
    }

    private suspend fun extractText(imagePath: String): String {
        return ocrService.extractText(imagePath)
    }

    private suspend fun parsingOcr(ocrText: String): String {
        return runCatching {
            val data = reviewRepository.getOcrParsedResponse(ocrText).getOrThrow()
            val jsonResponse = GsonUtil.toJson(data)
            Timber.d("parsingOcr_Success: $jsonResponse")
            jsonResponse
        }.getOrElse {
            Timber.e("parsingOcr_Failure: ${it.message}")
            throw Exception("parsingOcr failed", it)
        }
    }
}
