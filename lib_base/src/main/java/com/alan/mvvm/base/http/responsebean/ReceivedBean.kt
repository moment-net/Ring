package com.alan.mvvm.base.http.responsebean

data class ReceivedBean(
    var time: String? = null,
    var list: List<ListBean>? = null
)

data class ListBean(
    var detail: String? = null,
    var fromUserName: String? = null,
    var fromUserId: String? = null,
    var receiveDate: String? = null,
    var receiveTime: String? = null,
    var gitId: String? = null,
    var gitNum: Int = 0,
    var totalPoints: String? = null,
)
