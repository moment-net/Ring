package com.alan.module.main.dialog

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.alan.module.main.R
import com.alan.module.main.databinding.LayoutPushBinding
import com.alan.module.main.viewmodel.PushViewModel
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.base.ktx.dp2px
import com.alan.mvvm.base.mvvm.v.BaseFrameDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PushFragmentDialog : BaseFrameDialogFragment<LayoutPushBinding, PushViewModel>() {

    companion object {
        fun newInstance(): PushFragmentDialog {
            val bundle = Bundle()
            val dialog = PushFragmentDialog()
            dialog.setArguments(bundle)
            return dialog
        }
    }

    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<PushViewModel>()


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
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        params.gravity = Gravity.CENTER
        setCanceledOnTouchOutside(false)
        isCancelable = false
        window.attributes = params
    }


    override fun LayoutPushBinding.initView() {
        ivClose.clickDelay {
            dismiss()
        }

    }


    override fun initObserve() {
    }

    override fun initRequestData() {

    }


}