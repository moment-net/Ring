package com.alan.mvvm.base.http.responsebean

/**
 * 作者：alan
 * 时间：2021/8/24
 * 备注：FreedomSpeakRtc
 */
data class FreedomSpeakRtc(
    var pushPath: String,
    var livePath: String,
    var channelName: String,
    var rtcToken: String
)