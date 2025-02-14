package com.nexters.misik.webview

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.nexters.misik.webview.util.ShareUtil
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

                is WebViewIntent.Share -> {
                    ShareUtil.shareApp(context)
                }

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
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { webView },
            update = { webView ->
                Timber.d("updated :${webView.hashCode()}")
            },
        )
        when (val state = uiState) {
            is WebViewState.CopyToClipBoard -> {
                CopyToClipboard(state.review)
            }

            is WebViewState.PageLoading -> {
                Timber.d("WebViewScreen_UiState", "Loading")
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center), // 오버레이처럼 위에 띄움
                ) {
                    LoadingAnimation(modifier = Modifier.align(Alignment.Center))
                }
            }

            else -> {
            }
        }
    }
}

@Composable
fun CopyToClipboard(review: String) {
    val context = LocalContext.current
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("Review", review)
    clipboard.setPrimaryClip(clip)
}
