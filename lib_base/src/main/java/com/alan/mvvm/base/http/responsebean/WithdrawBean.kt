package com.alan.mvvm.base.http.responsebean

data class WithdrawBean(
    var tradeId: String? = null,
    var wxName: String? = null,
    var cashCount: Double = 0.0,
    var result: String? = null,
    var processMsg: String? = null,
    var operateTime: String? = null,
    var status: String? = null,
)
