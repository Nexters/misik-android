package com.nexters.misik.webview

import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun WebViewContainer() {
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            FrameLayout(context).apply {
                id = View.generateViewId()

                (context as? AppCompatActivity)?.supportFragmentManager?.beginTransaction()
                    ?.replace(id, WebViewFragment())
                    ?.commitAllowingStateLoss()
            }
        },
    )
}