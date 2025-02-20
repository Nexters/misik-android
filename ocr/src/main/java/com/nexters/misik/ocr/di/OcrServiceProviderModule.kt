package com.nexters.misik.ocr.di

import com.nexters.misik.ocr.OcrServiceImpl
import com.nexters.misik.ocr.service.CloudOcrRecognizer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OcrServiceProviderModule {

    @Provides
    @Singleton
    fun provideOcrServiceImpl(
        cloudOcrService: CloudOcrRecognizer,
    ): OcrServiceImpl {
        return OcrServiceImpl(cloudOcrService)
    }

    @Provides
    @Singleton
    fun provideCloudOcrService(): CloudOcrRecognizer {
        return CloudOcrRecognizer()
    }
}
