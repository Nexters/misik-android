package com.nexters.misik.preview

import android.Manifest
import android.app.Activity
import androidx.activity.ComponentActivity
import com.nexters.misik.core.ui.PreviewServiceContract
import com.nexters.misik.preview.util.ImageHandler
import com.nexters.misik.preview.util.MediaType
import com.nexters.misik.preview.util.PermissionHandler
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class PreviewService @Inject constructor(
    private val permissionHandler: PermissionHandler,
    private val imageHandlerUtil: ImageHandler,
) : PreviewServiceContract {
    private lateinit var activity: Activity

    override fun init(activity: ComponentActivity) {
        this.activity = activity
        permissionHandler.init(activity) // 권한 요청 초기화
        imageHandlerUtil.init(activity) // 이미지 처리 초기화
    }

    override fun openGallery(callback: (String?) -> Unit) {
        imageHandlerUtil.openMedia(MediaType.GALLERY, callback)
    }

    override fun openCamera(callback: (String?) -> Unit) {
        permissionHandler.requestPermission(
            Manifest.permission.CAMERA,
            onGranted = { imageHandlerUtil.openMedia(MediaType.CAMERA, callback) },
        )
    }
}
