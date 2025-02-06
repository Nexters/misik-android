package com.nexters.misik.preview.util

import android.app.Activity
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import com.nexters.misik.preview.util.ImageStorageUtil.createImageUri
import com.nexters.misik.preview.util.ImageStorageUtil.getCameraImagePath

class ImageHandler {
    private lateinit var activity: Activity
    private lateinit var galleryLauncher: ActivityResultLauncher<String>
    private lateinit var cameraLauncher: ActivityResultLauncher<Uri>
    private lateinit var cameraUri: Uri

    fun init(activity: ComponentActivity) {
        this.activity = activity

        // 갤러리 실행
        this.galleryLauncher = activity.registerForActivityResult(GetContent()) { uri ->
            uri?.let { selectedUri ->
                ImageStorageUtil.getFileFromUri(activity, selectedUri)?.absolutePath?.let {
                    startPreviewActivity(it)
                }
            }
        }

        // 카메라 실행
        this.cameraUri = createImageUri(activity)
        cameraLauncher =
            activity.registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
                if (success) {
                    getCameraImagePath(activity, cameraUri)?.let { fallbackPath ->
                        startPreviewActivity(fallbackPath)
                    }
                }
            }
    }

    fun openGallery() {
        galleryLauncher.launch("image/*")
    }

    fun openCamera() {
        cameraLauncher.launch(cameraUri)
    }

    private fun startPreviewActivity(imageUri: String) {
        // 갤러리 및 카메라에서 얻은 이미지 -> preview activity로 전송
        val intent = android.content.Intent(
            activity,
            com.nexters.misik.preview.ui.PreviewActivity::class.java,
        ).apply {
            putExtra("imageUri", imageUri)
        }
        activity.startActivity(intent)
    }
}
