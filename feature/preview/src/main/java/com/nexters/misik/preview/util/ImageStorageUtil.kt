package com.nexters.misik.preview.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

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

    fun loadBitmapFromFile(imagePath: String): Bitmap? {
        return BitmapFactory.decodeFile(imagePath)
    }

    // 📌 촬영한 이미지를 저장할 URI 생성
    fun createImageUri(context: Context): Uri {
        val file = File(context.cacheDir, "temp_image.jpg")
        return FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
    }
}
