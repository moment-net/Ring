package com.alan.mvvm.base.http.requestbean

data class EditRequestBean(
    var userName: String? = null,
    var desc: String? = null,
    var avatar: String? = null,
    var gender: Int = 0,
    var birthday: String? = null,
    var address: String? = null,
    var hometown: String? = null,
    var audioDesc: String? = null,
    var audioGreeti: String? = null,
    val audioDuration: Int = 0
)
