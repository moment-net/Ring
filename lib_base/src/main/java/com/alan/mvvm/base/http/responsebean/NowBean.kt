package com.alan.mvvm.base.http.responsebean

/**
 * {
"id": 6,
"user": {
"id": 69,
"userId": 1371664453468192,
"userName": "最好的水蜜面膜是",
"gender": 1,
"avatar": "http://heartmusicavatar.oss-cn-beijing.aliyuncs.com/avatar/1371664453468192777624ba-30ca-4f1b-b0bb-93c3c5ae925b.png?x-oss-process=image/resize,m_fixed,h_160,w_160",
"desc": "这个人很懒，还没有填写任何签名",
"followStatus": 0,
"bindPhone": null,
"bindWeChat": null,
"bindApple": null,
"hometown": "崇文区",
"address": "东城区",
"birthday": "",
"starSign": "",
"age": null,
"onlineStatus": false,
"audioDesc": null,
"greeting": null,
"onlineStatusDesc": "离线",
"onlineRoom": null,
"followCount": 38,
"fansCount": 29,
"needInvite": 0,
"type": 1,
"hasHeadset": true,
"eduRight": 0
},
"tag": "其他",
"tagPicUrl": "http://freedomspeak.oss-cn-beijing.aliyuncs.com/app/resource/icon/5.png",
"content": "写代码啊写代码",
"createTime": "2021-10-14T16:35:51",
"textColor": "#46DD93", //tag字体颜色
"textOpacity": "1.0", //字体透明度
"bgColor": "#46DD93", //背景颜色
"bgOpacity": "0.1", //背景透明度
"createTimeDesc": "14分钟前发布"
}
增加一个自定义字段为了显示隐藏输入框
 */
data class NowBean(
    val id: String,
    val user: UserInfoBean,
    val tag: String,
    val tagPicUrl: String,
    val content: String,
    val createTime: String,
    val textColor: String,
    val textOpacity: Float,
    val bgColor: String,
    val bgOpacity: Float,
    var isShow: Boolean = false,
    val createTimeDesc: String,
    var pic: ArrayList<PicBean>
)
