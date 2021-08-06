package com.alan.module.home.dialog

import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.alan.module.main.R
import com.alan.module.main.databinding.LayoutRemindBinding
import com.alan.module.main.viewmodel.RemindViewModel
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.base.ktx.dp2px
import com.alan.mvvm.base.mvvm.v.BaseFrameDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RemindFragmentDialog : BaseFrameDialogFragment<LayoutRemindBinding, RemindViewModel>() {


    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<RemindViewModel>()

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
        params.width = dp2px(320f)
        params.height = dp2px(480f)
        params.gravity = Gravity.CENTER
        setCanceledOnTouchOutside(true)
        isCancelable = true
        window.attributes = params
    }

    override fun LayoutRemindBinding.initView() {
        tvVoice.clickDelay {
            ivVoice.setImageResource(0)
            (ivVoice.background as AnimationDrawable).start()
        }
        tvCommit.clickDelay { }
    }


    override fun initObserve() {
    }

    override fun initRequestData() {
    }


}