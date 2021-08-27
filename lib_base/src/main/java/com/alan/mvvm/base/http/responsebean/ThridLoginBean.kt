package com.alan.mvvm.base.http.responsebean


data class ThridLoginBean(
    var user: UserInfoBean? = null,
    var tokenBean: TokenBean? = null,
    var newUser: Boolean = false,
)