package com.alan.mvvm.base.http.requestbean

import com.alan.mvvm.base.http.responsebean.PicBean

data class NowRequestBean(
    var tag: String? = null,
    var content: String? = null,
    var pic: ArrayList<PicBean>? = null
)