package com.nexters.misik.ui

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import com.nexters.misik.core.ui.LocalPreviewService
import com.nexters.misik.preview.PreviewService
import com.nexters.misik.preview.di.PreviewEntryPoint
import com.nexters.misik.ui.nav.MainNavGraph
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var previewService: PreviewService

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        previewService = EntryPointAccessors.fromActivity(this, PreviewEntryPoint::class.java)
            .previewService()
        previewService.init(this)

        setContent {
            CompositionLocalProvider(LocalPreviewService provides previewService) {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .safeDrawingPadding(),
                ) { innerPadding ->
                    MainNavGraph(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}
