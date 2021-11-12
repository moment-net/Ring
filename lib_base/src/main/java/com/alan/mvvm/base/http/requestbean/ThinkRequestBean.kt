package com.alan.mvvm.base.http.requestbean

import com.alan.mvvm.base.http.responsebean.PicBean

data class ThinkRequestBean(
    var content: String? = null,
    var pic: ArrayList<PicBean>? = null
)