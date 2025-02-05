package com.nexters.misik.ocr.di

import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.nexters.misik.ocr.OcrServiceImpl
import com.nexters.misik.ocr.service.CloudOcrRecognizer
import com.nexters.misik.ocr.service.OnDeviceOcrRecognizer
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
    fun provideOnDeviceOcrService(): OnDeviceOcrRecognizer {
        return OnDeviceOcrRecognizer()
    }

    @Provides
    @Singleton
    fun provideOcrServiceImpl(
        onDeviceOcrService: OnDeviceOcrRecognizer,
        cloudOcrService: CloudOcrRecognizer,
    ): OcrServiceImpl {
        return OcrServiceImpl(onDeviceOcrService, cloudOcrService)
    }

    @Provides
    @Singleton
    fun provideCloudOcrService(firebaseFunctions: FirebaseFunctions): CloudOcrRecognizer {
        return CloudOcrRecognizer(firebaseFunctions)
    }

    @Provides
    @Singleton
    fun provideFirebaseFunctions(): FirebaseFunctions {
        return Firebase.functions
    }
}