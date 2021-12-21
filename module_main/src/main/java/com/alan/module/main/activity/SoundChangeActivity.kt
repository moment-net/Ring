package com.alan.module.main.activity

import androidx.activity.viewModels
import com.alan.module.main.databinding.ActivitySoundChangeBinding
import com.alan.module.main.viewmodel.SoundChangeViewModel
import com.alan.mvvm.common.constant.RouteUrl
import com.alan.mvvm.common.ui.BaseActivity
import com.alibaba.android.arouter.facade.annotation.Route
import dagger.hilt.android.AndroidEntryPoint

/**
 * 作者：alan
 * 时间：2021/7/30
 * 备注：捏声音
 */
@Route(path = RouteUrl.MainModule.ACTIVITY_MAIN_SOUNDCHANGE)
@AndroidEntryPoint
class SoundChangeActivity : BaseActivity<ActivitySoundChangeBinding, SoundChangeViewModel>() {

    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<SoundChangeViewModel>()

    /**
     * 初始化View
     */
    override fun ActivitySoundChangeBinding.initView() {

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