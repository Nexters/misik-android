package com.nexters.misik.preview.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Scale
import com.nexters.misik.preview.R
import com.nexters.misik.preview.ui.common.PreviewScanningAnimation
import kotlinx.coroutines.delay
import java.io.File

@Composable
fun PreviewScreen(
    viewModel: PreviewViewModel,
    imagePath: String,
    onClose: (String?) -> Unit,
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
                onClose(null)
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp),
        )
        ScanningOverlay()

        when (state) {
            is PreviewState.Success -> {
                SuccessOverlay(
                    text = viewModel.extractedText.value ?: "",
                    onClose = onClose,
                )
            }

            is PreviewState.Loading -> {
                ScanningOverlay()
            }

            is PreviewState.Idle -> {}

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
fun SuccessOverlay(
    text: String,
    onClose: (String?) -> Unit,
) {
//    if (BuildConfig.DEBUG) {
//        Box(
//            modifier = Modifier
//                .background(Color.Black.copy(alpha = 0.5f))
//                .padding(16.dp),
//        ) {
//            Text(
//                text = text,
//                color = Color.White,
//            )
//        }
//    }
    // 2초 후 onClose 호출
    LaunchedEffect(Unit) {
        delay(2000)
        onClose(text)
    }
}

@Composable
fun ScanningOverlay() {
    Box(modifier = Modifier.fillMaxSize()) {
        PreviewScanningAnimation(
            modifier = Modifier.align(Alignment.Center),
        )
        ScanningTextOverlay(modifier = Modifier.align(Alignment.BottomCenter))
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
    val context = LocalContext.current
    val imageRequest = remember {
        ImageRequest.Builder(context)
            .data(R.drawable.ic_back_button)
            .crossfade(true)
            .build()
    }

    IconButton(
        onClick = onClick,
        modifier = modifier.padding(8.dp, 8.dp),
    ) {
        Image(
            painter = rememberAsyncImagePainter(imageRequest),
            contentDescription = "Close",
            modifier = Modifier.size(32.dp),
        )
    }
}

@Preview
@Composable
fun ScanningTextOverlay(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(
                brush = Brush.verticalGradient(
                    0f to Color.Black,
                    0.7f to Color.Black.copy(alpha = 0f),
                    1f to Color.Transparent,
                    startY = Float.POSITIVE_INFINITY,
                    endY = 0f,
                ),
            )
            .padding(top = 40.dp, bottom = 50.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "영수증을 인식 중입니다.",
            color = Color.White,
            fontSize = 22.sp,
        )
    }
}
