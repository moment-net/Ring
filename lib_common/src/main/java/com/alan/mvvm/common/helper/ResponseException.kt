package com.alan.mvvm.common.helper

import com.alan.mvvm.common.helper.ResponseExceptionEnum as ExceptionType

/**
 * 作者：alan
 * 时间：2021/7/28
 * 备注：请求响应异常，主要为各种code码专门定义的异常
 * @property type ResponseExceptionEnum 异常类型枚举，用于标记该异常的类型
 */
class ResponseException(val type: ExceptionType) : Exception()

/**
 * 空异常，表示该异常已经被处理过了，不需要再做额外处理了
 */
class ResponseEmptyException : Exception()