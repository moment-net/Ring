package com.alan.mvvm.base.http.responsebean

data class CardInfoBean(
    var id: String? = null,
    var userId: String? = null,
    var cardName: String? = null,
    var desc: String? = null,
    var style: StyleBean? = null,
    var tags: List<TagBean>? = null
)


data class StyleBean(
    val picUrl: String,
    val bgColor: String,
    val textOpacity: Float,
    val bgOpacity: Float,
    val textColor: String,
)
