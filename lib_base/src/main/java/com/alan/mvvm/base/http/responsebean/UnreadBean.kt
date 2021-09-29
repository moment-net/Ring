package com.alan.mvvm.base.http.responsebean

data class UnreadBean(
    val newNoticeTotal: Int,
    val newOtherNoticeTotal: Int,
    val newSystemNoticeTotal: Int,
)