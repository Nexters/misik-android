package com.nexters.misik.preview

import android.app.Activity
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.nexters.misik.preview.ui.PreviewActivity
import com.nexters.misik.preview.util.ImageHandler
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class PreviewService @Inject constructor(
    private val imageHandler: ImageHandler,
) {
    private lateinit var activity: Activity
    private lateinit var galleryLauncher: ActivityResultLauncher<String>
    private lateinit var cameraLauncher: ActivityResultLauncher<Intent>

    fun init(
        activity: ComponentActivity,
    ) {
        this.activity = activity
        this.galleryLauncher =
            activity.registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
                uri?.let { startPreviewActivity(it.toString()) }
            }

        this.cameraLauncher =
            activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val uri = result.data?.data
                    uri?.let { startPreviewActivity(it.toString()) }
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
        val (intent, _) = imageHandler.getCameraIntent(activity)
        cameraLauncher.launch(intent)
    }

    private fun startPreviewActivity(imageUri: String) {
        val intent = Intent(activity, PreviewActivity::class.java).apply {
            putExtra("imageUri", imageUri)
        }
        activity.startActivity(intent)
    }
}