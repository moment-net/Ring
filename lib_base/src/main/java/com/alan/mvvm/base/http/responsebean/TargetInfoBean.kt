package com.alan.mvvm.base.http.responsebean

data class TargetInfoBean(
    var id: String,
    var userId: String,
    var likes: ArrayList<String>,
    var typeTag: ArrayList<String>,
    var createTime: String
)
