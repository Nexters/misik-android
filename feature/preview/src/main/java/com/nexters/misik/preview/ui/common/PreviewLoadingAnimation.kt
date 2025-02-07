package com.nexters.misik.webview.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.nexters.misik.preview.R

@Composable
fun PreviewLoadingAnimation(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val imageLoader = ImageLoader.Builder(context)
        .components {
            add(ImageDecoderDecoder.Factory())
        }.build()

    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context)
            .data(R.drawable.loading)
            .build(),
        imageLoader = imageLoader,
    )

    Image(
        painter = painter,
        contentDescription = "Loading",
        modifier = modifier.size(100.dp),
    )
}
