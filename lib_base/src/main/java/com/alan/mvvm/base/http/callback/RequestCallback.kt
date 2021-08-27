package com.alan.mvvm.base.http.callback

import com.alan.mvvm.base.http.baseresp.BaseResponse
import com.alan.mvvm.common.http.exception.BaseHttpException


/**
 * 作者：alan
 * 时间：2021/8/23
 * 备注：请求返回
 */
open class RequestCallback<T>(
    internal var onStart: (() -> Unit)? = null,
    internal var onCancelled: (() -> Unit)? = null,
    internal var onSuccess: ((BaseResponse<T>) -> Unit)? = null,
    internal var onFailed: ((BaseHttpException) -> Unit)? = null,
    internal var onFinally: (() -> Unit)? = null
) {

    /**
     * 在显示 Loading 之后且开始网络请求之前执行
     */
    fun onStart(block: () -> Unit) {
        this.onStart = block
    }

    /**
     * 如果外部主动取消了网络请求，不会回调 onFail，而是回调此方法，随后回调 onFinally
     * 但如果当取消网络请求时已回调了 onSuccess / onSuccessIO 方法，则不会回调此方法
     */
    fun onCancelled(block: () -> Unit) {
        this.onCancelled = block
    }

    /**
     * 当网络请求成功时会调用此方法，随后会先后调用 onSuccessIO、onFinally 方法
     */
    fun onSuccess(block: (data: BaseResponse<T>) -> Unit) {
        this.onSuccess = block
    }

    /**
     * 当网络请求失败时会调用此方法，在 onFinally 被调用之前执行
     */
    fun onFailed(block: (BaseHttpException) -> Unit) {
        this.onFailed = block
    }

    /**
     * 在网络请求结束之后（不管请求成功与否）且隐藏 Loading 之前执行
     */
    fun onFinally(block: () -> Unit) {
        this.onFinally = block
    }

}