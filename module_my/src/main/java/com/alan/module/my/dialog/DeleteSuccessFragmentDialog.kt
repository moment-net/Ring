package com.alan.module.my.dialog

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.alan.module.my.R
import com.alan.module.my.databinding.LayoutDialogDeleteSuccessBinding
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.base.ktx.dp2px
import com.alan.mvvm.base.mvvm.v.BaseFrameDialogFragment
import com.alan.mvvm.base.mvvm.vm.EmptyViewModel
import com.alan.mvvm.base.utils.ActivityStackManager
import com.alan.mvvm.base.utils.jumpARoute
import com.alan.mvvm.common.constant.RouteUrl
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeleteSuccessFragmentDialog :
    BaseFrameDialogFragment<LayoutDialogDeleteSuccessBinding, EmptyViewModel>() {

    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<EmptyViewModel>()


    companion object {
        fun newInstance(): DeleteSuccessFragmentDialog {
            val args = Bundle()
            val fragment = DeleteSuccessFragmentDialog()
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
        setCanceledOnTouchOutside(false)
        isCancelable = false
        window.attributes = params
    }

    override fun LayoutDialogDeleteSuccessBinding.initView() {
        tvCommit.clickDelay {
            dismiss()
            ActivityStackManager.finishAllActivity()
            jumpARoute(RouteUrl.MainModule.ACTIVITY_MAIN_LOGIN)
        }
    }


    override fun initObserve() {

    }

    override fun initRequestData() {
    }


}