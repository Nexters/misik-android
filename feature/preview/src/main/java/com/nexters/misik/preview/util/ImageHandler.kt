package com.nexters.misik.preview.util

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import com.nexters.misik.preview.ui.PreviewActivity
import com.nexters.misik.preview.util.ImageStorageUtil.createImageUri
import com.nexters.misik.preview.util.ImageStorageUtil.getCameraImagePath

enum class MediaType { CAMERA, GALLERY }

class ImageHandler {
    private lateinit var activity: Activity
    private lateinit var galleryLauncher: ActivityResultLauncher<String>
    private lateinit var cameraLauncher: ActivityResultLauncher<Uri>
    private lateinit var previewResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var cameraUri: Uri
    private var callback: ((String?) -> Unit)? = null

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

        // PreviewActivity 결과 받기
        this.previewResultLauncher = activity.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val ocrText = result.data?.getStringExtra("imageUri")
                callback?.invoke(ocrText)  // 콜백 실행
            } else {
                callback?.invoke(null)  // 실패 시 null 전달
            }
        }
    }

    fun openMedia(type: MediaType, callback: (String?) -> Unit) {
        this.callback = callback
        when (type) {
            MediaType.GALLERY -> openGallery()
            MediaType.CAMERA -> openCamera()
        }
    }

    private fun openGallery() {
        galleryLauncher.launch("image/*")
    }

    private fun openCamera() {
        cameraLauncher.launch(cameraUri)
    }

    private fun startPreviewActivity(imageUri: String) {
        val intent = Intent(activity, PreviewActivity::class.java).apply {
            putExtra("imageUri", imageUri)
        }
        previewResultLauncher.launch(intent)
    }
}
