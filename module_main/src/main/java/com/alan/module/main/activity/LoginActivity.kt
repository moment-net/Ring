package com.alan.module.main.activity


import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import androidx.activity.viewModels
import com.alan.module.main.R
import com.alan.module.main.databinding.ActivityLoginBinding
import com.alan.module.main.viewmodel.LoginViewModel
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.base.utils.jumpARoute
import com.alan.mvvm.base.utils.toast
import com.alan.mvvm.common.constant.RouteUrl
import com.alan.mvvm.common.ui.BaseActivity
import com.alibaba.android.arouter.facade.annotation.Route
import dagger.hilt.android.AndroidEntryPoint

/**
 * 作者：alan
 * 时间：2021/7/30
 * 备注：登录页面
 */
@Route(path = RouteUrl.MainModule.ACTIVITY_MAIN_LOGIN)
@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>() {

    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<LoginViewModel>()

    /**
     * 初始化View
     */
    override fun ActivityLoginBinding.initView() {
        initAgreement()

        llPhone.clickDelay {
            if (!checkBox.isChecked()) {
                toast("请先勾选协议再登录")
                return@clickDelay
            }
            jumpARoute(RouteUrl.MainModule.ACTIVITY_MAIN_PHONE);
        }

        llWx.clickDelay {
            if (!checkBox.isChecked()) {
                toast("请先勾选协议再登录")
                return@clickDelay
            }
            jumpARoute(RouteUrl.MainModule.ACTIVITY_MAIN_MAIN);
        }
    }


    /**
     * 初始化协议
     */
    fun initAgreement() {
        val spanText = SpannableString(getString(R.string.string_agreement))
        spanText.setSpan(object : ClickableSpan() {
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                //设置下划线
                ds.isUnderlineText = false
            }

            override fun onClick(view: View) {
//                val intent = Intent(
//                    Intent.ACTION_VIEW,
//                    Uri.parse(
//                        "scheme://speak:8888/webActivity?webUrl=" + ApiConstant.getHostUrl(
//                            Host.APP
//                        ).toString() + "page/user-agreement&title=用户协议"
//                    )
//                )
//                intent.putExtra(
//                    IConstantRoom.KEY_SPEAK_FROM_PAGE,
//                    IConstantRoom.MyConstant.MY_LOGIN_PHONE
//                )
//                startActivity(intent)
            }
        }, spanText.length - 11, spanText.length - 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spanText.setSpan(object : ClickableSpan() {
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                //设置下划线
                ds.isUnderlineText = false
            }

            override fun onClick(view: View) {
//                val intent = Intent(
//                    Intent.ACTION_VIEW,
//                    Uri.parse(
//                        "scheme://speak:8888/webActivity?webUrl=" + ApiConstant.getHostUrl(
//                            Host.APP
//                        ).toString() + "page/privacy-policy&title=隐私政策"
//                    )
//                )
//                intent.putExtra(
//                    IConstantRoom.KEY_SPEAK_FROM_PAGE,
//                    IConstantRoom.MyConstant.MY_LOGIN_PHONE
//                )
//                startActivity(intent)
            }
        }, spanText.length - 4, spanText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        mBinding.tvAgreement.setHighlightColor(Color.TRANSPARENT) //设置点击后的颜色为透明，否则会一直出现高亮
        mBinding.tvAgreement.setText(spanText)
        mBinding.tvAgreement.setMovementMethod(LinkMovementMethod.getInstance()) //开始响应点击事件
    }

    /**
     * 订阅数据
     */
    override fun initObserve() {

    }

    /**
     * 获取数据
     */
    override fun initRequestData() {

    }
}