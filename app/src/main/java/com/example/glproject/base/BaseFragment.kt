package com.example.glproject.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.dylanc.viewbinding.base.FragmentBinding
import com.dylanc.viewbinding.base.FragmentBindingDelegate
import com.tbruyelle.rxpermissions3.RxPermissions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope

/**
 * @author zc
 * @desc fragment基类
 * @time 2020/6/10 2:08 PM
 */
open abstract class BaseFragment<VH : ViewBinding> : Fragment(),
    CoroutineScope by MainScope(), FragmentBinding<VH> by FragmentBindingDelegate() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    val rxPermissions by lazy {
        RxPermissions(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createViewWithBinding(inflater, container)
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

}