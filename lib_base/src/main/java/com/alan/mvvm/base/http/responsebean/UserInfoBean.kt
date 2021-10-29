package com.alan.mvvm.base.http.responsebean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserInfoBean(
    var id: String = "",
    var userId: String = "",
    var userName: String = "",
    // 1是男 2是女 0是没性别,
    var gender: Int = 0,
    var status: Boolean? = false,
    var avatar: String = "",
    var followCount: Int = 0,
    var fansCount: Int = 0,
    var followStatus: Int = 0,
    var type: Int = 0,
    var hostLevel: String? = "",
    var reviewScore: String? = "",
    //屏蔽状态，1为屏蔽，0为未屏蔽；
    var shieldStatus: Int = 0,
    var desc: String? = "",
    var inviteStatus: Int = 0,
    var online: Boolean? = false,
    var dyUserInfoId: Int = 0,
    var dyNickname: String? = "",
    var registTime: String? = "",
    var refuseHotmusic: Boolean? = false,
    var onlyAdvanced: Boolean? = false,
    var alreadyFollowed: Boolean? = false,
    var bindPhone: Boolean? = false,
    var bindWeChat: Boolean? = false,
    //现居地址
    var birthday: String? = "",
    var address: String? = "",
    var hometown: String? = "",
    var onlineStatus: Boolean? = false,
    var audioDesc: GreetBean? = null,
    var greeting: GreetBean? = null,
    var headsetStatus: String? = "",
    var onlineStatusDesc: String? = "",
    var age: Int = 0,
    var bindApple: Boolean? = false,
    var eduRight: Int = 0,
    var hasHeadset: Boolean? = false,
    var needInvite: Int = 0,
    var starSign: String? = "",
    val likes: List<String>,
    val typeTag: List<String>,
    val mealStatusTitle: String,
    val recentDoing: String,
) : Parcelable