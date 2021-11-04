package com.alan.mvvm.common.push

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import cn.jpush.android.api.CustomMessage
import cn.jpush.android.service.JPushMessageReceiver
import com.socks.library.KLog
import org.json.JSONException
import org.json.JSONObject

class PushMessageReceiver : JPushMessageReceiver() {
    companion object {
        var TAG = "PushReceiver"
    }

    override fun onMessage(context: Context, customMessage: CustomMessage) {
        super.onMessage(context, customMessage)
        KLog.e(TAG, "收到通知: ${customMessage.toString()}")
        handleCustomMessage(context, customMessage)
    }

    //处理自定义通知
    private fun handleCustomMessage(context: Context, customMessage: CustomMessage) {
        val title = customMessage.title
        val message = customMessage.message
        val extras = customMessage.extra
        var extrasJsonObject: JSONObject? = null

        try {
//            extrasJsonObject = JSONObject(extras)
//            KLog.e(TAG, "收到通知Json:$extrasJsonObject")
//            if (TextUtils.equals(pushType, "APPLICATION")) {
//                //跳转到申请认证页面
//
//            }
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("ring://com.moment.ring/main/")
            )
            val resultPendingIntent =
                PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
            NotificationPushManager.getInstance()
                .sendNotification(context, title, message, resultPendingIntent)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}