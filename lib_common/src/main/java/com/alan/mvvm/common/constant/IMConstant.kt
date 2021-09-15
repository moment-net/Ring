package com.alan.mvvm.common.constant

import com.hyphenate.util.PathUtil

object IMConstant {


    //语音聊天地址
    var PATH_VOICE =
        "${PathUtil.getInstance().voicePath.toString()}/${System.currentTimeMillis()}.amr"

    //拍摄视频地址
    var PATH_VEDIO = PathUtil.getInstance().videoPath.toString()


    //收到推送是否为音视频通话推送
    @JvmField
    var isRtcCall = false;

    @JvmField
    var type = 0;


    //头像
    const val MESSAGE_ATTR_AVATAR = "avatar"

    //名字
    const val MESSAGE_ATTR_USERNAME = "userName"

    //对方的头像
    const val MESSAGE_ATTR_AVATAR_OTHER = "toAvatar"

    //对方的名字
    const val MESSAGE_ATTR_USERNAME_OTHER = "toUserName"

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
    const val EVENT_TYPE_MESSAGE = "chatmessage"
    const val EVENT_TYPE_CONNECTION = "chatconnection"

    //EventBus事件-事件
    const val EVENT_EVENT_CHANGE = "change"


    const val ACCOUNT_CHANGE = "account_change"
    const val ACCOUNT_REMOVED = "account_removed"
    const val ACCOUNT_CONFLICT = "conflict"
    const val ACCOUNT_FORBIDDEN = "user_forbidden"
    const val ACCOUNT_KICKED_BY_CHANGE_PASSWORD = "kicked_by_change_password"
    const val ACCOUNT_KICKED_BY_OTHER_DEVICE = "kicked_by_another_device"

    const val EXTRA_CONFERENCE_ID = "confId"
    const val EXTRA_CONFERENCE_PASS = "password"
    const val EXTRA_CONFERENCE_INVITER = "inviter"
    const val EXTRA_CONFERENCE_IS_CREATOR = "is_creator"
    const val EXTRA_CONFERENCE_GROUP_ID = "group_id"
    const val EXTRA_CONFERENCE_GROUP_EXIST_MEMBERS = "exist_members"

    const val OP_INVITE = "invite"
    const val OP_REQUEST_TOBE_SPEAKER = "request_tobe_speaker"
    const val OP_REQUEST_TOBE_AUDIENCE = "request_tobe_audience"

    const val EM_CONFERENCE_OP = "em_conference_op"
    const val EM_CONFERENCE_ID = "em_conference_id"
    const val EM_CONFERENCE_PASSWORD = "em_conference_password"
    const val EM_CONFERENCE_TYPE = "em_conference_type"
    const val EM_MEMBER_NAME = "em_member_name"
    const val EM_NOTIFICATION_TYPE = "em_notification_type"

    const val MSG_ATTR_CONF_ID = "conferenceId"
    const val MSG_ATTR_CONF_PASS = EXTRA_CONFERENCE_PASS
    const val MSG_ATTR_EXTENSION = "msg_extension"

    const val NEW_FRIENDS_USERNAME = "item_new_friends"
    const val GROUP_USERNAME = "item_groups"
    const val CHAT_ROOM = "item_chatroom"
    const val CHAT_ROBOT = "item_robots"

    const val NOTIFY_GROUP_INVITE_RECEIVE = "invite_receive"
    const val NOTIFY_GROUP_INVITE_ACCEPTED = "invite_accepted"
    const val NOTIFY_GROUP_INVITE_DECLINED = "invite_declined"
    const val NOTIFY_GROUP_JOIN_RECEIVE = "invite_join_receive"
    const val NOTIFY_CHANGE = "notify_change"

    const val MESSAGE_GROUP_JOIN_ACCEPTED = "message_join_accepted"
    const val MESSAGE_GROUP_AUTO_ACCEPT = "message_auto_accept"

    const val CONTACT_REMOVE = "contact_remove"
    const val CONTACT_ACCEPT = "contact_accept"
    const val CONTACT_DECLINE = "contact_decline"
    const val CONTACT_BAN = "contact_ban"
    const val CONTACT_ALLOW = "contact_allow"

    const val CONTACT_CHANGE = "contact_change"
    const val CONTACT_ADD = "contact_add"
    const val CONTACT_DELETE = "contact_delete"
    const val CONTACT_UPDATE = "contact_update"
    const val NICK_NAME_CHANGE = "nick_name_change"
    const val AVATAR_CHANGE = "avatar_change"
    const val REMOVE_BLACK = "remove_black"

    const val GROUP_CHANGE = "group_change"
    const val GROUP_OWNER_TRANSFER = "group_owner_transfer"
    const val GROUP_SHARE_FILE_CHANGE = "group_share_file_change"

    const val CHAT_ROOM_CHANGE = "chat_room_change"
    const val CHAT_ROOM_DESTROY = "chat_room_destroy"

    const val REFRESH_NICKNAME = "refresh_nickname"

    const val CONVERSATION_DELETE = "conversation_delete"
    const val CONVERSATION_READ = "conversation_read"

    const val MESSAGE_NOT_SEND = "message_not_send"

    const val SYSTEM_MESSAGE_FROM = "from"
    const val SYSTEM_MESSAGE_REASON = "reason"
    const val SYSTEM_MESSAGE_STATUS = "status"
    const val SYSTEM_MESSAGE_GROUP_ID = "groupId"
    const val SYSTEM_MESSAGE_NAME = "name"
    const val SYSTEM_MESSAGE_INVITER = "inviter"

    const val USER_CARD_EVENT = "userCard"
    const val USER_CARD_ID = "uid"
    const val USER_CARD_NICK = "nickname"
    const val USER_CARD_AVATAR = "avatar"
}