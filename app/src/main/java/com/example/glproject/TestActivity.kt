package com.example.glproject

import android.app.ActivityManager
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.widget.SeekBar
import com.example.glproject.base.BaseActivity
import com.example.glproject.databinding.TestActivityBinding

class TestActivity() : BaseActivity<TestActivityBinding>() {
    private val TAG = "TestActivity"
    val mRenderer by lazy {
        TestRenderer(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val configurationInfo =
            getSystemService(ActivityManager::class.java).deviceConfigurationInfo;
        val supportsEs3 = configurationInfo.reqGlEsVersion >= 0x30000

        binding.glSurfaceView.debugFlags = GLSurfaceView.DEBUG_LOG_GL_CALLS
        binding.glSurfaceView.setEGLContextClientVersion(3)
        binding.glSurfaceView.setRenderer(mRenderer)
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                mRenderer.r = progress / 100f
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
    }
}