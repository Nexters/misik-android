package com.nexters.misik.ocr.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint

object BitmapUtils {

    fun preprocessImage(bitmap: Bitmap): Bitmap {
        var processedBitmap = convertToGrayscale(bitmap)
        processedBitmap = scaleBitmapDown(processedBitmap, 1024) // 최대 크기 1024로 제한
        processedBitmap = adjustContrast(processedBitmap, 1.5f) // 대비 증가
        return processedBitmap
    }

    private fun convertToGrayscale(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val grayscaleBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(grayscaleBitmap)
        val paint = Paint()
        val colorMatrix = ColorMatrix()
        colorMatrix.setSaturation(0f) // 흑백 처리
        val colorFilter = ColorMatrixColorFilter(colorMatrix)
        paint.colorFilter = colorFilter
        canvas.drawBitmap(bitmap, 0f, 0f, paint)

        return grayscaleBitmap
    }

    fun scaleBitmapDown(bitmap: Bitmap, maxDimension: Int): Bitmap {
        val originalWidth = bitmap.width
        val originalHeight = bitmap.height
        val resizedWidth: Int
        val resizedHeight: Int

        if (originalWidth <= maxDimension && originalHeight <= maxDimension) return bitmap

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension
            resizedWidth = (resizedHeight * originalWidth.toFloat() / originalHeight).toInt()
        } else {
            resizedWidth = maxDimension
            resizedHeight = (resizedWidth * originalHeight.toFloat() / originalWidth).toInt()
        }

        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, true)
    }

    private fun adjustContrast(bitmap: Bitmap, contrast: Float): Bitmap {
        val cm = ColorMatrix(
            floatArrayOf(
                contrast, 0f, 0f, 0f, 0f,
                0f, contrast, 0f, 0f, 0f,
                0f, 0f, contrast, 0f, 0f,
                0f, 0f, 0f, 1f, 0f,
            ),
        )
        val paint = Paint()
        paint.colorFilter = ColorMatrixColorFilter(cm)

        val resultBitmap = Bitmap.createBitmap(
            bitmap.width,
            bitmap.height,
            bitmap.config ?: Bitmap.Config.ARGB_8888,
        )
        val canvas = Canvas(resultBitmap)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)

        return resultBitmap
    }

    /*    private fun applyGaussianBlur(bitmap: Bitmap): Bitmap {
            val rs = RenderScript.create(context)
            val input = Allocation.createFromBitmap(rs, bitmap)
            val output = Allocation.createTyped(rs, input.type)
            val script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
            script.setRadius(5f) // 블러 강도 (1f ~ 25f)
            script.setInput(input)
            script.forEach(output)
            output.copyTo(bitmap)
            return bitmap
        }*/

}
