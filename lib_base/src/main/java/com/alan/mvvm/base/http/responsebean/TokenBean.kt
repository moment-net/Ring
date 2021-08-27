package com.alan.mvvm.base.http.responsebean

data class TokenBean(
    var expiresIn: String? = null,
    var token: String? = null
)
