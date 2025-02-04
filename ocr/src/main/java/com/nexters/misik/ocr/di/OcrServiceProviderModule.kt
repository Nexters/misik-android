package com.nexters.misik.ocr.di

import com.nexters.misik.ocr.OcrServiceImpl
import com.nexters.misik.ocr.service.CloudOcrService
import com.nexters.misik.ocr.service.OnDeviceOcrService
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
    fun provideOnDeviceOcrService(): OnDeviceOcrService {
        return OnDeviceOcrService()
    }

    @Provides
    @Singleton
    fun provideOcrServiceImpl(
        onDeviceOcrService: OnDeviceOcrService,
        cloudOcrService: CloudOcrService,
    ): OcrServiceImpl {
        return OcrServiceImpl(onDeviceOcrService, cloudOcrService)
    }

    /*    @Provides
        @Singleton
        fun provideCloudOcrService(firebaseFunctions: FirebaseFunctions): CloudOcrService {
            return CloudOcrService(firebaseFunctions)
        }

        @Provides
        @Singleton
        fun provideFirebaseFunctions(): FirebaseFunctions {
            return Firebase.functions
        }*/
}