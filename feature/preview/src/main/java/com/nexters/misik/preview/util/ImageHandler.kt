package com.nexters.misik.preview.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import javax.inject.Inject

class ImageHandler @Inject constructor() {

    fun getGalleryIntent(): Intent {
        return Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
    }

    fun getCameraIntent(context: Context): Pair<Intent, Uri> {
        val uri = ImageStorageUtil.createImageUri(context)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_OUTPUT, uri)
        }
        return Pair(intent, uri)
    }
}