package com.alan.module.main.activity

import android.graphics.Color
import android.os.Bundle
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import androidx.activity.viewModels
import com.alan.module.main.R
import com.alan.module.main.databinding.ActivityLoginPhoneBinding
import com.alan.module.main.viewmodel.LoginPhoneViewModel
import com.alan.mvvm.base.http.apiservice.HttpBaseUrlConstant
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.base.utils.PhoneCountDownManager
import com.alan.mvvm.base.utils.jumpARoute
import com.alan.mvvm.base.utils.toast
import com.alan.mvvm.common.constant.RouteUrl
import com.alan.mvvm.common.helper.SpHelper
import com.alan.mvvm.common.report.DataPointUtil
import com.alan.mvvm.common.ui.BaseActivity
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import dagger.hilt.android.AndroidEntryPoint


/**
 * 作者：alan
 * 时间：2021/7/30
 * 备注：
 */
@Route(path = RouteUrl.MainModule.ACTIVITY_MAIN_PHONE)
@AndroidEntryPoint
class LoginPhoneActivity : BaseActivity<ActivityLoginPhoneBinding, LoginPhoneViewModel>() {
    //上一次的手机号
    var oldPhone: String? = null
    //1是手机号登录流程，2是微信登录流程绑定手机号

    //1是手机号登录流程，2是微信登录流程
    @JvmField
    @Autowired(name = "type")
    var type = 1

    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<LoginPhoneViewModel>()

    /**
     * 初始化View
     */
    override fun ActivityLoginPhoneBinding.initView() {
        ivBack.clickDelay {
            onBackPressed()
        }
        ivClear.clickDelay { etPhone.setText("") }
        tvCommit.clickDelay {
            requestCode()
            DataPointUtil.reportGetCode()
        }

        if (type == 1) {
            tvPhoneTitle.setText(getString(R.string.string_phone_login))
            val spanText = SpannableString(getString(R.string.string_agreement))
            spanText.setSpan(object : ClickableSpan() {
                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    //设置下划线
                    ds.isUnderlineText = false
                }

                override fun onClick(view: View) {
                    val bundle = Bundle().apply {
                        putString("webUrl", HttpBaseUrlConstant.BASE_URL + "page/user-agreement")
                        putString("webTitle", "用户协议")
                    }
                    jumpARoute(RouteUrl.WebModule.ACTIVITY_WEB_WEB, bundle)
                }
            }, spanText.length - 11, spanText.length - 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            spanText.setSpan(object : ClickableSpan() {
                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    //设置下划线
                    ds.isUnderlineText = false
                }

                override fun onClick(view: View) {
                    val bundle = Bundle().apply {
                        putString("webUrl", HttpBaseUrlConstant.BASE_URL + "page/privacy-policy")
                        putString("webTitle", "隐私政策")
                    }
                    jumpARoute(RouteUrl.WebModule.ACTIVITY_WEB_WEB, bundle)
                }
            }, spanText.length - 4, spanText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            tvAgreement.setHighlightColor(Color.TRANSPARENT) //设置点击后的颜色为透明，否则会一直出现高亮
            tvAgreement.setText(spanText)
            tvAgreement.setMovementMethod(LinkMovementMethod.getInstance()) //开始响应点击事件
            tvAgreement.visibility = View.INVISIBLE
        } else {
            tvPhoneTitle.setText(getString(R.string.string_phone_bind))
            tvAgreement.setText(getString(R.string.string_phone_bing_info))
        }

        etPhone.setFocusable(true)
        etPhone.setFocusableInTouchMode(true)
        etPhone.requestFocus()

        //输入框监听
        etPhone.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.length > 0) {
                    ivClear.setVisibility(View.VISIBLE)
                } else {
                    ivClear.setVisibility(View.GONE)
                }
                if (s.length == 11) {
                    tvCommit.setEnabled(true)
                } else {
                    tvCommit.setEnabled(false)
                }
            }
        })
    }

    /**
     * 订阅数据
     */
    override fun initObserve() {
        mViewModel.ldSuccess.observe(this) {
            if (it) {
                toast(resources.getString(R.string.string_message_send) + oldPhone)
                //如果验证码已经发送，且在60s倒计时，则直接跳转
                val bundle = Bundle().apply {
                    putString("phone", oldPhone)
                    putInt("type", type)
                }
                jumpARoute(RouteUrl.MainModule.ACTIVITY_MAIN_CODE, bundle)
            }
            mBinding.tvCommit.setEnabled(true)
        }
    }

    /**
     * 获取数据
     */
    override fun initRequestData() {

    }


    /**
     * 请求验证码
     */
    fun requestCode() {
        mBinding.tvCommit.setEnabled(false)
        val phone: String = mBinding.etPhone.getText().toString()
        if (!TextUtils.isEmpty(phone) && phone.length == 11) {
            if (!TextUtils.equals(phone, oldPhone)) {
                PhoneCountDownManager.instance!!.countDownTime = 0
            }
            if (PhoneCountDownManager.instance!!.isNeedRegetCode) {
                PhoneCountDownManager.instance!!.countDownTime = 0
                oldPhone = phone
                mViewModel.requestCode(phone)
            } else {
                //如果验证码已经发送，且在60s倒计时，则直接跳转
                val bundle = Bundle().apply {
                    putString("phone", phone)
                    putInt("type", type)
                }
                jumpARoute(RouteUrl.MainModule.ACTIVITY_MAIN_CODE, bundle)
                mBinding.tvCommit.setEnabled(true)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (type == 1) {
            DataPointUtil.reportLoginPhoneBack()
        } else {
            DataPointUtil.reportBindBack(SpHelper.getUserInfo()?.userId!!)
            SpHelper.clearUserInfo()
        }
    }
}
