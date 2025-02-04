package com.nexters.misik.ocr.di

import com.nexters.misik.domain.ocr.OcrService
import com.nexters.misik.ocr.OcrServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class OcrModule {
    @Binds
    @Singleton
    abstract fun bindOcrService(impl: OcrServiceImpl): OcrService
}