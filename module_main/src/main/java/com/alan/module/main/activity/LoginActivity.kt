package com.alan.module.main.activity


import android.app.Activity
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.Gravity
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.alan.module.main.R
import com.alan.module.main.databinding.ActivityLoginBinding
import com.alan.module.main.viewmodel.LoginViewModel
import com.alan.mvvm.base.http.apiservice.HttpBaseUrlConstant
import com.alan.mvvm.base.http.requestbean.LoginThirdRequestBean
import com.alan.mvvm.base.http.responsebean.LoginBean
import com.alan.mvvm.base.http.responsebean.PhoneBean
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.base.utils.EventBusRegister
import com.alan.mvvm.base.utils.NetworkUtil
import com.alan.mvvm.base.utils.jumpARoute
import com.alan.mvvm.base.utils.toast
import com.alan.mvvm.common.constant.Constants
import com.alan.mvvm.common.constant.RouteUrl
import com.alan.mvvm.common.event.WXCodeEvent
import com.alan.mvvm.common.helper.SpHelper
import com.alan.mvvm.common.report.DataPointUtil
import com.alan.mvvm.common.ui.BaseActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.geetest.onelogin.OneLoginHelper
import com.geetest.onelogin.config.OneLoginThemeConfig
import com.geetest.onelogin.listener.AbstractOneLoginListener
import com.socks.library.KLog
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.lang.ref.WeakReference
import java.nio.charset.Charset

/**
 * 作者：alan
 * 时间：2021/7/30
 * 备注：登录页面
 */
