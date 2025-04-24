package com.nexters.misik.webview

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.view.View
import android.webkit.WebView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowInsetsCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nexters.misik.core.ui.LocalPreviewService
import com.nexters.misik.webview.base.MisikWebViewFactory
import com.nexters.misik.webview.bridge.WebInterface
import com.nexters.misik.webview.common.LoadingAnimation
import com.nexters.misik.webview.util.JsResponseUtil.makeKeyboardHeightResponse
import com.nexters.misik.webview.util.JsResponseUtil.makeResponse
import com.nexters.misik.webview.util.JsResponseUtil.makeReviewResponse
import com.nexters.misik.webview.util.ShareUtil
import timber.log.Timber

// --- WebViewScreen.kt ---
@Composable
fun WebViewScreen(
    modifier: Modifier = Modifier,
    viewModel: WebViewViewModel = hiltViewModel(),
) {
    val previewService = LocalPreviewService.current
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val keyboardHeight by viewModel.keyboardHeight.collectAsState()
    val context = LocalContext.current

    val webInterface = remember {
        WebInterface { intent ->
            when (intent) {
                is WebViewIntent.OpenCamera -> previewService.openCamera {
                    viewModel.sendIntent(WebViewIntent.HandleOcrResult(it))
                }

                is WebViewIntent.OpenGallery -> previewService.openGallery {
                    viewModel.sendIntent(WebViewIntent.HandleOcrResult(it))
                }

                is WebViewIntent.Share -> ShareUtil.shareApp(context, intent.shareText)
                else -> viewModel.sendIntent(intent)
            }
        }
    }

    val initializedUrl by rememberUpdatedState(
        when (val state = uiState) {
            is WebViewState.CheckIsUpdateRequired -> state.url
            else -> ""
        },
    )

    val webView = remember {
        MisikWebViewFactory.create(
            context = context,
            webInterface = webInterface,
            onWebError = { error -> viewModel.sendIntent(WebViewIntent.WebViewLoadFailed(error)) },

        )
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is UiSideEffect.SendJs -> {
                    val js = when (effect.function) {
                        "receiveGeneratedReview" -> makeReviewResponse(
                            effect.function,
                            effect.value,
                        )

                        "receiveKeyboardHeight" -> makeKeyboardHeightResponse(
                            effect.function,
                            effect.value,
                        )

                        else -> makeResponse(effect.function, effect.value)
                    }
                    webView.evaluateJavascript(js, null)
                    Timber.d("WebViewScreen_sendJS: $js")
                    Timber.d("WebViewScreen_toJS_Success: $js")
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.getVersionUpdateStatus()
    }

    LaunchedEffect(initializedUrl) {
        if (initializedUrl.isNotEmpty()) {
            webView.loadUrl(initializedUrl)
            Timber.d("WebViewScreen_LoadingUrl: $initializedUrl")
        }
    }

    KeyboardInsetsListener { imeBottom -> viewModel.updateKeyboardHeight(imeBottom) }
    SendKeyboardHeightToJS(keyboardHeight, webView)

    Box(modifier = modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { webView },
            update = { Timber.d("updated :${it.hashCode()}") },
        )
        when (uiState) {
            is WebViewState.CopyToClipboard -> CopyToClipboard((uiState as WebViewState.CopyToClipboard).review)
            is WebViewState.PageLoading -> LoadingOverlay()
            else -> {}
        }
    }
}

@Composable
fun KeyboardInsetsListener(onKeyboardHeightChanged: (Int) -> Unit) {
    val view = LocalView.current
    DisposableEffect(view) {
        val listener = View.OnApplyWindowInsetsListener { _, insets ->
            val imeBottom = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
            } else {
                WindowInsetsCompat.toWindowInsetsCompat(insets).systemWindowInsetBottom
            }
            onKeyboardHeightChanged(imeBottom)
            insets
        }
        view.setOnApplyWindowInsetsListener(listener)
        onDispose { view.setOnApplyWindowInsetsListener(null) }
    }
}

@Composable
fun SendKeyboardHeightToJS(keyboardHeight: Int, webView: WebView) {
    LaunchedEffect(keyboardHeight) {
        if (keyboardHeight > 0) {
            val jsCode =
                makeKeyboardHeightResponse("receiveKeyboardHeight", keyboardHeight.toString())
            webView.evaluateJavascript(jsCode, null)
            Timber.d("WebViewScreen_sendKeyboardHeight: $jsCode")
        }
    }
}

@Composable
fun CopyToClipboard(review: String) {
    val context = LocalContext.current
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboard.setPrimaryClip(ClipData.newPlainText("Review", review))
}

@Composable
fun LoadingOverlay() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        LoadingAnimation()
    }
}
