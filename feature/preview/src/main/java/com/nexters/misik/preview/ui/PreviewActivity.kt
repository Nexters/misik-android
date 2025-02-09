package com.nexters.misik.preview.ui

import android.content.Intent
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
        imageUri?.let { it ->
            viewModel.handleIntent(PreviewIntent.LoadImage(it))

            setContent {
                PreviewScreen(
                    viewModel,
                    imageUri,
                    onClose = { result -> finishWebViewWithResult(result) },
                )
            }
        }
    }

    private fun finishWebViewWithResult(imageUri: String?) {
        val resultIntent = Intent().apply {
            putExtra("imageUri", imageUri)
        }
        setResult(RESULT_OK, resultIntent)
        finish()
    }
}
