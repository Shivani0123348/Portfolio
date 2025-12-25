package com.example.edgeapp

import android.graphics.Bitmap
import android.media.Image
import java.nio.ByteBuffer

object ImageUtils {
    var width = 0
    var height = 0

    // Convert YUV420_888 -> NV21 -> Bitmap -> RGBA byte[]
    fun yuv420ToRGBA(image: Image): ByteArray {
        width = image.width
        height = image.height
        val yBuffer = image.planes[0].buffer
        val uBuffer = image.planes[1].buffer
        val vBuffer = image.planes[2].buffer
        val ySize = yBuffer.remaining()
        val uSize = uBuffer.remaining()
        val vSize = vBuffer.remaining()

        val nv21 = ByteArray(ySize + uSize + vSize)
        yBuffer.get(nv21, 0, ySize)
        vBuffer.get(nv21, ySize, vSize)
        uBuffer.get(nv21, ySize + vSize, uSize)

        // convert NV21 to Bitmap (use android API)
        val yuvImage = android.graphics.YuvImage(nv21, android.graphics.ImageFormat.NV21, width, height, null)
        val out = java.io.ByteArrayOutputStream()
        yuvImage.compressToJpeg(android.graphics.Rect(0,0,width,height), 90, out)
        val imageBytes = out.toByteArray()
        val bmp = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        val argb = IntArray(width * height)
        bmp.getPixels(argb, 0, width, 0, 0, width, height)
        val rgba = ByteArray(width * height * 4)
        var i = 0
        for (p in argb) {
            rgba[i++] = ((p shr 16) and 0xFF).toByte() // R
            rgba[i++] = ((p shr 8) and 0xFF).toByte()  // G
            rgba[i++] = (p and 0xFF).toByte()          // B
            rgba[i++] = ((p ushr 24) and 0xFF).toByte()// A
        }
        return rgba
    }
}
