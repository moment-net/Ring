package com.alan.module.main.activity

import android.Manifest
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.activity.viewModels
import com.alan.module.main.R
import com.alan.module.main.databinding.ActivitySoundTestBinding
import com.alan.module.main.dialog.EvaluateFragmentDialog
import com.alan.module.main.viewmodel.SoundTestViewModel
import com.alan.mvvm.base.http.responsebean.ConfigBean
import com.alan.mvvm.base.http.responsebean.SoundResultBean
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.base.utils.jumpARoute
import com.alan.mvvm.common.constant.RouteUrl
import com.alan.mvvm.common.ui.BaseActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.permissionx.guolindev.PermissionX
import com.socks.library.KLog
import dagger.hilt.android.AndroidEntryPoint

/**
 * 作者：alan
 * 时间：2021/7/30
 * 备注：声音测试
 */
@Route(path = RouteUrl.MainModule.ACTIVITY_MAIN_SOUND)
@AndroidEntryPoint
class SoundTestActivity : BaseActivity<ActivitySoundTestBinding, SoundTestViewModel>() {
    private val REQUESTED_PERMISSIONS = arrayOf(
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    val RECORD_LIMIT = 15
    var isRecording = false
    var MESSAGE_START: Int = 100
    var dialog: EvaluateFragmentDialog? = null
    private val handler: TimeHandler = TimeHandler(Looper.myLooper()!!)


    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<SoundTestViewModel>()

    /**
     * 初始化View
     */
    override fun ActivitySoundTestBinding.initView() {
        ivBack.clickDelay {
            finish()
        }

        ivSound.clickDelay {
            if (!isRecording) {
                requestPermissions()
            } else {
                handler.removeMessages(MESSAGE_START)
                stopRecorder()
            }
        }
    }


    /**
     * 订阅数据
     */
    override fun initObserve() {
        mViewModel.ldData.observe(this) {
            when (it) {
                is SoundResultBean -> {
                    dialog?.dismiss()
                    finish()
                    val bundle = Bundle().apply {
                        putParcelable("bean", it)
                    }
                    jumpARoute(RouteUrl.MainModule.ACTIVITY_MAIN_SOUNDRESULT, bundle)
                }

                is ConfigBean -> {
                    mBinding.tvContent.setText(it.content)
                }
            }
        }
    }

    /**
     * 获取数据
     */
    override fun initRequestData() {
        mViewModel.requestConfigByName()
    }


    /**
     * 请求录音权限
     */
    fun requestPermissions() {
        PermissionX.init(this).permissions(REQUESTED_PERMISSIONS[0], REQUESTED_PERMISSIONS[1])
            .request { allGranted, grantedList, deniedList ->
                if (allGranted) {
                    startRecorder()
                }
            }
    }


    inner class TimeHandler(myLooper: Looper) : Handler(myLooper) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MESSAGE_START -> {
                    val num = msg.obj as Int + 1
                    KLog.e("xujm", "当前秒数：" + num)
                    setTime(num)
                    if (num == RECORD_LIMIT) {
                        stopRecorder()
                    } else {
                        val message = handler.obtainMessage(MESSAGE_START, num)
                        handler.sendMessageDelayed(message, 1000)
                    }
                }
            }
        }
    }

    /**
     * 开始录制
     */
    fun startRecorder() {
        isRecording = true
        KLog.e("xujm", "数值：" + RECORD_LIMIT)
        mViewModel.startRecorder()
        mBinding.ivSound.setImageResource(R.drawable.icon_sound_stop)
        setTime(0)
        val message = handler.obtainMessage(MESSAGE_START, 0)
        handler.sendMessageDelayed(message, 1000)
    }

    /**
     * 停止录制
     */
    fun stopRecorder() {
        isRecording = false
        mBinding.ivSound.setImageResource(R.drawable.icon_sound_play)
        setTime(RECORD_LIMIT)
        mViewModel.stopRecorder()
        showDialog()
        mViewModel.requestUploadAudio()
    }

    /**
     * 控制时间按钮显示
     */
    fun setTime(time: Int) {
        if (time == RECORD_LIMIT) {
            mBinding.tvTip.setText("点击录音")
        } else {
            mBinding.tvTip.setText("录音中${time}s")
        }
    }

    fun showDialog() {
        dialog = EvaluateFragmentDialog.newInstance()
        dialog?.show(supportFragmentManager)
    }

}