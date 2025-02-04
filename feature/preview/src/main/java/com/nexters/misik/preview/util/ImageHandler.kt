package com.nexters.misik.preview.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.core.content.FileProvider
import java.io.File
import javax.inject.Inject

class ImageHandler @Inject constructor() {

    fun getGalleryIntent(): Intent {
        return Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
    }

    fun getCameraIntent(context: Context): Pair<Intent, Uri> {
        val file = File(context.cacheDir, "captured_image.jpg")
        val uri = FileProvider.getUriForFile(context, "com.example.fileprovider", file)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_OUTPUT, uri)
        }
        return Pair(intent, uri)
    }
}