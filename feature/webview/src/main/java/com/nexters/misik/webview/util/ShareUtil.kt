package com.nexters.misik.webview.util

import android.content.Context
import android.content.Intent
import com.nexters.misik.feature.webview.R

object ShareUtil {

    /**
     * 기본 공유 기능 실행
     */
    fun shareApp(context: Context) {
        shareTextIntent(context)
    }

    /**
     * 기본 텍스트 공유 기능
     */
    private fun shareTextIntent(context: Context) {
        val title = context.getString(R.string.share_title)
        val description = context.getString(R.string.share_description)
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, description)
        }
        context.startActivity(Intent.createChooser(shareIntent, title))
    }
}
