package com.example.edgeapp

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class GLRenderer(private val ctx: Context) : GLSurfaceView.Renderer {
    private var texId = IntArray(1)
    private var textureBuffer: ByteBuffer? = null
    private var w = 0
    private var h = 0

    fun updateTexture(bytes: ByteArray, width: Int, height: Int) {
        w = width; h = height
        textureBuffer = ByteBuffer.allocateDirect(bytes.size).order(ByteOrder.nativeOrder())
        textureBuffer?.put(bytes)
        textureBuffer?.position(0)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glGenTextures(1, texId, 0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId[0])
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0,0,width,height)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        if (textureBuffer != null && w>0 && h>0) {
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId[0])
            GLES20.glTexImage2D(
                GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, w, h, 0,
                GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, textureBuffer
            )
            // draw textured quad - minimal: assume a helper draws the full-screen quad
            TexturedQuad.draw(texId[0])
        }
    }
}
