package com.nexters.misik.preview.ui

sealed class PreviewState {
    object Idle : PreviewState()
    object Loading : PreviewState()
    data class Success(val previewImagePath: String) : PreviewState()
    data class Error(val message: String) : PreviewState()
}
