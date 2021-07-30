package com.alan.module.main.activity

import androidx.activity.viewModels
import com.alan.module.main.databinding.ActivityMainBinding
import com.alan.module.main.viewmodel.HomeViewModel
import com.alan.module.main.viewmodel.MainViewModel
import com.alan.mvvm.common.constant.RouteUrl
import com.alan.mvvm.common.ui.BaseActivity
import com.alibaba.android.arouter.facade.annotation.Route
import dagger.hilt.android.AndroidEntryPoint

/**
 * 作者：alan
 * 时间：2021/7/28
 * 备注：首页
 */
@Route(path = RouteUrl.MainModule.RING_ACTIVITY_MAIN)
@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<MainViewModel>()

    /**
     * 初始化View
     */
    override fun ActivityMainBinding.initView() {

    }

    /**
     * 订阅数据
     */
    override fun initObserve() {

    }

    /**
     * 获取数据
     */
    override fun initRequestData() {

    }
}