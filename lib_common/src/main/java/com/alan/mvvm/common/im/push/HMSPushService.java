package com.alan.mvvm.common.im.push;

import com.huawei.hms.push.HmsMessageService;
import com.hyphenate.chat.EMClient;
import com.hyphenate.util.EMLog;

/**
 * 作者：alan
 * 时间：2021/9/14
 * 备注：华为推送服务
 */
public class HMSPushService extends HmsMessageService {

    @Override
    public void onNewToken(String token) {
        if (token != null && !token.equals("")) {
            //没有失败回调，假定token失败时token为null
            EMLog.d("HWHMSPush", "service register huawei hms push token success token:" + token);
            EMClient.getInstance().sendHMSPushTokenToServer(token);
        } else {
            EMLog.e("HWHMSPush", "service register huawei hms push token fail!");
        }
    }

}
