package com.nexters.misik.webview

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
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
import com.nexters.misik.preview.PreviewService
import com.nexters.misik.webview.base.MisikWebViewFactory
import com.nexters.misik.webview.bridge.WebInterface
import com.nexters.misik.webview.common.LoadingAnimation
import com.nexters.misik.webview.util.JsResponseUtil.makeKeyboardHeightResponse
import com.nexters.misik.webview.util.ShareUtil
import timber.log.Timber

@RequiresApi(Build.VERSION_CODES.R)
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

    val view = LocalView.current
    val keyboardHeight by viewModel.keyboardHeight.collectAsState()

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
                    ShareUtil.shareApp(context, intent.shareText)
                }

                else -> viewModel.sendIntent(intent)
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.getVersionUpdateStatus()
    }

    val initializedUrl by rememberUpdatedState(
        when (val state = uiState) {
            is WebViewState.CheckIsUpdateRequired -> {
                state.url
            }

            else -> {
                ""
            }
        },
    )

    val webView = remember {
        MisikWebViewFactory.create(
            context = context,
            webInterface = webInterface,
            onEvent = { event -> viewModel.onEvent(event) },
        )
    }

    LaunchedEffect(initializedUrl) {
        if (initializedUrl.isNotEmpty()) {
            webView.loadUrl(initializedUrl)
            Timber.d("WebViewScreen_LoadingUrl: $initializedUrl")
        }
    }

    DisposableEffect(view) {
        val listener = View.OnApplyWindowInsetsListener { v, insets ->
            val ime = insets.getInsets(WindowInsetsCompat.Type.ime()) // 키보드 높이 가져옴
            viewModel.updateKeyboardHeight(ime.bottom) // 뷰모델에 업데이트
            insets // 원래의 insets 반환
        }
        view.setOnApplyWindowInsetsListener(listener)
        onDispose { view.setOnApplyWindowInsetsListener(null) }
    }

    // 키보드 높이 변화 시 웹에 전달
    LaunchedEffect(keyboardHeight) {
        if (keyboardHeight > 0) { // 키보드가 올라왔을 때만 전달
            val jsCode =
                makeKeyboardHeightResponse("receiveKeyboardHeight", keyboardHeight.toString())
            webView.evaluateJavascript(jsCode, null)
            Timber.d("WebViewScreen_sendKeyboardHeight", jsCode)
        }
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
