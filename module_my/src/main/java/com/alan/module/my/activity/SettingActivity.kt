package com.alan.module.my.activity

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import androidx.activity.viewModels
import com.alan.module.my.databinding.ActivitySettingBinding
import com.alan.module.my.viewmodol.SettingViewModel
import com.alan.mvvm.base.http.apiservice.HttpBaseUrlConstant
import com.alan.mvvm.base.http.requestbean.LoginThirdRequestBean
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.base.utils.*
import com.alan.mvvm.common.constant.Constants
import com.alan.mvvm.common.constant.RouteUrl
import com.alan.mvvm.common.dialog.DialogHelper
import com.alan.mvvm.common.event.WXCodeEvent
import com.alan.mvvm.common.helper.SpHelper
import com.alan.mvvm.common.ui.BaseActivity
import com.alibaba.android.arouter.facade.annotation.Route
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
 * 备注：
 */
@EventBusRegister
@Route(path = RouteUrl.MyModule.ACTIVITY_MY_SET)
@AndroidEntryPoint
class SettingActivity : BaseActivity<ActivitySettingBinding, SettingViewModel>() {
    // IWXAPI 是第三方app和微信通信的openApi接口
    var api: IWXAPI? = null
    var handler: MyHandler? = null
    var openId: String? = null
    var accessToken: String? = null
    var refreshToken: String? = null
    var scope: String? = null
    var unionid: String? = null


    inner class MyHandler(activity: Activity) : Handler() {
        private val wxEntryActivityWeakReference: WeakReference<Activity>
        override fun handleMessage(msg: Message) {
            val tag = msg.what
            val data = msg.data
            var json: JSONObject? = null
            val activity = wxEntryActivityWeakReference.get() as SettingActivity?
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
                        activity?.requestBindWX(
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
    override val mViewModel by viewModels<SettingViewModel>()

    /**
     * 初始化View
     */
    override fun ActivitySettingBinding.initView() {
        regToWx()

        handler = MyHandler(this@SettingActivity)


        ivBack.clickDelay { finish() }
        tvBinding.clickDelay {
            tvBinding.isEnabled = false
            requestWX()
        }
        tvSafe.clickDelay {
            jumpARoute(RouteUrl.MyModule.ACTIVITY_MY_SAFE)
        }
        tvAgreement.clickDelay {
            val bundle = Bundle().apply {
                putString("webUrl", HttpBaseUrlConstant.BASE_URL + "page/user-agreement")
                putString("webTitle", "用户协议")
            }
            jumpARoute(RouteUrl.WebModule.ACTIVITY_WEB_WEB, bundle)
        }
        tvPrivacy.clickDelay {
            val bundle = Bundle().apply {
                putString("webUrl", HttpBaseUrlConstant.BASE_URL + "page/privacy-policy")
                putString("webTitle", "隐私政策")
            }
            jumpARoute(RouteUrl.WebModule.ACTIVITY_WEB_WEB, bundle)
        }
        tvUpdate.clickDelay { }
        tvFeedback.clickDelay { }
        tvLogout.clickDelay { logout() }



        tvAppVersion.text = "v${DeviceUtil.getApkVersionName(this@SettingActivity)}"

        if (SpHelper.getUserInfo()?.bindWeChat!!) {
            tvBind.setText("已绑定")
            tvBinding.setEnabled(false)
        } else {
            tvBind.setText("绑定")
            tvBinding.setEnabled(true)
        }
    }

    /**
     * 订阅数据
     */
    override fun initObserve() {
        mViewModel.ldSuccess.observe(this) {
            dismissDialog()
            mBinding.tvBinding.isEnabled = true
            if (it) {
                toast("微信绑定成功")
                mBinding.tvBind.setText("已绑定")
                mBinding.tvBinding.setEnabled(false)
            }
        }
    }

    /**
     * 获取数据
     */
    override fun initRequestData() {

    }


    fun requestBindWX(
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
        mViewModel.requestBindWX(requestBean)
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


    fun logout() {
        DialogHelper.showMultipleDialog(this, "确定退出登录吗？", "确定", "取消", {
            SpHelper.clearUserInfo()
            ActivityStackManager.finishAllActivity()
            jumpARoute(RouteUrl.MainModule.ACTIVITY_MAIN_LOGIN)
        }, {

        })
    }
}