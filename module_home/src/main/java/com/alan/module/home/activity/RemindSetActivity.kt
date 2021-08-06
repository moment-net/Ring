package com.alan.module.home.activity

import androidx.activity.viewModels
import com.alan.module.home.databinding.ActivityRemindSetBinding
import com.alan.module.home.viewmodol.RemindSetViewModel
import com.alan.mvvm.common.constant.RouteUrl
import com.alan.mvvm.common.ui.BaseActivity
import com.alibaba.android.arouter.facade.annotation.Route
import dagger.hilt.android.AndroidEntryPoint

/**
 * 作者：alan
 * 时间：2021/7/30
 * 备注：设置提醒
 */
@Route(path = RouteUrl.HomeModule.ACTIVITY_HOME_REMIND)
@AndroidEntryPoint
class RemindSetActivity : BaseActivity<ActivityRemindSetBinding, RemindSetViewModel>() {

    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<RemindSetViewModel>()

    /**
     * 初始化View
     */
    override fun ActivityRemindSetBinding.initView() {

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