package com.alan.mvvm.base.mvvm.m

import com.alan.mvvm.base.http.apiservice.HomeApiService
import com.alan.mvvm.base.http.baseresp.BaseResponse
import com.alan.mvvm.base.http.callback.RequestCallback
import com.alan.mvvm.common.http.exception.BaseHttpException
import com.alan.mvvm.common.http.exception.BaseHttpException.Companion.CODE_ERROR_LOCAL_UNKNOWN
import com.alan.mvvm.common.http.exception.BaseHttpException.Companion.ERROR_NO_NET
import com.alan.mvvm.common.http.exception.BaseHttpException.Companion.ERROR_PARSE
import com.alan.mvvm.common.http.exception.BaseHttpException.Companion.ERROR_UNKNOWN
import com.google.gson.JsonSyntaxException
import com.socks.library.KLog
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.net.UnknownServiceException
import java.security.GeneralSecurityException

/**
 * 作者：alan
 * 时间：2021/7/28
 * 备注：仓库层 Repository 基类
 */
open class BaseRepository {

    /**
     * 发起请求封装
     * 该方法将flow的执行切换至IO线程
     *
     * @param requestBlock 请求的整体逻辑
     * @return Flow<T>
     */
    suspend fun <T> request(
        mApi: HomeApiService,
        callback: RequestCallback<T>,
        request: suspend HomeApiService.() -> BaseResponse<T>?
    ) {
        callback.onStart?.invoke()
        KLog.e("http", "onStart==>")
        flow {
            val response = request.invoke(mApi) ?: throw BaseHttpException(
                CODE_ERROR_LOCAL_UNKNOWN,
                "数据非法，获取响应数据为空",
                null
            )
            KLog.e("http", "结果==>response:${response}")
            if (!response.httpIsSuccess) {
                throw  BaseHttpException(response.resultCode, response.msg, null)
            }
            emit(response)
        }
            .flowOn(Dispatchers.IO)
            .catch {
                KLog.e("http", "catch==>throw:${it}")
                handleException(it, callback)
            }
            .onCompletion { cause ->
                KLog.e("http", "onCompletion==>cause:${cause}")
                callback.onFinally?.invoke()
            }.collect {
                KLog.e("http", "collect==>it:${it}")
                callback.onSuccess?.invoke(it)
            }
    }


    /**
     * 处理异常
     */
    protected fun <T> handleException(throwable: Throwable, callback: RequestCallback<T>) {
        if (throwable is CancellationException) {
            KLog.e("http", "onCancelled==>exception:${throwable}")
            callback.onCancelled?.invoke()
            return
        }
        val exception: BaseHttpException
        when (throwable) {
            is HttpException -> {
                exception = BaseHttpException(throwable.code(), "服务器出了点小差", throwable)
            }
            is ConnectException, is SocketTimeoutException, is UnknownHostException, is GeneralSecurityException, is UnknownServiceException, is IOException -> {
                exception = BaseHttpException(ERROR_NO_NET, "没有网络，请检查网络设置", throwable)
            }
            is JsonSyntaxException -> {
                exception = BaseHttpException(ERROR_PARSE, "解析出错，服务器异常", throwable)
            }
            else -> {
                exception =
                    BaseHttpException(ERROR_UNKNOWN, throwable.message ?: "请求出错，请检查网络设置", throwable)
            }
        }
        KLog.e("http", "onFailed==>exception:${exception}")
        callback.onFailed?.invoke(exception)
    }


}