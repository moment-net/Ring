package com.alan.mvvm.base.http.requestbean

//action=1 是添加 -1是删除这个标签 不传action默认是1 添加
data class ModifyRequestBean(
    val tag: String,
    val action: Int
)