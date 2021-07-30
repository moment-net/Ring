package com.alan.mvvm.common.ui

import androidx.viewbinding.ViewBinding
import com.alan.mvvm.base.mvvm.v.BaseFrameFragment
import com.alan.mvvm.base.mvvm.vm.BaseViewModel

/**
 * 作者：alan
 * 时间：2021/7/28
 * 备注：Fragment基类
 */
abstract class BaseFragment<VB : ViewBinding, VM : BaseViewModel> : BaseFrameFragment<VB, VM>()