package com.nexters.misik

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MisikApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        initTimber()
        preventDarkMode()
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun preventDarkMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}
