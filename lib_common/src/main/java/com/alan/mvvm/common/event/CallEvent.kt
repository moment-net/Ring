package com.alan.mvvm.common.event

data class CallEvent(
    var isComingCall: Boolean = true,
    var channelName: String = "",
    var username: String = ""
)
