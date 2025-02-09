package com.nexters.misik.preview.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object ImageStorageUtil {

    fun saveBitmapToFile(context: Context, bitmap: Bitmap): String {
        val file = File(context.cacheDir, "temp_image.jpg")
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
        }
        return file.absolutePath
    }

    fun getFileFromUri(context: Context, uri: Uri): File? {
        val inputStream = context.contentResolver.openInputStream(uri) ?: return null
        val file = File(context.cacheDir, "temp_image.jpg")
        file.outputStream().use { output ->
            inputStream.copyTo(output)
        }
        return file
    }

    fun getCameraImagePath(context: Context, uri: Uri): String? {
        return run {
            fixImageRotation(context, uri) ?: getBitmapFromUri(context, uri)
        }?.let { bitmap ->
            saveBitmapToFile(context, bitmap)
        }
    }

    fun getBitmapFromUri(context: Context, uri: Uri): Bitmap? {
        return context.contentResolver.openInputStream(uri)?.use {
            BitmapFactory.decodeStream(it)
        }
    }

    // 촬영한 이미지를 저장할 URI 생성
    fun createImageUri(context: Context): Uri {
        val file = File(context.cacheDir, "temp_image.jpg")
        return FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
    }

    private fun fixImageRotation(context: Context, imageUri: Uri): Bitmap? {
        val bitmap = getBitmapFromUri(context, imageUri) ?: return null
        val inputStream = context.contentResolver.openInputStream(imageUri) ?: return bitmap

        try {
            val exif = ExifInterface(inputStream)
            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED,
            )

            val rotationAngle = when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90f
                ExifInterface.ORIENTATION_ROTATE_180 -> 180f
                ExifInterface.ORIENTATION_ROTATE_270 -> 270f
                else -> 0f
            }

            return rotateBitmap(bitmap, rotationAngle)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return bitmap
    }

    // Bitmap 회전 함수
    private fun rotateBitmap(bitmap: Bitmap, rotationAngle: Float): Bitmap {
        if (rotationAngle == 0f) return bitmap

        val matrix = Matrix().apply { postRotate(rotationAngle) }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }
}
