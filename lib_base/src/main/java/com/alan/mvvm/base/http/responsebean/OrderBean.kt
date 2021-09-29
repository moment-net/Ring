package com.alan.mvvm.base.http.responsebean


data class OrderBean(
    var orderId: String = "",
    var totalFee: Float = 0f,
    var num: Int = 0,
    var state: Int = 0,//'支付状态:\n1: 未支付\n2: 支付成功\n3: 支付失败',
    var goodsName: String = "",
    var tradeTime: String = "",
)
