package com.alan.module.home.activity

import android.graphics.Color
import androidx.activity.viewModels
import com.alan.mvvm.common.ui.BaseActivity
import com.alan.module.home.databinding.HomeActivityMainBinding
import com.alan.module.home.viewmodel.HomeViewModel
import com.alan.mvvm.common.constant.RouteUrl
import com.alibaba.android.arouter.launcher.ARouter
import dagger.hilt.android.AndroidEntryPoint

/**
 * 作者：alan
 * 时间：2021/7/28
 * 备注：首页
 */
@AndroidEntryPoint
class MainActivity : BaseActivity<HomeActivityMainBinding, HomeViewModel>() {

    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<HomeViewModel>()

    override fun HomeActivityMainBinding.initView() {
        goToNextBtn.setOnClickListener {
            ARouter.getInstance().build(RouteUrl.MainModule.RING_ACTIVITY_MAIN).navigation()
        }
    }

    override fun initObserve() {
        // 订阅数据
        mViewModel.data.observe(this, {
            mBinding.vTvHello.text = it
            mBinding.vTvHello.setTextColor(Color.BLUE)
        })
    }

    override fun initRequestData() {
        // 模拟获取数据
        mViewModel.getData()
    }
}