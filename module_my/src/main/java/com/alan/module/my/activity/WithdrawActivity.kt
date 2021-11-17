package com.alan.module.my.activity

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import androidx.activity.viewModels
import com.alan.module.my.databinding.ActivityWithdrawBinding
import com.alan.module.my.dialog.WxCommitFragmentDialog
import com.alan.module.my.viewmodol.WithdrawViewModel
import com.alan.mvvm.base.http.apiservice.HttpBaseUrlConstant
import com.alan.mvvm.base.http.requestbean.AccountBean
import com.alan.mvvm.base.http.responsebean.ApplyRequestBean
import com.alan.mvvm.base.http.responsebean.WXAccountBindBean
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.base.utils.EventBusRegister
import com.alan.mvvm.base.utils.NetworkUtil
import com.alan.mvvm.base.utils.jumpARoute
import com.alan.mvvm.base.utils.toast
import com.alan.mvvm.common.constant.Constants
import com.alan.mvvm.common.constant.RouteUrl
import com.alan.mvvm.common.event.WXCodeEvent
import com.alan.mvvm.common.ui.BaseActivity
import com.alibaba.android.arouter.facade.annotation.Autowired
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
@Route(path = RouteUrl.MyModule.ACTIVITY_MY_WITHDRAW)
@AndroidEntryPoint
class WithdrawActivity : BaseActivity<ActivityWithdrawBinding, WithdrawViewModel>() {

    @JvmField
    @Autowired(name = "bean")
    var accountBean: AccountBean? = null

    // IWXAPI 是第三方app和微信通信的openApi接口
    var api: IWXAPI? = null
    var handler: WithdrawActivity.MyHandler? = null
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
            val activity = wxEntryActivityWeakReference.get() as WithdrawActivity?
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
                        activity!!.requestAddWXAccount(nickname, openId!!)
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
    override val mViewModel by viewModels<WithdrawViewModel>()

    /**
     * 初始化View
     */
    override fun ActivityWithdrawBinding.initView() {
        ivBack.clickDelay { finish() }
        tvRight.clickDelay {
            val bundle = Bundle().apply {
                putString("webUrl", HttpBaseUrlConstant.BASE_URL + "page/QnA")
                putString("webTitle", "常见问题")
            }
            jumpARoute(RouteUrl.WebModule.ACTIVITY_WEB_WEB, bundle)
        }
        tvCommit.clickDelay {
            if (accountBean?.cashCount == 0.0) {
                toast("可提现金额为0")
                return@clickDelay
            }
            mViewModel.requestWxAccount()
        }



        regToWx()
        handler = MyHandler(this@WithdrawActivity)
    }

    /**
     * 订阅数据
     */
    override fun initObserve() {
        if (accountBean != null) {
            mBinding.tvMoney.setText("${accountBean?.cashCount}")
            mBinding.tvFee.setText("预计到账金额：${accountBean?.cashReal}元（税费：${accountBean?.dutyCount}元）")
        }

        mViewModel.ldSuccess.observe(this) {
            when (it) {
                is WXAccountBindBean -> {
                    if (it.bindWeChat) {
                        showDialog(it.wxName)
                    } else {
                        requestWX()
                    }
                }

                is String -> {
                    showDialog(it)
                }

                is ApplyRequestBean -> {
                    finish()
                    val bundle = Bundle().apply {
                        putString("tradeId", it.tradeId)
                    }
                    jumpARoute(RouteUrl.MyModule.ACTIVITY_MY_WITHDRAWDETAIL, bundle)

                }
            }
        }

    }

    /**
     * 获取数据
     */
    override fun initRequestData() {

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

    /**
     * 显示确认弹框
     */
    fun showCommitDialog(wxName: String?) {
        val wxCommitDialog: WxCommitFragmentDialog = WxCommitFragmentDialog.newInstance(wxName)
        wxCommitDialog.show(this.getSupportFragmentManager())
        wxCommitDialog.dialogListener = object : WxCommitFragmentDialog.DialogOnClickListener {
            override fun onCancelClick() {
            }

            override fun onCommitClick() {
                mViewModel.requestApply()
            }

            override fun onEditClick() {
                requestWX()
            }
        }
    }

    /**
     * 绑定用户微信名
     */
    fun requestAddWXAccount(wxName: String, openId: String) {
        mViewModel.requestAddWXAccount(wxName, openId)
    }
}