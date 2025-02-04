package com.nexters.misik.preview.di

import com.nexters.misik.preview.PreviewService
import com.nexters.misik.preview.util.ImageHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
object PreviewModule {

    @Provides
    @ActivityScoped
    fun provideImageHandler(): ImageHandler {
        return ImageHandler()
    }

    @Provides
    @ActivityScoped
    fun providePreviewService(imageHandler: ImageHandler): PreviewService {
        return PreviewService(imageHandler)
    }
}