package com.nexters.misik.webview

import android.app.Application
import android.content.Context
import com.nexters.misik.domain.WebBridgeContract
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object WebBridgeModule {

    @Provides
    fun provideWebBridgeImpl(context: Context): WebBridgeContract {
        return WebBridgeImpl(context)
    }

    @Provides
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

}

