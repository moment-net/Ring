package com.alan.module.my.dialog

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.InputFilter
import android.view.Gravity
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.alan.module.my.R
import com.alan.module.my.databinding.LayoutDialogCardInputBinding
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.base.ktx.dp2px
import com.alan.mvvm.base.mvvm.v.BaseFrameDialogFragment
import com.alan.mvvm.base.mvvm.vm.EmptyViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CardInputFragmentDialog :
    BaseFrameDialogFragment<LayoutDialogCardInputBinding, EmptyViewModel>() {

    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<EmptyViewModel>()
    var inputListener: OnInputListener? = null


    companion object {
        fun newInstance(tagName: String, name: String, limit: Int): CardInputFragmentDialog {
            val args = Bundle()
            args.putString("tagName", tagName)
            args.putString("name", name)
            args.putInt("limit", limit)
            val fragment = CardInputFragmentDialog()
            fragment.arguments = args
            return fragment
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
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        params.height = dp2px(600f)
        params.gravity = Gravity.BOTTOM
        setCanceledOnTouchOutside(false)
        isCancelable = false
        window.attributes = params
    }

    override fun LayoutDialogCardInputBinding.initView() {
        ivClose.clickDelay {
            dismiss()
            if (inputListener != null) {
                inputListener!!.onInput(etName.text.toString())
            }
        }


        val tagName = arguments?.getString("tagName", "")
        val name = arguments?.getString("name", "")
        val limit = arguments?.getInt("limit", 16)!!
        tvTitle.setText(tagName)
        etName.setText(name)
        etName.filters = arrayOf(InputFilter.LengthFilter(limit))
    }


    override fun initObserve() {

    }

    override fun initRequestData() {
    }

    interface OnInputListener {
        fun onInput(input: String)
    }
}