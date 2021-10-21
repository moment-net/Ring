package com.alan.module.home.dialog

import android.animation.Animator
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.alan.module.main.R
import com.alan.module.main.databinding.LayoutMatchBinding
import com.alan.module.main.viewmodel.MatchDialogViewModel
import com.alan.mvvm.base.coil.CoilUtils
import com.alan.mvvm.base.http.responsebean.UserInfoBean
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.base.ktx.dp2px
import com.alan.mvvm.base.ktx.getResColor
import com.alan.mvvm.base.ktx.visible
import com.alan.mvvm.base.mvvm.v.BaseFrameDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MatchFragmentDialog : BaseFrameDialogFragment<LayoutMatchBinding, MatchDialogViewModel>() {


    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<MatchDialogViewModel>()
    lateinit var userId: String

    companion object {
        fun newInstance(userId: String): MatchFragmentDialog {
            val bundle = Bundle().apply {
                putString("userId", userId)
            }
            val dialog = MatchFragmentDialog()
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
        params.width = dp2px(335f)
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        params.gravity = Gravity.TOP
        setCanceledOnTouchOutside(false)
        isCancelable = false
        window.attributes = params
    }

    override fun LayoutMatchBinding.initView() {
        userId = arguments?.getString("userId", "")!!

        ivClose.clickDelay {
            dismiss()
        }

        tvChat.clickDelay {
            dismiss()
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
                groupAll.visible()
            }
        }

        mBinding.tvNum.setText("免费匹配次数：3")
    }


    override fun initObserve() {
        mViewModel.ldSuccess.observe(this) {
            when (it) {
                is UserInfoBean -> {
                    CoilUtils.loadCircleBlur(
                        mBinding.ivAvatar,
                        it?.avatar!!,
                        requireContext(),
                        25f,
                        1f
                    )
                    mBinding.tvDesc.setText(it.desc)

                    var address = ""
                    if (!TextUtils.isEmpty(it?.address) && it?.address!!.split("-").size == 3) {
                        address = it?.address!!.split("-")[1]
                    }
                    val age = if (it.age > 0) {
                        "${it.age}岁"
                    } else {
                        ""
                    }
                    if (TextUtils.isEmpty(age) && TextUtils.isEmpty(address)) {
                        mBinding.tvAge.setText("")
                    } else {
                        mBinding.tvAge.setText("$age  ${address}")
                    }
                    if (it.gender == 1) {
                        mBinding.tvAge.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.icon_home_boy,
                            0,
                            0,
                            0
                        )
                        mBinding.tvAge.setTextColor(R.color._515FFF.getResColor())
                        mBinding.tvAge.setShapeSolidColor(R.color._EAECFF.getResColor())
                    } else {
                        mBinding.tvAge.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.icon_home_girl,
                            0,
                            0,
                            0
                        )
                        mBinding.tvAge.setTextColor(R.color._FF517A.getResColor())
                        mBinding.tvAge.setShapeSolidColor(R.color._FFE6EC.getResColor())
                    }
                }
            }
        }
    }

    override fun initRequestData() {
        mBinding.lvMatch.playAnimation()
        mViewModel.requestUserInfo(userId)
    }

}