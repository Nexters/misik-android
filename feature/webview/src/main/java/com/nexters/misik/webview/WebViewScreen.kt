package com.nexters.misik.webview

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nexters.misik.preview.PreviewService
import com.nexters.misik.webview.base.MisikWebViewFactory
import com.nexters.misik.webview.bridge.WebInterface
import timber.log.Timber

@SuppressLint("SetJavaScriptEnabled", "JavascriptInterface")
@Composable
fun WebViewScreen(
    previewService: PreviewService,
    modifier: Modifier = Modifier,
    viewModel: WebViewViewModel = hiltViewModel(),
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    val webInterface = remember {
        WebInterface { intent ->
            when (intent) {
                is WebViewIntent.OpenCamera -> previewService.openCamera(
                    {
                        viewModel.sendIntent(WebViewIntent.HandleOcrResult(it))
                    },
                )

                is WebViewIntent.OpenGallery -> previewService.openGallery(
                    {
                        viewModel.sendIntent(WebViewIntent.HandleOcrResult(it))
                    },
                )
                else -> viewModel.sendIntent(intent)
            }
        }
    }

    // 페이지 로딩 이벤트 발생 (WebView 로드 시작 시)
    LaunchedEffect(Unit) {
        viewModel.onEvent(WebViewEvent.LoadPage)
    }

    Box(modifier = modifier.fillMaxSize()) {
        if (uiState == WebViewState.PageLoading) {
            Timber.d("WebViewScreen_UiState", "Loading")
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            Timber.d("WebViewScreen_UiState", "Loaded")
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->
                    MisikWebViewFactory.create(
                        context = context,
                        webInterface = webInterface,
                        onEvent = { event -> viewModel.onEvent(event) },
                    )
                },
            )
        }
    }
}