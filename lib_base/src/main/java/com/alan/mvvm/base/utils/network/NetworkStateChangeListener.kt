package com.alan.mvvm.base.utils.network

/**
 * 作者：alan
 * 时间：2021/7/28
 * 备注：网络状态改变监听起
 */
interface NetworkStateChangeListener {

    /**
     * 网络类型更改回调
     * @param type Int 网络类型
     * @return Unit
     */
    fun networkTypeChange(type: Int)

    /**
     * 网络连接状态更改回调
     * @param isConnected Boolean 是否已连接
     * @return Unit
     */
    fun networkConnectChange(isConnected: Boolean)
}