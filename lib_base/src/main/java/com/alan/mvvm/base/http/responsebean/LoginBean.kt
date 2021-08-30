package com.alan.mvvm.base.http.responsebean

import java.io.Serializable

data class LoginBean(
    var token: TokenBean? = null,
    var user: UserInfoBean? = null,
    var newUser: Boolean = false,
    var needInvite: Int = 0,
) : Serializable