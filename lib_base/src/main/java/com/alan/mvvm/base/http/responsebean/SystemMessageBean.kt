package com.alan.mvvm.base.http.responsebean

data class SystemMessageBean(
    var id: String? = null,
    var title: String? = null,
    var content: String? = null,
    var url: String? = null,
    var userId: String? = null,
    var source: Int = 0,
    var isRead: Int = 0,
    var createTime: String? = null
)
