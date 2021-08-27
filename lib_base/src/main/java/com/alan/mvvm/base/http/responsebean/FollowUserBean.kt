package com.alan.mvvm.base.http.responsebean

import java.io.Serializable

data class FollowUserBean(
    private var status: Int = 0,
    val user: UserInfoBean? = null
) : Serializable
