package com.alan.mvvm.common.http.exception

import com.alan.mvvm.base.http.baseresp.BaseResponse


/**
 * @param errorCode        服务器返回的错误码 或者是 HttpConfig 中定义的本地错误码
 * @param errorMessage     服务器返回的异常信息 或者是 请求过程中抛出的信息，是最原始的异常信息
 * @param realException    用于当 code 是本地错误码时，存储真实的运行时异常
 */
open class BaseHttpException(
    val errorCode: Int,
    val errorMessage: String,
    val realException: Throwable?
) : Exception(errorMessage) {

    companion object {
        /**
         * 未知错误
         */
        const val ERROR_UNKNOWN = 1000

        /**
         * 解析错误
         */
        const val ERROR_PARSE = 1001

        /**
         * 网络错误
         */
        const val ERROR_NETWORK = 1002

        /**
         * 协议出错
         */
        const val ERROR_HTTP = 1003

        /**
         * 证书出错
         */
        const val ERROR_SSL = 1005

        /**
         * 连接超时
         */
        const val ERROR_TIMEOUT = 1006

        /**
         * 没有网络
         */
        const val ERROR_NO_NET = 9999

        /**
         * 没有指定主机
         */
        const val ERROR_NO_HOST = 1008

        /**
         * 此变量用于表示在网络请求过程过程中抛出了异常
         */
        const val CODE_ERROR_LOCAL_UNKNOWN = -1024520

    }
}


/**
 * API 请求成功了，但 code != successCode
 * @param errorCode
 * @param errorMessage
 */
class ServerCodeBadException(errorCode: Int, errorMessage: String) :
    BaseHttpException(errorCode, errorMessage, null) {

    constructor(bean: BaseResponse<*>) : this(bean.resultCode, bean.msg)

}

/**
 * 请求过程抛出异常
 * @param throwable
 */
class LocalBadException(throwable: Throwable) : BaseHttpException(
    CODE_ERROR_LOCAL_UNKNOWN, throwable.message
        ?: "", throwable
)