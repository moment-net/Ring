package com.alan.module.main.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.alan.module.main.databinding.ActivityLoginPhoneBinding
import com.alan.module.main.viewmodel.LoginPhoneViewModel
import com.alan.mvvm.base.mvvm.vm.EmptyViewModel
import com.alan.mvvm.common.constant.RouteUrl
import com.alan.mvvm.common.ui.BaseActivity
import com.alibaba.android.arouter.facade.annotation.Route
import dagger.hilt.android.AndroidEntryPoint

/**
 * 作者：alan
 * 时间：2021/7/30
 * 备注：
 */
@Route(path = RouteUrl.MainModule.RING_ACTIVITY_PHONE)
@AndroidEntryPoint
class LoginPhoneActivity : BaseActivity<ActivityLoginPhoneBinding, LoginPhoneViewModel>() {

    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<LoginPhoneViewModel>()

    /**
     * 初始化View
     */
    override fun ActivityLoginPhoneBinding.initView() {

    }

    /**
     * 订阅数据
     */
    override fun initObserve() {
        mViewModel.lvPhone.value = "111111111"
    }

    /**
     * 获取数据
     */
    override fun initRequestData() {

    }
}
