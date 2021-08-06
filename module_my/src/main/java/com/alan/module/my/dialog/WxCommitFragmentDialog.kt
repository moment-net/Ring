package com.alan.module.my.dialog

import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.alan.module.my.R
import com.alan.module.my.databinding.LayoutWxcommitBinding
import com.alan.module.my.viewmodol.WxCommitViewModel
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.base.ktx.dp2px
import com.alan.mvvm.base.mvvm.v.BaseFrameDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WxCommitFragmentDialog : BaseFrameDialogFragment<LayoutWxcommitBinding, WxCommitViewModel>() {


    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<WxCommitViewModel>()


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
        params.width = dp2px(290f)
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        params.gravity = Gravity.CENTER
        setCanceledOnTouchOutside(true)
        isCancelable = true
        window.attributes = params
        mDimAmount = 0.8f
    }


    override fun LayoutWxcommitBinding.initView() {
        tvEdit.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        tvCommit.clickDelay { }
        tvCancle.clickDelay { }
        tvEdit.clickDelay { }
    }


    override fun initObserve() {
    }

    override fun initRequestData() {
    }


}