@EventBusRegister
@Route(path = RouteUrl.MainModule.ACTIVITY_MAIN_LOGIN)
@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>() {
    // IWXAPI 是第三方app和微信通信的openApi接口
    var api: IWXAPI? = null
    var handler: MyHandler? = null
    var openId: String? = null
    var accessToken: String? = null
    var refreshToken: String? = null
    var scope: String? = null
    var unionid: String? = null
    var loginBean: LoginBean? = null
    var loginType = 0 //type=1是手机号登录流程，2是微信登录流程


    inner class MyHandler(activity: Activity) : Handler() {
        private val wxEntryActivityWeakReference: WeakReference<Activity>
        override fun handleMessage(msg: Message) {
            val tag = msg.what
            val data = msg.data
            var json: JSONObject? = null
            val activity = wxEntryActivityWeakReference.get() as LoginActivity?
            when (tag) {
                NetworkUtil.GET_TOKEN -> {
                    try {
                        json = JSONObject(data.getString("result"))
                        openId = json.getString("openid")
                        accessToken = json.getString("access_token")
                        refreshToken = json.getString("refresh_token")
                        scope = json.getString("scope")
                        unionid = json.getString("unionid")
                        if (accessToken != null && openId != null) {
                            NetworkUtil.sendWxAPI(
                                activity!!.handler, String.format(
                                    "https://api.weixin.qq.com/sns/auth?" +
                                            "access_token=%s&openid=%s",
                                    accessToken,
                                    openId
                                ), NetworkUtil.CHECK_TOKEN
                            )
                        } else {
                            toast("请先获取code")
                        }
                    } catch (e: JSONException) {
                        Log.e("xujm", e.message!!)
                    }
                }
                NetworkUtil.CHECK_TOKEN -> {
                    try {
                        json = JSONObject(data.getString("result"))
                        val errcode = json.getInt("errcode")
                        if (errcode == 0) {
                            NetworkUtil.sendWxAPI(
                                activity!!.handler, String.format(
                                    "https://api.weixin.qq.com/sns/userinfo?" +
                                            "access_token=%s&openid=%s",
                                    accessToken,
                                    openId
                                ), NetworkUtil.GET_INFO
                            )
                        } else {
                            NetworkUtil.sendWxAPI(
                                activity!!.handler, java.lang.String.format(
                                    "https://api.weixin.qq.com/sns/oauth2/refresh_token?" +
                                            "appid=%s&grant_type=refresh_token&refresh_token=%s",
                                    Constants.APP_ID,
                                    refreshToken
                                ),
                                NetworkUtil.REFRESH_TOKEN
                            )
                        }
                    } catch (e: JSONException) {
                        Log.e("xujm", e.message!!)
                    }
                }
                NetworkUtil.REFRESH_TOKEN -> {
                    try {
                        json = JSONObject(data.getString("result"))
                        openId = json.getString("openid")
                        accessToken = json.getString("access_token")
                        refreshToken = json.getString("refresh_token")
                        scope = json.getString("scope")
                        NetworkUtil.sendWxAPI(
                            activity!!.handler, String.format(
                                "https://api.weixin.qq.com/sns/userinfo?" +
                                        "access_token=%s&openid=%s",
                                accessToken,
                                openId
                            ), NetworkUtil.GET_INFO
                        )
                    } catch (e: JSONException) {
                        Log.e("xujm", e.message!!)
                    }
                }
                NetworkUtil.GET_INFO -> {
                    try {
                        json = JSONObject(data.getString("result"))
                        Log.e("xujm", "返回的个人信息$json")
                        val headimgurl = json.getString("headimgurl")
                        val encode: String = getcode(json.getString("nickname"))!!
                        val nickname = String(
                            json.getString("nickname").toByteArray(charset(encode)),
                            Charset.forName("utf-8")
                        )
                        val sex = json.getString("sex")
                        val province = json.getString("province")
                        val city = json.getString("city")
                        val country = json.getString("country")
                        activity?.requestLoginWX(
                            openId,
                            unionid,
                            accessToken,
                            nickname,
                            sex,
                            headimgurl
                        )
                    } catch (e: JSONException) {
                        Log.e("xujm", e.message!!)
                    } catch (e: UnsupportedEncodingException) {
                        Log.e("xujm", e.message!!)
                    }
                }
            }
        }

        init {
            wxEntryActivityWeakReference = WeakReference(activity)
        }
    }

    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<LoginViewModel>()

    /**
     * 初始化View
     */
    override fun ActivityLoginBinding.initView() {
        regToWx()

        handler = MyHandler(this@LoginActivity)

        initAgreement()

        llPhone.clickDelay {
            if (!checkBox.isChecked()) {
                toast("请先勾选协议再登录")
                return@clickDelay
            }
            loginType = 1
            loginPhone()
        }

        llWx.clickDelay {
            if (!checkBox.isChecked()) {
                toast("请先勾选协议再登录")
                return@clickDelay
            }
            loginType = 2
            requestWX()
            DataPointUtil.reportLogin(2)
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
        mBinding.tvAgreement.setHighlightColor(Color.TRANSPARENT) //设置点击后的颜色为透明，否则会一直出现高亮
        mBinding.tvAgreement.setText(spanText)
        mBinding.tvAgreement.setMovementMethod(LinkMovementMethod.getInstance()) //开始响应点击事件
    }

    /**
     * 订阅数据
     */
    override fun initObserve() {
        mViewModel.ldSuccess.observe(this) {
            when (it) {
                is PhoneBean -> {
                    requestLogin(it.phone)
                }
                is LoginBean -> {
                    loginBean = it
                    mViewModel.loginIM(it.user!!)
                }
            }
        }
        mViewModel.ldFailed.observe(this) {
            when (it) {
                1 -> {
                    //手机号登录
                    val bundle = Bundle().apply {
                        putInt("type", 1)
                    }
                    jumpARoute(RouteUrl.MainModule.ACTIVITY_MAIN_PHONE, bundle)
                    OneLoginHelper.with().dismissAuthActivity()
                    DataPointUtil.reportLogin(1)
                }
                2 -> {
                    dismissDialog()
                }
                3 -> {
                    dismissDialog()
                }
            }
        }
        mViewModel.ldIM.observe(this) {
            dismissDialog()
            if (it.errorCode == 0) {
                handleIMLogin()
            } else {
                toast(it.errorMessage)
            }
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
        if (loginType == 1) {
            if (loginBean == null) {
                return
            }
            //1用户注册成功后调用
//            OpenInstall.reportRegister()
            //2更新用户信息
            SpHelper.updateUserInfo(loginBean)
            //3上传JPUSH设备ID
            mViewModel.requestDevicesRegister()
            //4跳转逻辑
            if (loginBean?.newUser!!) {
                val bundle = Bundle().apply {
                    putInt("type", 1)
                }
                jumpARoute(RouteUrl.MainModule.ACTIVITY_MAIN_WXINFO, bundle)
            } else {
                jumpARoute(RouteUrl.MainModule.ACTIVITY_MAIN_MAIN)
            }
            finish()
        } else if (loginType == 2) {
            if (loginBean == null) {
                return
            }
            //1用户注册成功后调用
//            OpenInstall.reportRegister()
            //2更新用户信息
            SpHelper.updateUserInfo(loginBean)
            //3上传JPUSH设备ID
            mViewModel.requestDevicesRegister()
            //4跳转逻辑
            if (loginBean!!.user!!.bindPhone!!) {
                finish()
                jumpARoute(RouteUrl.MainModule.ACTIVITY_MAIN_MAIN)
            } else {
                //绑定微信
                val bundle = Bundle().apply {
                    putInt("type", 2)
                }
                jumpARoute(RouteUrl.MainModule.ACTIVITY_MAIN_PHONE, bundle)
            }
        }
    }

    /**
     * 手机号一键登录
     */
    fun loginPhone() {
        OneLoginHelper.with().setRequestedOrientation(this, true)
        val config =
            OneLoginThemeConfig.Builder() //                .setAuthNavTextView("一键登录", ContextCompat.getColor(mContext, R.color._3A3A3A), 16, false, "隐私条例", ContextCompat.getColor(mContext, R.color._3A3A3A), 14)
                //返回按钮
                .setAuthNavReturnImgView("icon_onelogin_back", 12, 20, false, 20, 8) //中间logo
                .setLogoImgView("icon_onelogin", 207, 160, false, 80, 0, 0) //电话号码
                .setNumberView(ContextCompat.getColor(this, R.color._3A3A3A), 24, 220, 0, 0)
                .setNumberViewTypeface(Typeface.DEFAULT_BOLD) //点击button按钮
                .setLogBtnLayout("icon_onelogin_bt", 300, 48, 312, 0, 0)
                .setLogBtnTextView(
                    "本机手机号一键登录",
                    ContextCompat.getColor(this, R.color.white),
                    16
                )
                .setLogBtnDisableIfUnChecked(true) //slogan颜色
                .setSloganView(
                    ContextCompat.getColor(this, R.color._6F6E6E),
                    12,
                    280,
                    0,
                    0
                ) //其他按钮
                .setSwitchView(
                    "其他手机号登录",
                    ContextCompat.getColor(this, R.color._FFA545),
                    14,
                    false,
                    380,
                    0,
                    0
                )
                .setSwitchViewTypeface(Typeface.DEFAULT_BOLD)
                .setSwitchViewLayout(null, 160, 25)
                /**
                 * 连续四个参数为一组隐私协议条款 第一个为条款说明前缀或者分隔字符，第二个为条款名称，第三个为条款 URL，第四个为说明后缀或者分隔字符
                 * 任意一组留空，SDK 会自动添加运营商隐私条款，留空的位置可前后调整。
                 */
                .setPrivacyCheckBox("gt_one_login_unchecked", "gt_one_login_checked", false, 9, 9)
                .setPrivacyClauseTextStrings(
                    "未注册的手机号验证通过后将自动注册登录即同意",
                    "用户协议",
                    HttpBaseUrlConstant.BASE_URL + "page/user-agreement",
                    "",
                    "和",
                    "隐私政策",
                    HttpBaseUrlConstant.BASE_URL + "page/privacy-policy",
                    "",
                    "和",
                    "",
                    "",
                    "并使用本机号码登录"
                )
                .setPrivacyClauseView(
                    ContextCompat.getColor(this, R.color._6F6E6E),
                    ContextCompat.getColor(this, R.color._3A3A3A),
                    11
                )
                .setPrivacyTextGravity(Gravity.TOP)
                .setPrivacyClauseViewTypeface(Typeface.DEFAULT_BOLD, Typeface.DEFAULT_BOLD)
                .setPrivacyUnCheckedToastText(true, "请同意服务条款")
                .build()
        showDialog()
        OneLoginHelper
            .with()
            .requestToken(config, object : AbstractOneLoginListener() {
                override fun onResult(jsonObject: JSONObject) {
                    dismissDialog()
                    KLog.e("xujm", "loginPhone:$jsonObject")
                    try {
                        val statusResult = jsonObject.getInt("status")
                        // status=200 为取号成功，其他返回码请参考返回码章节
                        if (statusResult != 200) {
                            val errorCode = jsonObject.getString("errorCode")
                            if (TextUtils.equals(errorCode, "-20301") || TextUtils.equals(
                                    errorCode,
                                    "-20302"
                                )
                            ) {
                                //-20302点击返回按钮
                                DataPointUtil.reportAutoPhoneBack()
                            } else {
                                //用户切换登录方式:-20303;用户手机没有网络:-20200;当前手机没有手机号码:-20201;
                                val bundle = Bundle().apply {
                                    putInt("type", 1)
                                }
                                //手机号登陆
                                jumpARoute(RouteUrl.MainModule.ACTIVITY_MAIN_PHONE, bundle)
                                OneLoginHelper.with().dismissAuthActivity()
                                DataPointUtil.reportLogin(1)
                            }
                        } else {
                            //以下为获取成功
                            try {
                                val processId = jsonObject.getString("process_id")
                                val token = jsonObject.getString("token")
                                val authcode = jsonObject.optString("authcode")
                                mViewModel.requestCheckPhone(processId, token, authcode)
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }
                            OneLoginHelper.with().dismissAuthActivity()
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }

                override fun onAuthActivityCreate(activity: Activity) {
                    super.onAuthActivityCreate(activity)
                    dismissDialog()
                    toast("请同意服务条款")
                    DataPointUtil.reportLogin(3)
                }
            })
    }


    /**
     * 登录1：手机号一键登录
     *
     * @param phone
     */
    fun requestLogin(phone: String) {
        showDialog()
        mViewModel.requestLogin(phone, "")
    }

    /**
     * 登录2：三方登录接口
     *
     * @param openId
     * @param unionId
     * @param oauthToken
     * @param name
     * @param sex
     * @param imgUrl
     */
    fun requestLoginWX(
        openId: String?,
        unionId: String?,
        oauthToken: String?,
        name: String?,
        sex: String?,
        imgUrl: String?
    ) {
        showDialog()

        val requestBean = LoginThirdRequestBean(
            openId ?: "",
            3,
            unionId ?: "",
            oauthToken ?: "",
            name ?: "",
            (sex ?: "0").toInt(),
            imgUrl ?: "",
            "",
            null
        )
        mViewModel.requestLoginWX(requestBean)
    }


    //注册微信
    private fun regToWx() {
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID, true)
        // 将应用的appId注册到微信
        api!!.registerApp(Constants.APP_ID)
    }

    /**
     * 请求获取微信Code
     */
    fun requestWX() {
        if (!api!!.isWXAppInstalled) {
            toast("您还未安装微信客户端")
            return
        }
        val req = SendAuth.Req()
        req.scope = Constants.APP_SNSAPI
        req.state = "none"
        api!!.sendReq(req)
    }


    //获取微信Code
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun getWXCode(event: WXCodeEvent) {
        NetworkUtil.sendWxAPI(
            handler, java.lang.String.format(
                "https://api.weixin.qq.com/sns/oauth2/access_token?" +
                        "appid=%s&secret=%s&code=%s&grant_type=authorization_code",
                Constants.APP_ID,
                Constants.APP_SECRET,
                event.code
            ), NetworkUtil.GET_TOKEN
        )
    }


    /**
     * 获取下编码格式
     *
     * @param str
     * @return
     */
    fun getcode(str: String): String? {
        val encodelist = arrayOf(
            "GB2312",
            "ISO-8859-1",
            "UTF-8",
            "GBK",
            "Big5",
            "UTF-16LE",
            "Shift_JIS",
            "EUC-JP"
        )
        for (i in encodelist.indices) {
            try {
                if (str == String(
                        str.toByteArray(Charset.forName(encodelist[i])),
                        Charset.forName(encodelist[i])
                    )
                ) {
                    return encodelist[i]
                }
            } catch (e: Exception) {
            }
        }
        return ""
    }
}