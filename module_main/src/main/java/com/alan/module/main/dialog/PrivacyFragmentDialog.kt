package com.alan.module.main.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.alan.module.main.R
import com.alan.module.main.databinding.LayoutPrivacyBinding
import com.alan.mvvm.base.http.apiservice.HttpBaseUrlConstant
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.base.ktx.dp2px
import com.alan.mvvm.base.ktx.getResColor
import com.alan.mvvm.base.mvvm.v.BaseFrameDialogFragment
import com.alan.mvvm.base.mvvm.vm.EmptyViewModel
import com.alan.mvvm.base.utils.ActivityStackManager
import com.alan.mvvm.base.utils.jumpARoute
import com.alan.mvvm.common.constant.RouteUrl
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PrivacyFragmentDialog : BaseFrameDialogFragment<LayoutPrivacyBinding, EmptyViewModel>() {

    lateinit var listener: OnClickAgreeListener

    companion object {
        fun newInstance(): PrivacyFragmentDialog {
            val bundle = Bundle()
            val dialog = PrivacyFragmentDialog()
            dialog.setArguments(bundle)
            return dialog
        }
    }

    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<EmptyViewModel>()


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


    override fun LayoutPrivacyBinding.initView() {
        tvAgree.clickDelay {
            dismiss()
            if (listener != null) {
                listener.onClick()
            }
        }
        tvCancel.clickDelay {
            ActivityStackManager.exitApp()
        }

        val spanText =
            SpannableString("1、我们会遵循隐私政策收集、使用信息，但不会强制捆绑；\n2、在仅浏览时，为保证帐号登陆安全及信息安全，我们会收集设备信息与日志信息；\n3、地理位置信息、摄像头、麦克风、相册杈限均需要经过授权才会为实现功能或服务时使用。您可以查看完整版《用戶隐私政策》")
        spanText.setSpan(object : ClickableSpan() {
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = R.color._08DA89.getResColor()
            }

            override fun onClick(view: View) {
                val bundle = Bundle().apply {
                    putString("webUrl", HttpBaseUrlConstant.BASE_URL + "page/privacy-policy")
                    putString("webTitle", "隐私政策")
                }
                jumpARoute(RouteUrl.WebModule.ACTIVITY_WEB_WEB, bundle)
            }
        }, spanText.length - 8, spanText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        mBinding.tvContent.setHighlightColor(Color.TRANSPARENT) //设置点击后的颜色为透明，否则会一直出现高亮
        mBinding.tvContent.setText(spanText)
        mBinding.tvContent.setMovementMethod(LinkMovementMethod.getInstance()) //开始响应点击事件
    }


    override fun initObserve() {
    }

    override fun initRequestData() {

    }

    interface OnClickAgreeListener {
        fun onClick()
    }
}