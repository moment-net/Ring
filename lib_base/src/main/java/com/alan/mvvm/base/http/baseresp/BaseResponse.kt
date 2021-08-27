package com.alan.mvvm.base.http.baseresp

import java.io.Serializable

data class BaseResponse<T>(
    var resultCode: Int = 0,
    var msg: String = "",
    var data: T,
    var hasMore: Boolean,
    val cursor: String?,
    val cursorString: String?,
    val token: String?
) : Serializable {

    val httpIsSuccess: Boolean
        get() = resultCode == 0

}
