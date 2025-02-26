package com.nexters.misik.core.data.di

import com.nexters.misik.core.data.repository.ReviewRepositoryImpl
import com.nexters.misik.core.domain.ReviewRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindReviewRepository(reviewRepositoryImpl: ReviewRepositoryImpl): com.nexters.misik.core.domain.ReviewRepository
}
