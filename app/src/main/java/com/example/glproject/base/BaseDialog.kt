package com.example.glproject.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.viewbinding.ViewBinding
import com.dylanc.viewbinding.base.ViewBindingUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

/**
 * @auther zc
 * @create time 19-1-8-下午2:55
 * @desc dialog基类
 */
open class BaseDialog<VB : ViewBinding>(
    var mContext: Context,
) : Dialog(mContext), LifecycleOwner, CoroutineScope by MainScope() {

    val mLifecycle by lazy {
        LifecycleRegistry(this)
    }

    init {
        if (mContext is FragmentActivity) {
            setOwnerActivity(mContext as FragmentActivity)
        }
        mLifecycle.currentState = Lifecycle.State.STARTED
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mLifecycle.currentState = Lifecycle.State.CREATED
        setContentViewWithBinding()
    }

    private lateinit var _binding: VB

    val binding: VB get() = _binding

    fun setContentViewWithBinding(): View {
        _binding = ViewBindingUtil.inflateWithGeneric(this, layoutInflater)
        setContentView(_binding.root)
        return _binding.root
    }

    override fun setContentView(layoutResID: Int) {
        throw UnsupportedOperationException("如果使用BaseBindingDialog,请不要使用此方法来设置视图，请直接使用：setContentView(view: View)")
    }

    override fun show() {
        if (ownerActivity?.isFinishing == true) {
            return
        }
        super.show()
        mLifecycle.currentState = Lifecycle.State.RESUMED
    }

    override fun dismiss() {
        try {
            super.dismiss()
        } catch (e: Exception) {
        }
        this.mLifecycle.currentState = Lifecycle.State.DESTROYED
        cancel("dialog dismiss")
    }


    override fun getLifecycle(): Lifecycle {
        return mLifecycle
    }

}