package com.alan.module.main.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Message
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import com.alan.module.main.databinding.ActivityLoginWxBinding
import com.alan.module.main.viewmodel.LoginWxViewModel
import com.alan.mvvm.base.coil.CoilUtils
import com.alan.mvvm.base.http.requestbean.LoginThirdRequestBean
import com.alan.mvvm.base.http.responsebean.FileBean
import com.alan.mvvm.base.http.responsebean.UserInfoBean
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.base.utils.*
import com.alan.mvvm.common.constant.Constants
import com.alan.mvvm.common.constant.RouteUrl
import com.alan.mvvm.common.event.WXCodeEvent
import com.alan.mvvm.common.helper.SpHelper
import com.alan.mvvm.common.report.DataPointUtil
import com.alan.mvvm.common.ui.BaseActivity
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
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
import java.util.*

/**
 * 作者：alan
 * 时间：2021/7/30
 * 备注：注册信息
 */
@EventBusRegister
@Route(path = RouteUrl.MainModule.ACTIVITY_MAIN_WXINFO)
@AndroidEntryPoint
class LoginWxActivity : BaseActivity<ActivityLoginWxBinding, LoginWxViewModel>() {
    // IWXAPI 是第三方app和微信通信的openApi接口
    var api: IWXAPI? = null
    var handler: MyHandler? = null
    var openId: String? = null
    var accessToken: String? = null
    var refreshToken: String? = null
    var scope: String? = null
    var unionid: String? = null

    //1是手机号登录流程，2是微信登录流程
    @JvmField
    @Autowired(name = "type")
    var type = 0

    //性别
    var gender = 0
    var birthday: String? = null
    var imgUrl: String? = null
    var address: String? = null

    inner class MyHandler(activity: Activity) : Handler() {
        private val wxEntryActivityWeakReference: WeakReference<Activity>
        override fun handleMessage(msg: Message) {
            val tag = msg.what
            val data = msg.data
            var json: JSONObject? = null
            val activity = wxEntryActivityWeakReference.get() as LoginWxActivity?
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
                NetworkUtil.GET_IMG -> {
                    val imgdata = data.getByteArray("imgdata")
                    val bitmap: Bitmap?
                    bitmap = if (imgdata != null) {
                        BitmapFactory.decodeByteArray(imgdata, 0, imgdata.size)
                    } else {
                        null
                    }

                    val img: String = BitmapUtil.save(activity, bitmap) ?: ""
                    activity?.requestUploadPic(img)
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
    override val mViewModel by viewModels<LoginWxViewModel>()

    /**
     * 初始化View
     */
    override fun ActivityLoginWxBinding.initView() {
        ivAvator.clickDelay {
            ImageSelectUtil.singlePicCrop(this@LoginWxActivity)
        }
        tvBind.clickDelay {
            requestWX()
        }
        rbBoy.clickDelay {
            gender = 1
        }
        rbGirl.clickDelay {
            gender = 2
        }
        tvCommit.clickDelay {
            DataPointUtil.reportProfileNext(SpHelper.getUserInfo()?.userId!!)
            requestEditUserInfo()
        }
        tvBirthdayValue.clickDelay {
            changeBirthday()
        }
        tvHometownValue.clickDelay {
            changeAddress()
        }

        etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s?.length!! > 0) {
                    tvCommit.setEnabled(true);
                } else {
                    tvCommit.setEnabled(false);
                }

                tvLimit.setText("${s?.length!!}/15");
            }
        })


        if (type == 1) {
            mBinding.llBind.setVisibility(View.VISIBLE)
            if (SpHelper.getUserInfo()?.bindWeChat!!) {
                mBinding.tvBind.setText("已绑定微信")
                mBinding.tvBind.setEnabled(false)
            } else {
                mBinding.tvBind.setText("绑定微信")
                mBinding.tvBind.setEnabled(true)
            }

            regToWx()
            handler = MyHandler(this@LoginWxActivity)

        } else {
            mBinding.llBind.setVisibility(View.GONE)
            mBinding.etName.setText(SpHelper.getUserInfo()?.userName)
            if (SpHelper.getUserInfo()?.gender == 1) {
                gender = 1
                mBinding.rbBoy.setChecked(true)
            } else if (SpHelper.getUserInfo()?.gender == 2) {
                gender = 2
                mBinding.rbGirl.setChecked(true)
            }
        }


