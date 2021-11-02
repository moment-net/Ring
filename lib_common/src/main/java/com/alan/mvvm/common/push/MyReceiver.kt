package com.alan.mvvm.common.push

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.socks.library.KLog

/**
 * 自定义接收器
 *
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
class MyReceiver : BroadcastReceiver() {
    companion object {
        var TAG = "PushReceiver"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        KLog.e(TAG, "[MyReceiver] onReceive - ${intent!!.action}")
    }
}