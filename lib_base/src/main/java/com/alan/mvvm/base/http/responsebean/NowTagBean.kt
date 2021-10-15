package com.alan.mvvm.base.http.responsebean

data class NowTagBean(
    val bgColor: String,
    val bgOpacity: Float,
    val defaultText: String,
    val highlightPicUrl: String,
    val picUrl: String,
    val tag: String,
    val textColor: String,
    val textOpacity: Float
)