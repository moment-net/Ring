package com.alan.mvvm.base.http.responsebean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * 作者：alan
 * 时间：2021/8/24
 * 备注：FreedomSpeakRtc
 */
@Parcelize
data class FreedomSpeakRtc(
    var pushPath: String,
    var livePath: String,
    var channelName: String,
    var rtcToken: String
) : Parcelable