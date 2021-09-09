package com.alan.module.chat.activity

import android.net.Uri
import androidx.activity.viewModels
import com.alan.module.chat.databinding.ActivityImageBinding
import com.alan.mvvm.base.coil.CoilUtils
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
@Route(path = RouteUrl.ChatModule.ACTIVITY_CHAT_IMAGE)
@AndroidEntryPoint
class ImageActivity : BaseActivity<ActivityImageBinding, EmptyViewModel>() {

    @JvmField
    @Autowired
    var uri: Uri? = null

    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<EmptyViewModel>()

    /**
     * 初始化View
     */
    override fun ActivityImageBinding.initView() {
        ivBack.clickDelay { finish() }
        CoilUtils.load(ivPic, uri!!)
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