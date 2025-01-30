package com.nexters.misik

import android.app.Application
import androidx.multidex.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MisikApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        initTimber()
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
