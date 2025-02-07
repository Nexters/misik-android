package com.nexters.misik.preview.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Scale
import java.io.File

@Composable
fun PreviewScreen(
    viewModel: PreviewViewModel,
    imagePath: String,
) {
    val state by viewModel.state.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
    ) {
        PreviewImage(imagePath)
        CloseButton(
            onClick = {
                // TODO 닫기 버튼 클릭 시
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp),
        )

        when (state) {
            is PreviewState.Success -> {
                Box(
                    modifier = Modifier
                        .background(Color.Black.copy(alpha = 0.8f))
                        .padding(16.dp),
                ) {
                    Text(
                        text = viewModel.extractedText.value ?: "",
                        color = Color.White,
                    )
                }
            }

            is PreviewState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(48.dp)
                        .align(Alignment.Center),
                )
            }

            is PreviewState.Idle -> {
                ProgressingText(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .size(24.dp)
                        .padding(bottom = 32.dp),
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

@Composable
fun PreviewImage(imagePath: String) {
    val context = LocalContext.current
    val imageRequest = remember {
        ImageRequest.Builder(context)
            .data(File(imagePath))
            .crossfade(true)
            .scale(Scale.FIT)
            .build()
    }

    Image(
        painter = rememberAsyncImagePainter(imageRequest),
        contentDescription = "Receipt Preview",
        modifier = Modifier
            .fillMaxSize(),
    )
}

@Composable
fun CloseButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // 닫기 버튼
    IconButton(
        onClick = { onClick },
        modifier = modifier,
    ) {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Close",
            tint = Color.White,
        )
    }
}

@Composable
fun ProgressingText(
    modifier: Modifier = Modifier,
) {
    Text(
        text = "영수증을 인식 중입니다.",
        color = Color.White,
        style = TextStyle(fontSize = 18.sp),
        modifier = modifier,
    )
}
