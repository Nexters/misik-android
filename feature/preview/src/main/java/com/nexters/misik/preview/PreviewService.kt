package com.nexters.misik.preview

import android.Manifest
import android.app.Activity
import android.content.Intent
import androidx.activity.ComponentActivity
import com.nexters.misik.preview.ui.PreviewActivity
import com.nexters.misik.preview.util.ImageHandler
import com.nexters.misik.preview.util.MediaType
import com.nexters.misik.preview.util.PermissionHandler
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class PreviewService @Inject constructor(
    private val permissionHandler: PermissionHandler,
    private val imageHandlerUtil: ImageHandler,
) {
    private lateinit var activity: Activity

    fun init(activity: ComponentActivity) {
        this.activity = activity
        permissionHandler.init(activity) // 권한 요청 초기화
        imageHandlerUtil.init(activity) // 이미지 처리 초기화
    }

    fun openGallery(callback: (String?) -> Unit) {
        imageHandlerUtil.openMedia(MediaType.GALLERY, callback)
    }

    fun openCamera(callback: (String?) -> Unit) {
        permissionHandler.requestPermission(
            Manifest.permission.CAMERA,
            onGranted = { imageHandlerUtil.openMedia(MediaType.CAMERA, callback) },
        )
    }

    private fun startPreviewActivity(imageUri: String) {
        val intent = Intent(activity, PreviewActivity::class.java).apply {
            putExtra("imageUri", imageUri)
        }
        activity.startActivity(intent)
    }
}
