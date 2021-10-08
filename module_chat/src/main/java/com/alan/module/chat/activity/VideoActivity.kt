package com.alan.module.chat.activity

import android.net.Uri
import android.widget.MediaController
import androidx.activity.viewModels
import com.alan.module.chat.databinding.ActivityVideoBinding
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.base.mvvm.vm.EmptyViewModel
import com.alan.mvvm.base.utils.toast
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
        showDialog()
        //加载指定的视频文件
        val videoUri = Uri.parse(uri)
        //创建MediaController对象
        val mediaController = MediaController(this)
        //VideoView与MediaController建立关联
        mBinding.videoview.setMediaController(mediaController);

        mBinding.videoview.setVideoURI(videoUri);

        mBinding.videoview.setOnPreparedListener {
            dismissDialog()
            mBinding.videoview.start()
            //让VideoView获取焦点
            mBinding.videoview.requestFocus();
        }
        mBinding.videoview.setOnErrorListener { mp, what, extra ->
            toast("该视频地址有误")
            this.finish()
            return@setOnErrorListener false
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        mBinding.videoview.stopPlayback()
    }
}