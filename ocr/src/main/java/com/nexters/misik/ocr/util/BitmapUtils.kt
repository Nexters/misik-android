package com.nexters.misik.ocr.util

import android.content.ContentResolver
import android.graphics.Bitmap
import android.provider.MediaStore

object BitmapUtils {
    fun getBitmapFromUri(contentResolver: ContentResolver, uri: android.net.Uri): Bitmap {
        return MediaStore.Images.Media.getBitmap(contentResolver, uri)
    }

    fun scaleBitmapDown(bitmap: Bitmap, maxDimension: Int): Bitmap {
        val originalWidth = bitmap.width
        val originalHeight = bitmap.height
        val resizedWidth: Int
        val resizedHeight: Int

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension
            resizedWidth = (resizedHeight * originalWidth.toFloat() / originalHeight).toInt()
        } else {
            resizedWidth = maxDimension
            resizedHeight = (resizedWidth * originalHeight.toFloat() / originalWidth).toInt()
        }

        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false)
    }
}
