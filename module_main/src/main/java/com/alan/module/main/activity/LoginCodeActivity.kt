package com.alan.module.main.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.alan.module.main.databinding.ActivityLoginCodeBinding
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
@Route(path = RouteUrl.MainModule.RING_ACTIVITY_CODE)
@AndroidEntryPoint
class LoginCodeActivity : BaseActivity<ActivityLoginCodeBinding, EmptyViewModel>() {

    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<EmptyViewModel>()

    /**
     * 初始化View
     */
    override fun ActivityLoginCodeBinding.initView() {

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