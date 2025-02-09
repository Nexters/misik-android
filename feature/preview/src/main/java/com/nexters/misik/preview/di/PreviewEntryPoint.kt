package com.nexters.misik.preview.di

import com.nexters.misik.preview.PreviewService
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@EntryPoint
@InstallIn(ActivityComponent::class)
interface PreviewEntryPoint {
    fun previewService(): PreviewService
}
