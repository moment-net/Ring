package com.alan.mvvm.base.utils

/**
 * 作者：alan
 * 时间：2021/7/28
 * 备注：辅助注册EventBus注解
 * 只有添加这个，Activity才可以注册成功
 */
@Target(AnnotationTarget.CLASS)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class EventBusRegister