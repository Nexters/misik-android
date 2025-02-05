package com.nexters.misik.data.di

import com.nexters.misik.network.service.ReviewService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {
    @Singleton
    @Provides
    internal fun provideReviewService(
        retrofit: Retrofit,
    ): ReviewService {
        return retrofit.create(ReviewService::class.java)
    }
}
