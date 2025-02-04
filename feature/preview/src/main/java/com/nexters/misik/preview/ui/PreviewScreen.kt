package com.nexters.misik.preview.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@Composable
fun PreviewScreen(viewModel: PreviewViewModel) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        when (state) {
            is PreviewState.Idle -> {
                Text(
                    text = "이미지를 선택해주세요.",
                    modifier = Modifier.padding(16.dp),
                )
            }

            is PreviewState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.fillMaxSize())
            }

            is PreviewState.Success -> {
                val imagePath = (state as PreviewState.Success).previewImagePath
                Image(
                    painter = rememberAsyncImagePainter(imagePath),
                    contentDescription = "Preview Image",
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "OCR 결과를 기다리는 중...",
                    modifier = Modifier.padding(16.dp),
                )
            }

            is PreviewState.Error -> {
                val errorMessage = (state as PreviewState.Error).message
                Text(
                    text = errorMessage,
                    modifier = Modifier.padding(16.dp),
                    color = Color.Red,
                )
            }
        }
    }
}