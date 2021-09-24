package com.alan.mvvm.common.event

data class MessageEvent(var type: String, var event: String = "", var content: String? = "")
