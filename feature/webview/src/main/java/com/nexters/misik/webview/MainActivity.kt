package com.nexters.misik.webview

import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.nexters.misik.webview.MainActivity.Companion.WEB_URL
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var webAppInterface: WebInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                WebViewScreen(webAppInterface, modifier = Modifier.padding(innerPadding))
            }

        }
    }

    companion object {
        const val WEB_URL = "https://misik-web.vercel.app/"
    }
}

@Composable
fun WebViewScreen(
    webAppInterface: WebInterface,
    modifier: Modifier = Modifier
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                webViewClient = WebViewClient()
                webChromeClient = WebChromeClient()
                addJavascriptInterface(webAppInterface, "AndroidBridge")
                loadUrl(WEB_URL)
            }
        }
    )
}


