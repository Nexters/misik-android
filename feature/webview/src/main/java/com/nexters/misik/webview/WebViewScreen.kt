package com.nexters.misik.webview

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
    val responseJs by viewModel.responseJs.collectAsStateWithLifecycle()
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
        responseJs?.let {
            webView.evaluateJavascript(it, null)
            Timber.d("WebViewScreen_toJS_Success", it)
        } ?: Timber.d("WebViewScreen_toJS_Failure", "js is null")

    }



    Box(modifier = modifier.fillMaxSize()) {
        when (uiState) {
//            WebViewState.PageLoading -> {
//                Timber.d("WebViewScreen_UiState", "Loading")
//                LoadingAnimation(modifier = Modifier.align(Alignment.Center))
//            }


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
