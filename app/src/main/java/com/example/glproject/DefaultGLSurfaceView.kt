package com.example.glproject

import android.content.Context
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.util.AttributeSet

class DefaultGLSurfaceView(context: Context, attributeSet: AttributeSet? = null) :
    GLSurfaceView(context, attributeSet) {
        init {
            setEGLContextClientVersion(3)
        }
}