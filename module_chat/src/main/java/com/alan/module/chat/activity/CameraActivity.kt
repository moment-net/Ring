package com.alan.module.chat.activity

import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.view.View
import androidx.activity.viewModels
import com.alan.module.chat.databinding.ActivityCameraBinding
import com.alan.module.im.constants.IMConstant
import com.alan.mvvm.base.mvvm.vm.EmptyViewModel
import com.alan.mvvm.base.utils.BitmapUtil
import com.alan.mvvm.common.constant.Constants
import com.alan.mvvm.common.constant.RouteUrl
import com.alan.mvvm.common.ui.BaseActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.cjt2325.cameralibrary.listener.JCameraListener
import dagger.hilt.android.AndroidEntryPoint

/**
 * 作者：alan
 * 时间：2021/7/30
 * 备注：
 */
@Route(path = RouteUrl.ChatModule.ACTIVITY_CHAT_CAMERA)
@AndroidEntryPoint
class CameraActivity : BaseActivity<ActivityCameraBinding, EmptyViewModel>() {

    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<EmptyViewModel>()

    /**
     * 初始化View
     */
    override fun ActivityCameraBinding.initView() {
        //设置视频保存路径
        jcamera.setSaveVideoPath(IMConstant.PATH_VEDIO)

        //JCameraView监听
        jcamera.setJCameraLisenter(object : JCameraListener {
            override fun captureSuccess(bitmap: Bitmap) {
                val path = BitmapUtil.save(this@CameraActivity, bitmap)
                val intent = Intent().apply {
                    putExtra("type", Constants.TYPE_IMAGE)
                    putExtra("bitmap", path)
                }
                setResult(RESULT_OK, intent)
                finish()
            }

            override fun recordSuccess(url: String, firstFrame: Bitmap) {
                val path = BitmapUtil.save(this@CameraActivity, firstFrame)
                val intent = Intent().apply {
                    putExtra("type", Constants.TYPE_VIDEO)
                    putExtra("bitmap", path)
                    putExtra("url", url)
                }
                setResult(RESULT_OK, intent)
                finish()
            }
        })
        jcamera.setLeftClickListener {
            finish()
        }

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

    override fun onStart() {
        super.onStart()
        //全屏显示
        if (Build.VERSION.SDK_INT >= 19) {
            val decorView = window.decorView
            decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        } else {
            val decorView = window.decorView
            val option = View.SYSTEM_UI_FLAG_FULLSCREEN
            decorView.systemUiVisibility = option
        }
    }

    override fun onPause() {
        super.onPause()
        mBinding.jcamera.onPause()
    }

    override fun onResume() {
        super.onResume()
        mBinding.jcamera.onResume()
    }
}