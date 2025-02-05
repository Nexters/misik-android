package com.nexters.misik.webview

import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import timber.log.Timber

@Composable
fun WebViewScreen(
    webAppInterface: WebInterface,
    modifier: Modifier = Modifier,
    viewModel: WebViewViewModel = hiltViewModel(),
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    // 페이지 로딩 이벤트 발생 (WebView 로드 시작 시)
    LaunchedEffect(Unit) {
        viewModel.onEvent(WebViewEvent.LoadPage)
        viewModel.generateReview()
        viewModel.getReview()
    }

    Box(modifier = modifier.fillMaxSize()) {
        // 로딩 상태 UI
        if (uiState.isLoading) {
            Timber.d("WebViewScreen_UiState", "Loading")
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
            )
        }

        // 콘텐츠가 있을 경우, WebView를 보여줌
        if (!uiState.isLoading) {
            Timber.d("WebViewScreen_UiState", "Loaded")
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->
                    WebView(context).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                        )
                        settings.javaScriptEnabled = true
                        webViewClient = WebViewClient()
                        webChromeClient = WebChromeClient()

                        addJavascriptInterface(webAppInterface, "AndroidBridge")

                        loadUrl("https://misik-web.vercel.app/")

                        // 페이지 로딩 완료 후 이벤트 처리
                        webViewClient = object : WebViewClient() {
                            override fun onPageFinished(view: WebView?, url: String?) {
                                super.onPageFinished(view, url)
                                viewModel.onEvent(WebViewEvent.PageLoaded)
                            }

                            override fun onReceivedError(
                                view: WebView?,
                                request: WebResourceRequest?,
                                error: WebResourceError?,
                            ) {
                                super.onReceivedError(view, request, error)
                                // 오류 발생 시 이벤트 호출
                                viewModel.onEvent(WebViewEvent.JsError("Error loading page: ${error?.description}"))
                            }
                        }
                    }
                },
            )
        }
    }
}
