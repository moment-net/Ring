package com.alan.mvvm.base.http.responsebean

import com.google.gson.annotations.SerializedName


data class PrepayBean(
    @SerializedName("package")
    var packageX: String = "",
    var appid: String = "",
    var sign: String = "",
    var partnerid: String = "",
    var prepayid: String = "",
    var noncestr: String = "",
    var prepay_id: String = "",
    var timestamp: String = "",
)
