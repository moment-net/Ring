package com.alan.mvvm.base.http.responsebean

data class ChannelBean(
    var id: String? = null,
    var name: String? = null,
    var chatRoomId: String? = null,
    var hostUserId: String? = null,
    var hostInfo: UserInfoBean? = null,
    var onlineNum: Int = 0,
    var onlineStatus: Int = 0,
    var followUser: List<FollowUserBean>? = null,
    var rtcInfo: FreedomSpeakRtc? = null,
    var isPublic: Boolean = false,
    var cityCode: String? = null,
    var enterCode: String? = null
)