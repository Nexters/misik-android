package com.nexters.misik.network

import com.hyeseon.misik.network.BuildConfig
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Logging

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val CONTENT_TYPE = "application/json"

    @Singleton
    @Provides
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        prettyPrint = true
    }

    @Singleton
    @Provides
    fun provideJsonConverterFactory(json: Json): Converter.Factory {
        return json.asConverterFactory(CONTENT_TYPE.toMediaType())
    }

    @Logger
    @Singleton
    @Provides
    fun provideLoggingInterceptor(): Interceptor =
        HttpLoggingInterceptor().setLevel(

            HttpLoggingInterceptor.Level.BODY,

        )

    @Auth
    @Singleton
    @Provides
    fun provideAuthInterceptor(): Interceptor = Interceptor { chain ->
        val originalRequest = chain.request()

        // 일단은 모든 요청 100 으로 고정
        val newRequest = originalRequest.newBuilder()
            .addHeader("device-id", "100")
            .build()

        chain.proceed(newRequest)
    }

    @Logging
    @Singleton
    @Provides
    fun provideOkHttpClient(
        @Logger loggingInterceptor: Interceptor,
        @Auth authInterceptor: Interceptor,
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(authInterceptor)
        .build()

    @Singleton
    @Provides
    fun provideRetrofit(
        @Logging client: OkHttpClient,
        converterFactory: Converter.Factory,
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.NETWORK_BASE_URL)
        .client(client)
        .addConverterFactory(converterFactory)
        .build()
}
