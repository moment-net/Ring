package com.moment.ring.wxapi;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.alan.mvvm.base.utils.EventBusUtils;
import com.alan.mvvm.common.constant.Constants;
import com.alan.mvvm.common.event.WXPayEvent;
import com.socks.library.KLog;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends FragmentActivity implements IWXAPIEventHandler {
    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);

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
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            KLog.e("xujm", "onPayFinish,errCode=" + resp.errCode);
            EventBusUtils.INSTANCE.postEvent(new WXPayEvent(resp.errCode));
        }
        finish();
    }
}