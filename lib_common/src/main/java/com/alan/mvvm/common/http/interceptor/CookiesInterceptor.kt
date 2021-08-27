package com.alan.mvvm.common.http.interceptor

import android.os.Build
import android.text.TextUtils
import com.alan.mvvm.common.helper.SpHelper
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response


class CookiesInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder: Request.Builder = request.newBuilder()

        /**
         * 登陆相关接口不设置cookie
         */
        val url = request.url.toString()

        builder.addHeader("device_platform", "android")
        builder.addHeader("device_type", Build.MODEL)
        builder.addHeader("device_brand", Build.MANUFACTURER)
        builder.addHeader("os_version", Build.VERSION.RELEASE)
//        builder.addHeader("app_version", SystemUtil.getApkVersionName(BaseApp.getInstance()))
//        builder.addHeader("udid", DeviceUtil.getDeviceLongUuId(BaseApp.getInstance()))
//        builder.addHeader("imei", DeviceUtil.getAndroidIMEI(BaseApp.getInstance()))
//        //推广邀请码
//        builder.addHeader("inviteCode", PreUtils.getString(BaseApp.getInstance(), "inviteCode", ""))
//        //推广渠道
//        builder.addHeader("channel", SystemUtil.getChannel(BaseApp.getInstance()))


        val token: String = SpHelper.getToken()
        if (!TextUtils.isEmpty(token)) {
            builder.addHeader("token", token)
        }


        return chain.proceed(builder.build())
    }


}