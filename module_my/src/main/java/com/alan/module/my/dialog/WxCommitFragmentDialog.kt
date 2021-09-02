package com.alan.module.my.dialog

import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
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


    companion object {
        fun newInstance(wxAccount: String?): WxCommitFragmentDialog {
            val bundle = Bundle()
            bundle.putString("wxAccount", wxAccount)
            val dialog = WxCommitFragmentDialog()
            dialog.setArguments(bundle)
            return dialog
        }
    }

    var dialogListener: DialogOnClickListener? = null
        set(value) {
            field = value
        }

    var wxAccount: String? = null

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
        tvCommit.clickDelay {
            if (dialogListener != null) {
                dialogListener!!.onCommitClick()
            }
            dismiss()
        }
        tvCancle.clickDelay {
            if (dialogListener != null) {
                dialogListener!!.onCancelClick()
            }
            dismiss()
        }
        tvEdit.clickDelay {
            if (dialogListener != null) {
                dialogListener!!.onEditClick()
            }
            dismiss()
        }

        wxAccount = arguments?.getString("wxAccount")
        tvEdit.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG)
        tvInfo.setText(wxAccount)
    }


    override fun initObserve() {

    }

    override fun initRequestData() {
    }


    interface DialogOnClickListener {
        fun onCancelClick();
        fun onCommitClick();
        fun onEditClick();
    }


}

