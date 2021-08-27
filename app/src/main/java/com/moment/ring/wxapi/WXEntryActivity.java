package com.moment.ring.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.fragment.app.FragmentActivity;

import com.alan.module.main.activity.MainActivity;
import com.alan.mvvm.common.constant.Constants;
import com.socks.library.KLog;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.ShowMessageFromWX;
import com.tencent.mm.opensdk.modelmsg.WXAppExtendObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

public class WXEntryActivity extends FragmentActivity implements IWXAPIEventHandler {
    private IWXAPI api;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        KLog.e("xujm", "微信onCreate");
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID, false);


        try {
            Intent intent = getIntent();
            api.handleIntent(intent, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
        KLog.e("xujm", "微信onNewIntent");
    }

    @Override
    public void onReq(BaseReq req) {
        if (req.getType() == ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX) {
            ShowMessageFromWX.Req showReq = (ShowMessageFromWX.Req) req;
            WXMediaMessage wxMsg = showReq.message;
            WXAppExtendObject obj = (WXAppExtendObject) wxMsg.mediaObject;
            String extInfo = obj.extInfo;// 对应 小程序 app_paramter 参数
            KLog.e("xujm", "类型：" + req.getType() + "返回值：" + extInfo);
            //{"ChannelId":"1099216325","chatRoomId":"129997018824705"}
            try {
                Intent intent = new Intent(this, MainActivity.class);
                if (!TextUtils.isEmpty(extInfo)) {
                    JSONObject jsonObject = new JSONObject(extInfo);
                    String channelId = jsonObject.optString("ChannelId");
                    String chatRoomId = jsonObject.optString("chatRoomId");
                    intent.putExtra("channelId", channelId);
                    intent.putExtra("chatRoomId", chatRoomId);
                }
                startActivity(intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        finish();
    }

    @Override
    public void onResp(BaseResp resp) {
        String result;

        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = "成功";
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = "取消";
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = "延迟";
                break;
            case BaseResp.ErrCode.ERR_UNSUPPORT:
                result = "不支持";
                break;
            default:
                result = "不明错误";
                break;
        }

        KLog.e("xujm", "类型：" + resp.getType() + "返回值：" + result);

        //获取微信Code
        if (resp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {
            SendAuth.Resp authResp = (SendAuth.Resp) resp;
            final String code = authResp.code;
            KLog.e("xujm", "微信认证返回CODE：" + code);
//            EventBus.getDefault().post(new WXCodeEvent(code));
        }
        finish();
    }

}
