package com.nexters.misik.webview

import android.content.Context
import com.nexters.misik.domain.WebBridgeContract
import javax.inject.Inject

class WebBridgeImpl @Inject constructor(
    private val context: Context,
) : WebBridgeContract {
    override fun openCamera() {
        // 카메라 실행
    }

    override fun openGallery() {
        // 갤러리 실행
    }

    override fun share(content: String) {
        // 공유
    }

    override fun createReview(ocrText: String, hashTags: List<String>, reviewStyle: String) {
        // 리뷰 생성
    }

    override fun copy(review: String) {
        // 텍스트 복사
    }
}
