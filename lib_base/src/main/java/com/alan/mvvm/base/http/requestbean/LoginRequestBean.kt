package com.alan.mvvm.base.http.requestbean

data class LoginRequestBean(
    var phone: String = "",
    var code: String = "",
    var installParam: String = "",
    var isChecked: Int = 0
)