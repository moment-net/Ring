package com.alan.module.chat.activity

import android.net.Uri
import androidx.activity.viewModels
import com.alan.module.chat.databinding.ActivityVideoBinding
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.base.mvvm.vm.EmptyViewModel
import com.alan.mvvm.common.constant.RouteUrl
import com.alan.mvvm.common.ui.BaseActivity
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import dagger.hilt.android.AndroidEntryPoint

/**
 * 作者：alan
 * 时间：2021/7/30
 * 备注：
 */
@Route(path = RouteUrl.ChatModule.ACTIVITY_CHAT_VIDEO)
@AndroidEntryPoint
class VideoActivity : BaseActivity<ActivityVideoBinding, EmptyViewModel>() {

    @JvmField
    @Autowired
    var uri: String? = null


    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<EmptyViewModel>()

    /**
     * 初始化View
     */
    override fun ActivityVideoBinding.initView() {
        ivBack.clickDelay { finish() }
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
        initVideo()
    }


    fun initVideo() {
        val videoUri = Uri.parse(uri)
        mBinding.videoview.setVideoURI(videoUri);
        mBinding.videoview.requestFocus();
        mBinding.videoview.start();

    }
}