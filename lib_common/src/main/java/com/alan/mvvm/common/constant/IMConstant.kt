package com.alan.mvvm.common.constant

import com.hyphenate.util.PathUtil

object IMConstant {


    //语音聊天地址
    var PATH_VOICE =
        "${PathUtil.getInstance().voicePath.absolutePath}/${System.currentTimeMillis()}.amr"

    //拍摄视频地址
    var PATH_VEDIO = PathUtil.getInstance().videoPath.absolutePath


    //收到推送是否为音视频通话推送
    @JvmField
    var isRtcCall = false;

    @JvmField
    var type = 0;


    //头像
    const val MESSAGE_ATTR_AVATAR = "avatar"
    //名字
    const val MESSAGE_ATTR_USERNAME = "userName"


    //强制推送通知和发送静默消息（不推送）
    const val MESSAGE_ATTR_FORCEPUSH = "em_force_notification"
    const val MESSAGE_ATTR_IGNOREPUSH = "em_ignore_notification"

    //是否是语音通话
    const val MESSAGE_ATTR_IS_VOICE_CALL = "is_voice_call"
    //是否是视频通话
    const val MESSAGE_ATTR_IS_VIDEO_CALL = "is_video_call"


    //消息类型-文字
    const val MESSAGE_TYPE_TXT_LEFT = 1
    const val MESSAGE_TYPE_TXT_RIGHT = 2

    //消息类型-图片
    const val MESSAGE_TYPE_IMAGE_LEFT = 3
    const val MESSAGE_TYPE_IMAGE_RIGHT = 4

    //消息类型-语音
    const val MESSAGE_TYPE_VOICE_LEFT = 5
    const val MESSAGE_TYPE_VOICE_RIGHT = 6

    //消息类型-视频
    const val MESSAGE_TYPE_VIDEO_LEFT = 7
    const val MESSAGE_TYPE_VIDEO_RIGHT = 8

    //消息类型-文件
    const val MESSAGE_TYPE_FILE_LEFT = 9
    const val MESSAGE_TYPE_FILE_RIGHT = 10

    //消息类型-自定义
    const val MESSAGE_TYPE_CUSTOM_LEFT = 11
    const val MESSAGE_TYPE_CUSTOM_RIGHT = 12

    //消息类型-文字-语音聊天
    const val MESSAGE_TYPE_VOICECALL_LEFT = 13
    const val MESSAGE_TYPE_VOICECALL_RIGHT = 14

    //消息类型-文字-视频聊天
    const val MESSAGE_TYPE_VIDEOCALL_LEFT = 15
    const val MESSAGE_TYPE_VIDEOCALL_RIGHT = 16


    //EventBus事件-类型
    //消息
    const val EVENT_TYPE_MESSAGE = "chatmessage"

    //IM连接状态
    const val EVENT_TYPE_CONNECTION = "chatconnection"


    //EventBus事件-事件
    //有新消息
    const val EVENT_EVENT_CHANGE = "change"


    //服务器长链消息key
    const val MESSAGE_KEY_COMMOND = "command"
    const val MESSAGE_KEY_DATA = "data"

    //匹配成功,等待聊天
    const val MESSAGE_COMMOND_MATCH_SUCCESS = 300100

    //发起聊天，等待加入
    const val MESSAGE_COMMOND_LAUNCH = 300210

    //加入完毕，开始聊天
    const val MESSAGE_COMMOND_JOINED = 300211

    //聊天结束
    const val MESSAGE_COMMOND_HANGUP = 300212


}