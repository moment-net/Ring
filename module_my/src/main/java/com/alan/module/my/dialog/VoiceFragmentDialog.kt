package com.alan.module.my.dialog

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.alan.module.my.R
import com.alan.module.my.databinding.LayoutDialogVoiceBinding
import com.alan.module.my.viewmodol.VoiceViewModel
import com.alan.mvvm.base.http.responsebean.UserInfoBean
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.base.ktx.dp2px
import com.alan.mvvm.base.mvvm.v.BaseFrameDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

@AndroidEntryPoint
class VoiceFragmentDialog : BaseFrameDialogFragment<LayoutDialogVoiceBinding, VoiceViewModel>() {


    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<VoiceViewModel>()
    lateinit var timeJob: Job

    companion object {
        fun newInstance(): VoiceFragmentDialog {
            val bundle = Bundle()
            val dialog = VoiceFragmentDialog()
            dialog.setArguments(bundle)
            return dialog
        }
    }


    override fun LayoutDialogVoiceBinding.initView() {
        ivCancel.clickDelay { dismiss() }
        ivCommit.clickDelay {
            timeJob.cancel()
            mViewModel.stopRecorder()
            mViewModel.requestUploadAudio()
        }
    }


    override fun initWindow() {
        super.initWindow()
        val window = dialog!!.window
        val params = window!!.attributes
        val colorDrawable = ColorDrawable(
            ContextCompat.getColor(
                mActivity, R.color.transparent
            )
        )
        window.setBackgroundDrawable(colorDrawable)
        params.width = dp2px(300f)
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        params.gravity = Gravity.CENTER
        setCanceledOnTouchOutside(true)
        isCancelable = true
        window.attributes = params
    }

    override fun initObserve() {
        mViewModel.ldSuccess.observe(this) {
            when (it) {
                is UserInfoBean -> {
                    dismiss()
                }
            }
        }
    }

    override fun initRequestData() {
        startRecorder()
    }

    /**
     * 开始录制
     */
    fun startRecorder() {
        mViewModel.startRecorder()
        timeJob = lifecycleScope.launch {
            flow<Int> {
                for (i in 0..10) {
                    delay(1000)
                    emit(i)
                }
            }.collect {
                if (it == 10) {
                    mViewModel.stopRecorder()
                    mViewModel.requestUploadAudio()
                } else {
                    mBinding.tvTime.setText("${10 - it}s")
                }
            }
        }
    }

}