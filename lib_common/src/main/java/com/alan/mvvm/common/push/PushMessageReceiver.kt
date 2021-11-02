package com.alan.mvvm.common.push

import android.content.Context
import cn.jpush.android.api.CustomMessage
import cn.jpush.android.service.JPushMessageReceiver
import com.socks.library.KLog

class PushMessageReceiver : JPushMessageReceiver() {
    companion object {
        var TAG = "PushReceiver"
    }

    override fun onMessage(context: Context?, customMessage: CustomMessage?) {
        super.onMessage(context, customMessage)
        KLog.e(TAG, "onReceive: ${customMessage.toString()}")
    }


}