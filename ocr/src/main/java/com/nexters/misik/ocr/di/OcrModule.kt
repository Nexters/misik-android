package com.nexters.misik.ocr.di

import com.nexters.misik.core.domain.ocr.OcrService
import com.nexters.misik.ocr.OcrServiceImpl
import com.nexters.misik.ocr.service.CloudOcrRecognizer
import com.nexters.misik.ocr.service.OcrRecognizer
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
    abstract fun bindOcrService(ocrServiceImpl: OcrServiceImpl): com.nexters.misik.core.domain.ocr.OcrService

    @Binds
    abstract fun bindCloudOcrService(service: CloudOcrRecognizer): OcrRecognizer
}
