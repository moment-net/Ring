package com.alan.module.home.dialog

import android.animation.Animator
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.alan.module.main.R
import com.alan.module.main.databinding.LayoutMatchingBinding
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.base.ktx.dp2px
import com.alan.mvvm.base.mvvm.v.BaseFrameDialogFragment
import com.alan.mvvm.base.mvvm.vm.EmptyViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MatchingFragmentDialog : BaseFrameDialogFragment<LayoutMatchingBinding, EmptyViewModel>() {


    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<EmptyViewModel>()
    lateinit var userId: String

    companion object {
        fun newInstance(userId: String): MatchingFragmentDialog {
            val bundle = Bundle().apply {
                putString("userId", userId)
            }
            val dialog = MatchingFragmentDialog()
            dialog.setArguments(bundle)
            return dialog
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
        params.gravity = Gravity.TOP
        setCanceledOnTouchOutside(false)
        isCancelable = false
        window.attributes = params
    }

    override fun LayoutMatchingBinding.initView() {
        userId = arguments?.getString("userId", "")!!



        clBg.clickDelay {

        }

        clFinish.setOnFinishListener {
            dismiss()
        }

        mBinding.lvMatch.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationRepeat(animation: Animator?) {
            }
        })
        mBinding.lvMatch.addAnimatorUpdateListener {
            if (mBinding.lvMatch.progress >= 0.5) {

            }
        }

    }


    override fun initObserve() {

    }

    override fun initRequestData() {
        mBinding.lvMatch.playAnimation()
    }

}