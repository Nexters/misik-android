package com.nexters.misik.webview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.multidex.BuildConfig
import com.nexters.misik.preview.PreviewService
import com.nexters.misik.preview.di.PreviewEntryPoint
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var previewService: PreviewService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        previewService = EntryPointAccessors.fromActivity(this, PreviewEntryPoint::class.java)
            .previewService()

        previewService.init(this)

        setContent {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                WebViewScreen(
                    previewService,
                    modifier = Modifier.padding(innerPadding),
                )
            }
        }
    }
}
