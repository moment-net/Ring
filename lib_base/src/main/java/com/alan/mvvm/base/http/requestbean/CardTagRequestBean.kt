package com.alan.mvvm.base.http.requestbean

data class CardTagRequestBean(
    val cardName: String,
    val tags: ArrayList<Tag>
)

data class Tag(
    val tagName: String,
    val values: ArrayList<String>
)