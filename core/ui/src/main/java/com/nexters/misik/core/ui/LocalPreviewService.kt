package com.nexters.misik.core.ui

import androidx.compose.runtime.compositionLocalOf

val LocalPreviewService = compositionLocalOf<PreviewServiceContract> {
    error("PreviewService not provided")
}
