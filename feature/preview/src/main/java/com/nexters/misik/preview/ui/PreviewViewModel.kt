package com.nexters.misik.preview.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreviewViewModel @Inject constructor(
//    private val ocrService: OcrService,
) : ViewModel() {

    private val _state = MutableStateFlow<PreviewState>(PreviewState.Idle)
    val state: StateFlow<PreviewState> = _state

    private val _imageUri = MutableStateFlow<String?>(null)
    val imageUri: StateFlow<String?> = _imageUri

    private val _extractedText = MutableStateFlow<String?>(null)
    val extractedText: StateFlow<String?> = _extractedText

    fun handleIntent(intent: PreviewIntent) {
        when (intent) {
            is PreviewIntent.LoadImage -> processImage(intent.imagePath)
        }
    }

    private fun processImage(imagePath: String) {
        _state.value = PreviewState.Loading

        viewModelScope.launch {
            try {
                val extractedText = ""//ocrService.extractText(imagePath)
                _extractedText.value = extractedText
                _state.value = PreviewState.Success(previewImagePath = imagePath)
            } catch (e: Exception) {
                _state.value = PreviewState.Error(message = "OCR error: ${e.message}")
            }
        }
    }
}