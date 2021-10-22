package com.alan.module.my.dialog

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.alan.module.my.R
import com.alan.module.my.databinding.LayoutDialogDeleteBinding
import com.alan.module.my.viewmodol.DeleteViewModel
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.base.ktx.dp2px
import com.alan.mvvm.base.ktx.getResColor
import com.alan.mvvm.base.mvvm.v.BaseFrameDialogFragment
import com.alan.mvvm.common.helper.SpHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DeleteFragmentDialog :
    BaseFrameDialogFragment<LayoutDialogDeleteBinding, DeleteViewModel>() {

    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<DeleteViewModel>()


    companion object {
        fun newInstance(): DeleteFragmentDialog {
            val args = Bundle()
            val fragment = DeleteFragmentDialog()
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
        params.width = dp2px(320f)
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        params.gravity = Gravity.CENTER
        setCanceledOnTouchOutside(true)
        isCancelable = true
        window.attributes = params
    }

    override fun LayoutDialogDeleteBinding.initView() {
        tvCancel.clickDelay { dismiss() }
        tvCommit.clickDelay {
            mViewModel.requestLogoff()
        }

        lifecycleScope.launch {
            flow {
                for (i in 1..5) {
                    delay(1000)
                    emit(i)
                }
            }.collect {
                val num = 5 - it
                if (num == 0) {
                    mBinding.tvCommit.setText("提交")
                    mBinding.tvCommit.isEnabled = true
                    mBinding.tvCommit.setShapeSolidColor(R.color._FFBF2F.getResColor())
                        .setUseShape()
                } else {
                    mBinding.tvCommit.setText("提交（${num}s）")
                    mBinding.tvCommit.isEnabled = false
                    mBinding.tvCommit.setShapeSolidColor(R.color._8D8C96.getResColor())
                        .setUseShape()
                }
            }
        }
    }


    override fun initObserve() {
        mViewModel.ldSuccess.observe(this) {
            when (it) {
                is Boolean -> {
                    dismiss()
                    SpHelper.clearUserInfo()
                    val dialog = DeleteSuccessFragmentDialog.newInstance()
                    dialog.show(requireActivity().supportFragmentManager)
                }
            }
        }
    }

    override fun initRequestData() {
    }


}