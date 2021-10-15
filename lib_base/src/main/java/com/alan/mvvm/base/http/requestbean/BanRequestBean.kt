package com.alan.mvvm.base.http.requestbean

/**
 * action//默认1是点赞 0取消点赞 -1是屏蔽
 */
data class BanRequestBean(
    var id: String? = "0",
    var action: String? = null
)