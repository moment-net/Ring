package com.moment.ring.wxapi

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.FragmentActivity
import com.alan.module.main.activity.MainActivity
import com.alan.mvvm.base.utils.EventBusUtils
import com.alan.mvvm.common.constant.Constants
import com.alan.mvvm.common.event.WXCodeEvent
import com.socks.library.KLog
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.modelmsg.ShowMessageFromWX
import com.tencent.mm.opensdk.modelmsg.WXAppExtendObject
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import org.json.JSONException
import org.json.JSONObject

class WXEntryActivity : FragmentActivity(), IWXAPIEventHandler {
    lateinit var api: IWXAPI

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        KLog.e("xujm", "微信onCreate")
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID, false)
        try {
            val intent = intent
            api.handleIntent(intent, this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        api.handleIntent(intent, this)
        KLog.e("xujm", "微信onNewIntent")
    }

    override fun onReq(req: BaseReq) {
        if (req.type == ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX) {
            val showReq = req as ShowMessageFromWX.Req
            val wxMsg = showReq.message
            val obj = wxMsg.mediaObject as WXAppExtendObject
            val extInfo = obj.extInfo // 对应 小程序 app_paramter 参数
            KLog.e("xujm", "类型：" + req.getType() + "返回值：" + extInfo)
            //{"ChannelId":"1099216325","chatRoomId":"129997018824705"}
            try {
                val intent = Intent(this, MainActivity::class.java)
                if (!TextUtils.isEmpty(extInfo)) {
                    val jsonObject = JSONObject(extInfo)
                    val channelId = jsonObject.optString("ChannelId")
                    val chatRoomId = jsonObject.optString("chatRoomId")
                    intent.putExtra("channelId", channelId)
                    intent.putExtra("chatRoomId", chatRoomId)
                }
                startActivity(intent)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        finish()
    }

    override fun onResp(resp: BaseResp) {
        val result: String
        result = when (resp.errCode) {
            BaseResp.ErrCode.ERR_OK -> "成功"
            BaseResp.ErrCode.ERR_USER_CANCEL -> "取消"
            BaseResp.ErrCode.ERR_AUTH_DENIED -> "延迟"
            BaseResp.ErrCode.ERR_UNSUPPORT -> "不支持"
            else -> "不明错误"
        }
        KLog.e("xujm", "类型：" + resp.type + "返回值：" + result)

        //获取微信Code
        if (resp.type == ConstantsAPI.COMMAND_SENDAUTH) {
            val authResp = resp as SendAuth.Resp
            val code = authResp.code
            KLog.e("xujm", "微信认证返回CODE：$code")
            EventBusUtils.postEvent(WXCodeEvent(code));
        }
        finish()
    }
}