package com.alan.mvvm.base.http.responsebean

data class CardTagBean(
    val tag: String,
    val picUrl: String,
    val highlightPicUrl: String,
    val textColor: String,
    val textOpacity: Float,
    val bgColor: String,
    val bgOpacity: Float,
    val defaultText: String,
)

