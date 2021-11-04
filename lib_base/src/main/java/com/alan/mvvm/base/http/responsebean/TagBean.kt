package com.alan.mvvm.base.http.responsebean


data class TagBean(
    var checkedValues: ArrayList<String> = arrayListOf(),
    var tagEnName: String,
    var tagName: String,
    var tagType: String,
    var limit: Int,
    var values: ArrayList<String> = arrayListOf()
)