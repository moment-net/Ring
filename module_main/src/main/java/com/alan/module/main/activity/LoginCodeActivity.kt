package com.alan.module.main.activity

import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextUtils
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.alan.module.main.R
import com.alan.module.main.databinding.ActivityLoginCodeBinding
import com.alan.module.main.viewmodel.LoginCodeViewModel
import com.alan.mvvm.base.http.responsebean.LoginBean
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.base.utils.ActivityStackManager
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch


/**
 * 作者：alan
 * 时间：2021/7/30
 * 备注：
 */
@Route(path = RouteUrl.MainModule.ACTIVITY_MAIN_CODE)
@AndroidEntryPoint
class LoginCodeActivity : BaseActivity<ActivityLoginCodeBinding, LoginCodeViewModel>() {
    @JvmField
    @Autowired
    var phone: String? = null

    //1是手机号登录流程，2是微信登录流程
    @JvmField
    @Autowired(name = "type")
    var type = 1

    var loginBean: LoginBean? = null

    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<LoginCodeViewModel>()

    /**
     * 初始化View
     */
    override fun ActivityLoginCodeBinding.initView() {
        ivBack.clickDelay { finish() }
        tvSend.clickDelay {
            mViewModel.requestCode(phone ?: "")
            DataPointUtil.reportReGetCode()
        }
        tvCommit.clickDelay {
            if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(etCode.text)) {
                when (type) {
                    1 -> {
                        showDialog()
                        mViewModel.requestLogin(phone ?: "", etCode.text.toString(), "", 0)
                    }
                    2 -> {
                        showDialog()
                        mViewModel.requestBindPhone(phone ?: "", etCode.text.toString(), 0)
                    }
                }
            }
        }

        //输入框监听
        etCode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.length == 6) {
                    tvCommit.isEnabled = true
                    tvCommit.performClick()
                } else {
                    tvCommit.isEnabled = false
                }
            }
        })

        startCountDown()
    }

    /**
     * 订阅数据
     */
    override fun initObserve() {
        mViewModel.ldCode.observe(this) {
            startCountDown()
        }

        mViewModel.ldLogin.observe(this) {
            when (it) {
                is LoginBean -> {
                    loginBean = it
                    mViewModel.loginIM(it.user!!)
                }
            }
        }

        mViewModel.ldIM.observe(this) {
            if (it.errorCode == 0) {
                handleIMLogin()
            } else if (it.errorCode == 200) {
                handleIMLogin()
            } else {
                toast(it.errorMessage)
            }
            dismissDialog()
        }


        mViewModel.ldBind.observe(this) {
            mViewModel.requestDevicesRegister()
            //5跳转逻辑
            if (SpHelper.getNewUser()) {
                val bundle = Bundle().apply {
                    putInt("type", type)
                }
                jumpARoute(RouteUrl.MainModule.ACTIVITY_MAIN_APPEARANCE, bundle)
            } else {
                val model = SpHelper.getUserInfo()?.model
                if (model == null) {
                    val bundle = Bundle().apply {
                        putInt("type", 2)
                    }
                    jumpARoute(RouteUrl.MainModule.ACTIVITY_MAIN_CHANGEAPPEARANCE, bundle)
                } else {
                    jumpARoute(RouteUrl.MainModule.ACTIVITY_MAIN_MAIN)
                }
            }
            ActivityStackManager.finishActivity(LoginActivity::class.java)
            ActivityStackManager.finishActivity(LoginPhoneActivity::class.java)
            ActivityStackManager.finishActivity(LoginCodeActivity::class.java)
        }

        mViewModel.ldFailed.observe(this) {
            dismissDialog()
        }


    }

    /**
     * 获取数据
     */
    override fun initRequestData() {
    }

    /**
     * 处理IM登录成功后跳转
     */
    fun handleIMLogin() {
        if (loginBean == null) {
            return
        }
        //1用户注册成功后调用
//        OpenInstall.reportRegister()
        //2更新用户信息
        SpHelper.updateUserInfo(loginBean)
        //3上传JPUSH设备ID
        mViewModel.requestDevicesRegister()
        //4跳转首页时取消倒计时

        //5跳转逻辑
        if (loginBean?.newUser!!) {
            val bundle = Bundle().apply {
                putInt("type", type)
            }
            jumpARoute(RouteUrl.MainModule.ACTIVITY_MAIN_APPEARANCE, bundle)
            DataPointUtil.reportRegister(SpHelper.getUserInfo()?.userId!!, 1)
        } else {
            val model = SpHelper.getUserInfo()?.model
            if (model == null) {
                val bundle = Bundle().apply {
                    putInt("type", 2)
                }
                jumpARoute(RouteUrl.MainModule.ACTIVITY_MAIN_CHANGEAPPEARANCE, bundle)
            } else {
                jumpARoute(RouteUrl.MainModule.ACTIVITY_MAIN_MAIN)
            }
        }
        ActivityStackManager.finishActivity(LoginActivity::class.java)
        ActivityStackManager.finishActivity(LoginPhoneActivity::class.java)
        ActivityStackManager.finishActivity(LoginCodeActivity::class.java)
    }

    /**
     * 开始倒计时
     */
    fun startCountDown() {
        var time = 0
        if (PhoneCountDownManager.instance!!.countDownTime.toInt() != 0) {
            time =
                (((System.currentTimeMillis() - PhoneCountDownManager.instance!!.countDownTime) / 1000).toInt())
        } else {
            PhoneCountDownManager.instance!!.countDownTime = System.currentTimeMillis()
        }
        mBinding.tvSend.setEnabled(false)
        mBinding.tvSend.setTextColor(ContextCompat.getColor(this, R.color._B9B9B9))
        lifecycleScope.launch {
            flow {
                for (i in time..60) {
                    delay(1000)
                    emit(i)
                }
            }.collect {
                if (it <= 59) {
                    mBinding.tvSend.setText(Html.fromHtml("<u>" + "重新发送 " + (60 - it) + "s" + "</u>"))
                } else {
                    PhoneCountDownManager.instance!!.countDownTime = 0
                    mBinding.tvSend.setEnabled(true)
                    mBinding.tvSend.setTextColor(
                        ContextCompat.getColor(
                            this@LoginCodeActivity,
                            R.color._D73E34
                        )
                    )
                    mBinding.tvSend.setText(Html.fromHtml("<u>" + "重新发送 " + "</u>"))
                }
            }
        }
    }
}