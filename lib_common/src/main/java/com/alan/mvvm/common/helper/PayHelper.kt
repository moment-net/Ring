package com.alan.mvvm.common.helper

import androidx.fragment.app.FragmentActivity
import com.alipay.sdk.app.PayTask
import com.socks.library.KLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * 作者：alan
 * 时间：2021/9/3
 * 备注：支付宝相关
 */
object PayHelper {

    fun requestPayResult(activity: FragmentActivity, orderInfo: String): Flow<Map<String, String>> {
        return flow {
            //异步解析
            val alipay = PayTask(activity);
            val result: Map<String, String> = alipay.payV2(orderInfo, true);
            KLog.e("xujm", "result2:" + result);
            emit(result);
        }
            .flowOn(Dispatchers.IO)
            .catch {
                KLog.e("xujm", "throwable:" + it.toString());
            }
    }

}