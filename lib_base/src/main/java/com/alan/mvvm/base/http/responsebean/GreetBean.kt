package com.alan.mvvm.base.http.responsebean

data class GreetBean(
    var duration: Int = 0,
    val audioName: String = "",
    val audioPath: String? = "",
)