        if (SpHelper.getUserInfo() != null) {
            mBinding.etName.setText(SpHelper.getUserInfo()?.userName)
            mBinding.etName.clearFocus()
            CoilUtils.loadCircle(mBinding.ivAvator, SpHelper.getUserInfo()?.avatar!!)
        }
    }

    /**
     * 订阅数据
     */
    override fun initObserve() {
        mViewModel.ldSuccess.observe(this) {
            when (it) {
                is FileBean -> {
                    //上传图片
                    imgUrl = it.fileName
                    CoilUtils.loadCircle(mBinding.ivAvator, it.fileUrl)
                }

                is LoginThirdRequestBean -> {
                    //绑定微信
                    toast("微信绑定成功")
                    mBinding.etName.setText(it?.nickname)
                    NetworkUtil.getImage(handler, it?.oauthToken, NetworkUtil.GET_IMG)
                    CoilUtils.loadCircle(mBinding.ivAvator, it?.oauthToken)

                    mBinding.tvBind.setText("已绑定微信")
                    mBinding.tvBind.setEnabled(false)
                }

                is UserInfoBean -> {
                    //用户信息更新
                    jumpARoute(RouteUrl.MainModule.ACTIVITY_MAIN_STATE)
                    finish()
                }

            }
        }

    }

    /**
     * 获取数据
     */
    override fun initRequestData() {

    }

    fun changeAddress() {
        LocationPickerUtil.showPickerView(
            this,
            object : LocationPickerUtil.OnPickerListener {
                override fun onPicker(opt1: String, opt2: String, opt3: String) {
                    address = opt1 + "-" + opt2 + "-" + opt3
                    mBinding.tvHometownValue.setText(opt3)
                }

            }
        )
    }

    /**
     * 生日
     */
    fun changeBirthday() {
        //时间选择器
        val startDate = Calendar.getInstance()
        val endDate = Calendar.getInstance()
        startDate[1938, 3] = 8
        val timePickerBuilder = TimePickerBuilder(this) { date: Date?, v: View? ->
            birthday = getTime(date)
            mBinding.tvBirthdayValue.setText(birthday)
        }
            .setRangDate(startDate, endDate)
            .setDate(getData(2020, 5, 4))

        if (!TextUtils.isEmpty(SpHelper.getUserInfo()?.birthday)) {
            val birth: List<String> = SpHelper.getUserInfo()?.birthday!!.split("-")
            val birthday = IntArray(3)
            if (birth.size == 3) {
                try {
                    for (i in birth.indices) {
                        birthday[i] = birth[i].toInt()
                    }
                    timePickerBuilder.setDate(getData(birthday[0], birthday[1] - 1, birthday[2]))
                } catch (e: java.lang.Exception) {
                    e.message
                }
            }
        } else {
            timePickerBuilder.setDate(getData(2000, 1, 1))
        }
        val pvTime = timePickerBuilder.build()
        pvTime.show()
    }

    fun getTime(date: Date?): String? {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar[Calendar.YEAR].toString() + "-" + (calendar[Calendar.MONTH] + 1) + "-" + calendar[Calendar.DATE]
    }

    fun getData(year: Int, month: Int, day: Int): Calendar? {
        val calendar = Calendar.getInstance()
        calendar[year, month] = day
        return calendar
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                PictureConfig.CHOOSE_REQUEST -> {
                    // 例如 LocalMedia 里面返回五种path
                    // 1.media.getPath(); 原图path
                    // 2.media.getCutPath();裁剪后path，需判断media.isCut();切勿直接使用
                    // 3.media.getCompressPath();压缩后path，需判断media.isCompressed();切勿直接使用
                    // 4.media.getOriginalPath()); media.isOriginal());为true时此字段才有值
                    // 5.media.getAndroidQToPath();Android Q版本特有返回的字段，但如果开启了压缩或裁剪还是取裁剪或压缩路径；注意：.isAndroidQTransform 为false 此字段将返回空
                    // 如果同时开启裁剪和压缩，则取压缩路径为准因为是先裁剪后压缩
                    val selectList = PictureSelector.obtainMultipleResult(data)
                    if (selectList != null && !selectList.isEmpty()) {
                        val media = selectList.get(0)
                        val url = if (media.isCut) {
                            media.cutPath
                        } else {
                            media.getPath()
                        }
                        requestUploadPic(url)
                    }
                }
            }
        }
    }



    fun requestEditUserInfo() {
        val userName: String = mBinding.etName.getText().toString()
        if (TextUtils.isEmpty(userName)) {
            toast("昵称不能为空")
            return
        }
        if (gender == 0) {
            toast("请选择性别")
            return
        }
        mViewModel.requestEditUserInfo(
            userName,
            "",
            imgUrl ?: "",
            gender,
            birthday ?: "",
            "",
            address ?: ""
        )
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


    /**
     * 上传图片
     */
    fun requestUploadPic(url: String) {
        mViewModel.requestUploadPic(url)
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
