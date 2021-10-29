package com.alan.module.my.dialog

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.alan.module.my.R
import com.alan.module.my.adapter.CardAdapter
import com.alan.module.my.databinding.LayoutDialogCardInputBinding
import com.alan.mvvm.base.ktx.clickDelay
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
    lateinit var mAdapter: CardAdapter

    companion object {
        fun newInstance(): CardInputFragmentDialog {
            val args = Bundle()
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
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        params.gravity = Gravity.BOTTOM
        setCanceledOnTouchOutside(false)
        isCancelable = false
        window.attributes = params
    }

    override fun LayoutDialogCardInputBinding.initView() {
        ivClose.clickDelay { dismiss() }
        tvCommit.clickDelay { dismiss() }

    }


    override fun initObserve() {

    }

    override fun initRequestData() {
    }


}