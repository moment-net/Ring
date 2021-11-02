package com.alan.mvvm.base.http.responsebean

data class CardDetailBean(
    val cardName: String,
    val desc: String,
    val exist: Boolean,
    val id: String,
    val style: StyleBean,
    val tags: List<TagBean>,
    val userId: String
)



