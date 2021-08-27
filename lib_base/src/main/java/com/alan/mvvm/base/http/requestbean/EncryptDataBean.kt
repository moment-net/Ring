package com.alan.mvvm.base.http.requestbean

data class EncryptDataBean(
    var data: String = "",
    var iv: String = "",
    var sessionKey: String = ""
)
