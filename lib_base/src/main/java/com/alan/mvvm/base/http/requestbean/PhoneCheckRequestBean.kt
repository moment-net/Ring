package com.alan.mvvm.base.http.requestbean

data class PhoneCheckRequestBean(
    var processId: String = "",
    val token: String = "",
    val authcode: String = ""
)
