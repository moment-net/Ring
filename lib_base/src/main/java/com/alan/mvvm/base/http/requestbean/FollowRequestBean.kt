package com.alan.mvvm.base.http.requestbean

data class FollowRequestBean(
    var userId: String,
    var tag: Int,
    var followType: String,
)