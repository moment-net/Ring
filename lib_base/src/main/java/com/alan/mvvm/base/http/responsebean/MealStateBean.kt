package com.alan.mvvm.base.http.responsebean

data class MealStateBean(
    val nextBeginTime: Long,// 下次匹配的开始时间
    val inMatchTime: Boolean,// 在匹配时间段中
    val currentEndTime: Long,// 本次匹配时间段的结束时间
    val matchStatus: Boolean,// 是否开启匹配
    val title: String,
    val desc: String,
    val value: Int
)