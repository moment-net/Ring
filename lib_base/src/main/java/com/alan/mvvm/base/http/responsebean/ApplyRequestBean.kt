package com.alan.mvvm.base.http.responsebean

data class ApplyRequestBean(
    var id: String? = null,
    var userId: String? = null,
    var userType: Int = 0,
    var assetsCo: Double = 0.0,
    var cashCoun: Double = 0.0,
    var dutyCoun: Double = 0.0,
    var cashReal: Double = 0.0,
    var wxAccountName: String,
    var applyTime: String,
    val status: String? = null,
    var operator: String? = null,
    var operateTime: String? = null,
    val reason: String? = null,
    var tradeId: String? = null,
)
