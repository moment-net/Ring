package com.alan.mvvm.common.http.model

data class BaseResponse<T>(
    var resultCode: Int = 0,
    var msg: String = "",
    var data: T,
    var hasMore: Boolean,
    val cursor: String,
    val cursorString: String,
    val token: String?

)
