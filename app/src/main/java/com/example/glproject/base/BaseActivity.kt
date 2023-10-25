package com.example.glproject.base

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.media.SoundPool
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.dylanc.viewbinding.base.ActivityBinding
import com.dylanc.viewbinding.base.ActivityBindingDelegate
import com.tbruyelle.rxpermissions3.RxPermissions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel


/**
 * @author zc
 * @desc activity基类
 * @time 2020/6/10 2:03 PM
 */
open class BaseActivity<VB : ViewBinding>() : AppCompatActivity(),
    ActivityBinding<VB> by ActivityBindingDelegate(), CoroutineScope by MainScope() {

    val rxPermissions by lazy {
        RxPermissions(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) {
            fixOrientation()
        }

        super.onCreate(savedInstanceState)
        setContentViewWithBinding()

    }

    private fun fixOrientation() {
        try {
            val field = Activity::class.java.getDeclaredField("mActivityInfo")
            field.isAccessible = true
            val o = field.get(this) as ActivityInfo
            o.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            field.isAccessible = false
        } catch (e: Exception) {
        }
    }


    override fun onDestroy() {
        cancel()
//        intersTimer.cancel()

        super.onDestroy()
    }


    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    /**
     * 设置是否常亮
     */
    fun keepScreenNight(night: Boolean) {
        if (night) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }


    /**
     * 权限获取
     *
     * @param requirePermissions
     * @param optionPermissions
     * @param pendingAction
     * @param rejectAction
     */
    fun invokeWithPermission(
        permissions: Array<String>,
        observer: (granted: Boolean) -> Unit
    ) {
        rxPermissions.request(*permissions).subscribe(observer)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //权限框架
    }
}