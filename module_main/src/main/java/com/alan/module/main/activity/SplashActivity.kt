package com.alan.module.main.activity

import androidx.activity.viewModels
import com.alan.module.main.databinding.ActivitySplashBinding
import com.alan.module.main.viewmodel.SplashViewModel
import com.alan.mvvm.common.ui.BaseActivity
import dagger.hilt.android.AndroidEntryPoint


/**
 * 作者：alan
 * 时间：2021/7/30
 * 备注：启动页
 */

@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding, SplashViewModel>() {

    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<SplashViewModel>()

    /**
     * 初始化View
     */
    override fun ActivitySplashBinding.initView() {

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