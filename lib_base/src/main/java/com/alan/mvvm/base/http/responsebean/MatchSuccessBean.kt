package com.alan.mvvm.base.http.responsebean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MatchSuccessBean(
    val chatRoomId: String,
    val chatType: String,
    val host: UserInfoBean,
    val sessionId: String,
    val userTimes: Int,
    val hostTimes: Int,
    val type: Int,
    val user: UserInfoBean
) : Parcelable
