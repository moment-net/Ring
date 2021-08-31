package com.alan.mvvm.base.http.responsebean

import java.io.Serializable

data class UserInfoBean(
    var id: String? = "",
    val userId: String = "",
    val userName: String = "",
    // 1是男 2是女 0是没性别,
    val gender: Int = 0,
    val status: Boolean = false,
    val avatar: String = "",
    val followCount: Int = 0,
    val fansCount: Int = 0,
    val followStatus: Int = 0,
    val type: Int = 0,
    val hostLevel: String = "",
    val reviewScore: String = "",
    //屏蔽状态，1为屏蔽，0为未屏蔽；
    val shieldStatus: Int = 0,
    val desc: String = "",
    val inviteStatus: Int = 0,
    val online: Boolean = false,
    val dyUserInfoId: Int = 0,
    val dyNickname: String = "",
    val registTime: String = "",
    val refuseHotmusic: Boolean = false,
    val onlyAdvanced: Boolean = false,
    val alreadyFollowed: Boolean = false,
    val bindPhone: Boolean = false,
    val bindWeChat: Boolean = false,
    //现居地址
    val birthday: String = "",
    val address: String = "",
    val hometown: String = "",
    val onlineStatus: Boolean = false,
    val audioDesc: String = "",
    val greeting: GreetBean? = null,
    val headsetStatus: String = "",
    val onlineRoom: ChannelBean? = null,
    val age: Int,
    val bindApple: Boolean = false,
    val eduRight: Int,
    val hasHeadset: Boolean,
    val needInvite: Int,
    val starSign: String

) : Serializable