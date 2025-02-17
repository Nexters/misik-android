package com.nexters.misik.preview.ui

sealed class PreviewState {
    data object Idle : PreviewState()
    data object Loading : PreviewState()
    data class Success(val result: String) : PreviewState()
    data class Error(val message: String) : PreviewState()
}
