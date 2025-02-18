package com.nexters.misik.webview.util

import android.content.Context
import android.content.Intent
import com.nexters.misik.feature.webview.R

object ShareUtil {

    /**
     * 기본 공유 기능 실행
     */
    fun shareApp(context: Context, shareText: String) {
        shareTextIntent(context, shareText)
    }

    /**
     * 기본 텍스트 공유 기능
     */
    private fun shareTextIntent(context: Context, shareText: String) {
        val title = context.getString(R.string.share_title)
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
        }
        context.startActivity(Intent.createChooser(shareIntent, title))
    }
}
