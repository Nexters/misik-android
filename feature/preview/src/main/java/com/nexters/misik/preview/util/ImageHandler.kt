package com.nexters.misik.preview.util

import android.app.Activity
import android.graphics.Bitmap
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.activity.result.contract.ActivityResultContracts.TakePicturePreview

class ImageHandler {
    private lateinit var activity: Activity
    private lateinit var galleryLauncher: ActivityResultLauncher<String>
    private lateinit var cameraLauncher: ActivityResultLauncher<Void?>

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
        this.cameraLauncher =
            activity.registerForActivityResult(TakePicturePreview()) { bitmap: Bitmap? ->
                bitmap?.let {
                    startPreviewActivity(ImageStorageUtil.saveBitmapToFile(activity, it))
                }
            }
    }

    fun openGallery() {
        galleryLauncher.launch("image/*")
    }

    fun openCamera() {
        cameraLauncher.launch(null)
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
