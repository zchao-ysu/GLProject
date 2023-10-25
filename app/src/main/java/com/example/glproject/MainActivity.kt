package com.example.glproject

import android.content.Intent
import android.os.Bundle
import com.example.glproject.base.BaseActivity
import com.example.glproject.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>() {
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.button.setOnClickListener {
            startActivity(Intent(this, TestActivity::class.java))
        }
    }
}