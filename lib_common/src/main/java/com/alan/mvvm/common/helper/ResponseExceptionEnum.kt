package com.alan.mvvm.common.helper

/**
 * 作者：alan
 * 时间：2021/7/28
 * 备注：请求响应异常的类型
 */
enum class ResponseExceptionEnum : ResponseExceptionEnumCode {
    INTERNAL_SERVER_ERROR {
        override fun getCode() = 500
        override fun getMessage() = "服务器内部错误"
    }
}