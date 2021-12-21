package com.alan.module.main.dialog

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.alan.module.main.R
import com.alan.module.main.databinding.LayoutDialogEvaluateBinding
import com.alan.module.main.viewmodel.EvaluateViewModel
import com.alan.mvvm.base.mvvm.v.BaseFrameDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EvaluateFragmentDialog :
    BaseFrameDialogFragment<LayoutDialogEvaluateBinding, EvaluateViewModel>() {

    companion object {
        fun newInstance(): EvaluateFragmentDialog {
            val bundle = Bundle()
            val dialog = EvaluateFragmentDialog()
            dialog.setArguments(bundle)
            return dialog
        }
    }

    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<EvaluateViewModel>()


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
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        params.height = WindowManager.LayoutParams.MATCH_PARENT
        params.gravity = Gravity.CENTER
        setCanceledOnTouchOutside(false)
        isCancelable = false
        window.attributes = params
    }


    override fun LayoutDialogEvaluateBinding.initView() {

    }


    override fun initObserve() {
    }

    override fun initRequestData() {

    }


}