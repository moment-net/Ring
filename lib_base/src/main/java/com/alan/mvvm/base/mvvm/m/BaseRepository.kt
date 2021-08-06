package com.alan.mvvm.base.mvvm.m

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

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
    protected fun <T> request(requestBlock: suspend FlowCollector<T>.() -> Unit): Flow<T> {
        return flow(block = requestBlock).flowOn(Dispatchers.IO).catch {

        }
    }
}