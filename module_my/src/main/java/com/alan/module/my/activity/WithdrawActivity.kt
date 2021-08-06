package com.alan.module.my.activity

import androidx.activity.viewModels
import com.alan.module.my.databinding.ActivityWithdrawBinding
import com.alan.module.my.viewmodol.WithdrawViewModel
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.common.constant.RouteUrl
import com.alan.mvvm.common.ui.BaseActivity
import com.alibaba.android.arouter.facade.annotation.Route
import dagger.hilt.android.AndroidEntryPoint

/**
 * 作者：alan
 * 时间：2021/7/30
 * 备注：
 */
@Route(path = RouteUrl.MyModule.ACTIVITY_MY_WITHDRAW)
@AndroidEntryPoint
class WithdrawActivity : BaseActivity<ActivityWithdrawBinding, WithdrawViewModel>() {

    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<WithdrawViewModel>()

    /**
     * 初始化View
     */
    override fun ActivityWithdrawBinding.initView() {
        ivBack.clickDelay { finish() }
        tvRight.clickDelay { }
        tvCommit.clickDelay { }
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