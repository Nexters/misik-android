package com.nexters.misik.preview

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.activity.result.contract.ActivityResultContracts.TakePicturePreview
import com.nexters.misik.preview.ui.PreviewActivity
import com.nexters.misik.preview.util.ImageHandler
import com.nexters.misik.preview.util.ImageStorageUtil
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class PreviewService @Inject constructor(
    private val imageHandler: ImageHandler,
) {
    private lateinit var activity: Activity
    private lateinit var galleryLauncher: ActivityResultLauncher<String>
    private lateinit var cameraLauncher: ActivityResultLauncher<Void?>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>

    fun init(
        activity: ComponentActivity,
    ) {
        this.activity = activity
        this.galleryLauncher =
            activity.registerForActivityResult(GetContent()) { uri ->
                uri?.let { selectedUri ->
                    ImageStorageUtil.getFileFromUri(activity, selectedUri)?.absolutePath?.let {
                        startPreviewActivity(it)
                    }
                }
            }

        this.cameraLauncher =
            activity.registerForActivityResult(
                TakePicturePreview(),
            ) { bitmap: Bitmap? ->
                bitmap?.let {
                    startPreviewActivity(ImageStorageUtil.saveBitmapToFile(activity, it))
                }
            }

        this.permissionLauncher = activity.registerForActivityResult(
            ActivityResultContracts.RequestPermission(),
        ) { isGranted ->
            if (isGranted) {
                // 퍼미션이 허용되면 카메라 실행
                cameraLauncher.launch(null)
            } else {
                Toast.makeText(activity, "카메라 사용을 위해 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun openGallery() {
        if (!::galleryLauncher.isInitialized) {
            throw IllegalStateException("PreviewService is not initialized. Call init(activity) first.")
        }
        galleryLauncher.launch("image/*")
    }

    fun openCamera() {
        if (!::cameraLauncher.isInitialized) {
            throw IllegalStateException("PreviewService is not initialized. Call init(activity) first.")
        }
        // 퍼미션 확인 후 요청
        if (activity.checkSelfPermission(Manifest.permission.CAMERA) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            cameraLauncher.launch(null)
        } else {
            // 퍼미션 요청
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun startPreviewActivity(imageUri: String) {
        val intent = Intent(activity, PreviewActivity::class.java).apply {
            putExtra("imageUri", imageUri)
        }
        activity.startActivity(intent)
    }
}