package com.alan.mvvm.common.http.interceptor

import android.text.TextUtils
import com.alan.mvvm.base.http.baseresp.BaseResponse
import com.alan.mvvm.base.utils.EventBusUtils
import com.alan.mvvm.base.utils.GsonUtil
import com.alan.mvvm.common.event.TokenEvent
import com.socks.library.KLog
import okhttp3.Interceptor
import okhttp3.Response
import java.nio.charset.Charset

/**
 * 作者：alan
 * 时间：2021/9/26
 * 备注：Token过期拦截器
 */
class TokenInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
//        val jsonString = response.peekBody(2048).string()

        val body = response.body
        val source = body?.source()
        source?.request(Long.MAX_VALUE)
        val buffer = source?.buffer
        val jsonString = buffer?.clone()?.readString(Charset.forName("UTF-8"))
        KLog.e("http", "拦截器：$jsonString")
        if (!TextUtils.isEmpty(jsonString)) {
            try {
                val baseResponse = GsonUtil.jsonToBean(jsonString, BaseResponse::class.java)
                if (baseResponse?.resultCode == 5000) {
                    //Token过期
                    EventBusUtils.postEvent(TokenEvent())
                }
            } catch (e: Exception) {

            }
        }

        return response
    }
}