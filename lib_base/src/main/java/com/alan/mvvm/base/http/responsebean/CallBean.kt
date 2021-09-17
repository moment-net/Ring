package com.alan.mvvm.base.http.responsebean

data class CallBean(
    val chatTime: Int,
    val endTime: Int,
    val followStatus: Int,
    val serverTime: Int,
    val sessionId: String,
    val startTime: Int,
    val tips: String,
    val type: Int
)