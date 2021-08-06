package com.alan.mvvm.common.http.exception

/**
 * 作者：alan
 * 时间：2021/7/28
 * 备注：请求响应异常枚举的抽象
 */
interface ResponseExceptionEnumCode {

    /**
     * 获取该异常枚举的code码
     * @return Int
     */
    fun getCode(): Int

    /**
     * 获取该异常枚举的描述
     * @return String
     */
    fun getMessage(): String
}