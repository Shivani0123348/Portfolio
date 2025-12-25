package com.example.edgeapp

import android.app.Activity
import android.graphics.ImageFormat
import android.media.ImageReader
import android.os.Bundle
import android.util.Log
import android.view.Surface
import android.view.TextureView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var textureView: TextureView
    private lateinit var cameraHelper: CameraHelper
    private lateinit var renderer: GLRenderer

    companion object {
        init { System.loadLibrary("native-lib") }
    }

    external fun nativeProcessFrame(pixels: ByteArray, w: Int, h: Int, toggle: Int): ByteArray

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        textureView = TextureView(this)
        setContentView(textureView)

        renderer = GLRenderer(this)
        // attach renderer's GLSurfaceView or SurfaceTexture to layout as needed
        cameraHelper = CameraHelper(this)

        cameraHelper.setOnImageAvailableListener { image ->
            // Convert image (YUV) to RGBA byte[] (helper below)
            val rgba = ImageUtils.yuv420ToRGBA(image)
            image.close()
            // call native processing (0 = grayscale, 1 = Canny)
            val processed = nativeProcessFrame(rgba, ImageUtils.width, ImageUtils.height, 1)
            // update GL texture with processed bytes
            renderer.updateTexture(processed, ImageUtils.width, ImageUtils.height)
        }
    }

    override fun onResume() {
        super.onResume()
        cameraHelper.openCamera()
        renderer.onResume()
    }

    override fun onPause() {
        super.onPause()
        cameraHelper.closeCamera()
        renderer.onPause()
    }
}
