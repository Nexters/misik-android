package com.nexters.misik.preview.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PreviewActivity : ComponentActivity() {

    private val viewModel: PreviewViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val imageUri = intent.getStringExtra("imageUri")
        imageUri?.let {
            viewModel.handleIntent(PreviewIntent.LoadImage(it))

            setContent {
                PreviewScreen(viewModel, imageUri)
            }
        }
    }

    companion object {
        const val PREVIEW_REQUEST_CODE = 100
    }
}
