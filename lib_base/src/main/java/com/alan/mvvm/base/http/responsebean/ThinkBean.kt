package com.alan.mvvm.base.http.responsebean

/**
{
"id": 6,
"user": {
"id": 260,
"userId": 1379482826965024,
"userName": "新的爽爷想改名字了啊",
"gender": 1,
"avatar": "http://heartmusicavatar.oss-cn-beijing.aliyuncs.com/avatar/1379482826965024b2e99e74-705d-42ad-97f3-c3b828d43bbc.png?x-oss-process=image/resize,m_fixed,h_160,w_160",
"desc": "你好呀我有两只猫",
"followStatus": 0,
"bindPhone": null,
"bindWeChat": null,
"bindApple": null,
"hometown": "东城区",
"address": "东城区",
"birthday": "2002-6-20",
"starSign": "双子座",
"age": 19,
"onlineStatus": true,
"audioDesc": {
"duration": 7100,
"audioName": "1383152756981792efb82673-6166-410c-a773-9f41f386c8f3.wav",
"audioPath": "http://freedomspeak.oss-cn-beijing.aliyuncs.com/audioDesc/1383152756981792efb82673-6166-410c-a773-9f41f386c8f3.wav"
},
"greeting": {
"duration": 7100,
"audioName": "1383152756981792efb82673-6166-410c-a773-9f41f386c8f3.wav",
"audioPath": "http://freedomspeak.oss-cn-beijing.aliyuncs.com/greeting/1383152756981792efb82673-6166-410c-a773-9f41f386c8f3.wav"
},
"onlineStatusDesc": "离线",
"onlineRoom": null,
"followCount": 29,
"fansCount": 18,
"needInvite": 0,
"type": 101,
"hasHeadset": true,
"eduRight": 1
},
"tag": "想法",
"content": "求求了带我上分",
"createTime": "2021-10-13T16:34:35",
"favoriteCount": 0,
"commentCount": null,
"isFavorite": false
}
 */
data class ThinkBean(
    var id: String,
    var user: UserInfoBean,
    var tag: String,
    var content: String,
    var createTime: String,
    var favoriteCount: Int,
    var commentCount: String,
    var audio: String,
    var audioDuration: Int,
    var isFavorite: Boolean,
    var pic: ArrayList<PicBean>
)
