package com.nexters.misik.webview

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nexters.misik.preview.PreviewService
import com.nexters.misik.webview.base.MisikWebViewFactory
import com.nexters.misik.webview.bridge.WebInterface
import com.nexters.misik.webview.common.LoadingAnimation
import timber.log.Timber

@SuppressLint("SetJavaScriptEnabled", "JavascriptInterface")
@Composable
fun WebViewScreen(
    previewService: PreviewService,
    modifier: Modifier = Modifier,
    viewModel: WebViewViewModel = hiltViewModel(),
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val responseJs by viewModel.responseJs.collectAsState()
    val context = LocalContext.current

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

    val webView = remember {
        MisikWebViewFactory.create(
            context = context,
            webInterface = webInterface,
            onEvent = { event -> viewModel.onEvent(event) },
        )
    }

    LaunchedEffect(responseJs) {
        responseJs?.let { jsResponse ->
            Timber.d("WebViewScreen_ResponseJs", jsResponse)
            webView.loadUrl(jsResponse)
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        when (uiState) {
            WebViewState.PageLoading -> {
                Timber.d("WebViewScreen_UiState", "Loading")
                LoadingAnimation(modifier = Modifier.align(Alignment.Center))
            }

            else -> {
                Timber.d("WebViewScreen_UiState", "Loaded")
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = { webView },
                )
            }
        }
    }
}