package com.nexters.misik.preview.ui

sealed class PreviewIntent {
    data class LoadImage(val imagePath: String) : PreviewIntent()
}