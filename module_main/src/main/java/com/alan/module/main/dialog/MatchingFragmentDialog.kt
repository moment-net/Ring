package com.alan.module.home.dialog

import android.animation.Animator
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.alan.module.main.R
import com.alan.module.main.databinding.LayoutMatchingBinding
import com.alan.module.main.viewmodel.MatchingViewModel
import com.alan.mvvm.base.http.responsebean.UserInfoBean
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.base.ktx.dp2px
import com.alan.mvvm.base.mvvm.v.BaseFrameDialogFragment
import com.alan.mvvm.base.utils.jumpARoute
import com.alan.mvvm.base.utils.toast
import com.alan.mvvm.common.constant.RouteUrl
import com.alan.mvvm.common.db.entity.UserEntity
import com.alan.mvvm.common.im.EMClientHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MatchingFragmentDialog : BaseFrameDialogFragment<LayoutMatchingBinding, MatchingViewModel>() {


    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<MatchingViewModel>()
    lateinit var tagName: String

    companion object {
        fun newInstance(tagName: String): MatchingFragmentDialog {
            val bundle = Bundle().apply {
                putString("tagName", tagName)
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
        arguments?.apply {
            tagName = getString("tagName", "")
        }


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
        mViewModel.ldData.observe(this) {
            when (it) {
                is UserInfoBean -> {
                    dismiss()
                    toast("匹配小伙伴成功")
                    val bundle = Bundle().apply {
                        putString("userId", it.userId)
                    }
                    EMClientHelper.saveUser(
                        UserEntity(
                            it.userId,
                            it.userName,
                            it.avatar
                        )
                    )
                    jumpARoute(RouteUrl.ChatModule.ACTIVITY_CHAT_DETAIL, bundle)
                }
                is Boolean -> {
                    dismiss()
                }
            }
        }
    }

    override fun initRequestData() {
        mBinding.lvMatch.playAnimation()
        lifecycleScope.launch {
            flow<Int> {
                delay(2000)
                emit(2)
            }.collect {
                if (it == 2) {
                    mViewModel.requestFastMatch(tagName)
                }
            }
        }
    }

}