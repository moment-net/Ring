package com.alan.mvvm.common.im.listener

import android.content.Context
import com.alan.mvvm.base.utils.ActivityStackManager
import com.alan.mvvm.base.utils.EventBusUtils
import com.alan.mvvm.common.constant.IMConstant
import com.alan.mvvm.common.db.entity.UserEntity
import com.alan.mvvm.common.event.MessageEvent
import com.alan.mvvm.common.im.EMClientHelper
import com.alan.mvvm.common.im.callkit.EaseCallKit
import com.alan.mvvm.common.im.callkit.base.*
import com.hyphenate.*
import com.hyphenate.chat.EMMessage
import com.hyphenate.chat.EMMucSharedFile
import com.socks.library.KLog
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

/**
 * 作者：alan
 * 时间：2021/1/29
 * 备注： * 主要用于chat过程中的全局监听，并对相应的事件进行处理
 * * [.init]方法建议在登录成功以后进行调用
 */
class EMClientListener private constructor() {
    //聊天页面监听
    var chatMsgListener: ChatMsgListener? = null

    companion object {
        const val TAG: String = "RingIM"

        val instance: EMClientListener by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            EMClientListener()
        }
    }


    /**
     * 将需要登录成功进入MainActivity中初始化的逻辑，放到此处进行处理
     */
    fun init() {
        //添加网络连接状态监听
        EMClientHelper.eMClient.addConnectionListener(ChatConnectionListener())
        //添加多端登录监听
        EMClientHelper.eMClient.addMultiDeviceListener(ChatMultiDeviceListener())
        //添加消息接受监听
        EMClientHelper.chatManager.addMessageListener(ChatMessageListener())
        //添加会话监听
        EMClientHelper.chatManager.addConversationListener(ChatConversationListener())
        //添加群组监听
        EMClientHelper.groupManager.addGroupChangeListener(ChatGroupListener())
        //添加联系人监听
        EMClientHelper.contactManager.setContactListener(ChatContactListener())
        //添加聊天室监听
        EMClientHelper.chatroomManager.addChatRoomChangeListener(ChatRoomListener())
        //添加聊天室监听
        EaseCallKit.getInstance().setCallKitListener(ChatCallListener())
    }


    /**
     * IM消息监听
     */
    inner class ChatMessageListener : EMMessageListener {
        override fun onMessageReceived(messages: List<EMMessage>) {
            if (chatMsgListener != null) {
                chatMsgListener?.onMessageReceived(messages)
            }
            EventBusUtils.postEvent(
                MessageEvent(
                    IMConstant.EVENT_TYPE_MESSAGE,
                    IMConstant.EVENT_EVENT_CHANGE
                )
            )
            for (message in messages) {
                KLog.e(TAG, "收到IM消息$message")
                KLog.e(TAG, "onMessageReceived id : " + message.msgId)
                KLog.e(TAG, "onMessageReceived: " + message.type)

                //每收到一个消息进行保存用户信息
                val userName = message.getStringAttribute(IMConstant.MESSAGE_ATTR_USERNAME, "")
                val avatar = message.getStringAttribute(IMConstant.MESSAGE_ATTR_AVATAR, "")
                EMClientHelper.saveUser(UserEntity(message.from, userName, avatar))


                // 如果设置群组离线消息免打扰，则不进行消息通知
//                val disabledIds: List<String> = EMClientHelper.pushManager.getNoPushGroups()
//                if (disabledIds.contains(message.conversationId())) {
//                    return
//                }
                //后台通知有新的消息
                if (!ActivityStackManager.isFront()) {
                    EMClientHelper.notifier.notify(message)
                }
                //通知有新的消息
                EMClientHelper.notifier.vibrateAndPlayTone(message)
            }
        }

        override fun onCmdMessageReceived(list: List<EMMessage>) {
            if (chatMsgListener != null) {
                chatMsgListener?.onCmdMessageReceived(list)
            }
        }

        override fun onMessageRead(list: List<EMMessage>) {
            if (chatMsgListener != null) {
                chatMsgListener?.onMessageRead(list)
            }
            EventBusUtils.postEvent(
                MessageEvent(
                    IMConstant.EVENT_TYPE_MESSAGE,
                    IMConstant.EVENT_EVENT_CHANGE
                )
            )
        }

        override fun onMessageDelivered(list: List<EMMessage>) {
            if (chatMsgListener != null) {
                chatMsgListener?.onMessageDelivered(list)
            }
        }

        override fun onMessageRecalled(list: List<EMMessage>) {
            if (chatMsgListener != null) {
                chatMsgListener?.onMessageRecalled(list)
            }
        }

        override fun onMessageChanged(emMessage: EMMessage, o: Any) {
            if (chatMsgListener != null) {
                chatMsgListener?.onMessageChanged(emMessage, o)
            }
        }
    }

    /**
     * 会话监听
     */
    class ChatConversationListener : EMConversationListener {
        override fun onCoversationUpdate() {

        }

        override fun onConversationRead(from: String?, to: String?) {
            EventBusUtils.postEvent(
                MessageEvent(
                    IMConstant.EVENT_TYPE_MESSAGE,
                    IMConstant.EVENT_EVENT_CHANGE
                )
            )
        }
    }

    /**
     * IM连接监听
     */
    class ChatConnectionListener : EMConnectionListener {
        override fun onConnected() {
            KLog.e(TAG, "环信连接onConnected")
            if (!EMClientHelper.isLoggedIn) {
                return
            }
            EventBusUtils.postEvent(
                MessageEvent(
                    IMConstant.EVENT_TYPE_MESSAGE,
                    IMConstant.EVENT_EVENT_CHANGE
                )
            )
        }

        /**
         * 用来监听账号异常
         *
         * @param error
         */
        override fun onDisconnected(error: Int) {
            KLog.e(TAG, "环信断开连接onDisconnected =$error")
            EventBusUtils.postEvent(MessageEvent(IMConstant.EVENT_TYPE_CONNECTION))
        }
    }

    /**
     * 群聊监听
     */
    class ChatGroupListener : EMGroupChangeListener {
        override fun onInvitationReceived(s: String, s1: String, s2: String, s3: String) {}
        override fun onRequestToJoinReceived(s: String, s1: String, s2: String, s3: String) {}
        override fun onRequestToJoinAccepted(s: String, s1: String, s2: String) {}
        override fun onRequestToJoinDeclined(s: String, s1: String, s2: String, s3: String) {}
        override fun onInvitationAccepted(s: String, s1: String, s2: String) {}
        override fun onInvitationDeclined(s: String, s1: String, s2: String) {}
        override fun onUserRemoved(s: String, s1: String) {}
        override fun onGroupDestroyed(s: String, s1: String) {}
        override fun onAutoAcceptInvitationFromGroup(s: String, s1: String, s2: String) {}
        override fun onMuteListAdded(s: String, list: List<String>, l: Long) {}
        override fun onMuteListRemoved(s: String, list: List<String>) {}
        override fun onWhiteListAdded(s: String, list: List<String>) {}
        override fun onWhiteListRemoved(s: String, list: List<String>) {}
        override fun onAllMemberMuteStateChanged(s: String, b: Boolean) {}
        override fun onAdminAdded(s: String, s1: String) {}
        override fun onAdminRemoved(s: String, s1: String) {}
        override fun onOwnerChanged(s: String, s1: String, s2: String) {}
        override fun onMemberJoined(s: String, s1: String) {}
        override fun onMemberExited(s: String, s1: String) {}
        override fun onAnnouncementChanged(s: String, s1: String) {}
        override fun onSharedFileAdded(s: String, emMucSharedFile: EMMucSharedFile) {}
        override fun onSharedFileDeleted(s: String, s1: String) {}
    }

    /**
     * 联系人监听
     */
    class ChatContactListener : EMContactListener {
        override fun onContactAdded(s: String) {}
        override fun onContactDeleted(s: String) {}
        override fun onContactInvited(s: String, s1: String) {}
        override fun onFriendRequestAccepted(s: String) {}
        override fun onFriendRequestDeclined(s: String) {}
    }

    /**
     * 多设备登陆监听
     */
    class ChatMultiDeviceListener : EMMultiDeviceListener {
        override fun onContactEvent(event: Int, target: String, ext: String) {}
        override fun onGroupEvent(event: Int, groupId: String, usernames: List<String>) {}
    }

    /**
     * 聊天室监听
     */
    class ChatRoomListener : EMChatRoomChangeListener {
        override fun onChatRoomDestroyed(s: String, s1: String) {
            KLog.e(TAG, "聊天室销毁$s$s1")
        }

        override fun onMemberJoined(s: String, s1: String) {
            KLog.e(TAG, "聊天室成员加入$s$s1")
        }

        override fun onMemberExited(s: String, s1: String, s2: String) {
            KLog.e(TAG, "聊天室成员退出$s$s1$s2")
        }

        override fun onRemovedFromChatRoom(i: Int, s: String, s1: String, s2: String) {
            KLog.e(TAG, "聊天室成员踢出$s$s1$s2")
        }

        override fun onMuteListAdded(s: String, list: List<String>, l: Long) {}
        override fun onMuteListRemoved(s: String, list: List<String>) {}
        override fun onWhiteListAdded(s: String, list: List<String>) {}
        override fun onWhiteListRemoved(s: String, list: List<String>) {}
        override fun onAllMemberMuteStateChanged(s: String, b: Boolean) {}
        override fun onAdminAdded(s: String, s1: String) {}
        override fun onAdminRemoved(s: String, s1: String) {}
        override fun onOwnerChanged(s: String, s1: String, s2: String) {}
        override fun onAnnouncementChanged(s: String, s1: String) {}
    }

    /**
     * 语音视频电话监听
     */
    class ChatCallListener : EaseCallKitListener {
        //多人通话中邀请
        override fun onInviteUsers(context: Context, userId: Array<String>, ext: JSONObject) {}

        //通话结束
        override fun onEndCallWithReason(
            callType: EaseCallType,
            channelName: String,
            reason: EaseCallEndReason,
            callTime: Long
        ) {
            KLog.e(
                TAG,
                "onEndCallWithReason" + callType.name + " reason:" + reason + " time:" + callTime
            )
            val formatter = SimpleDateFormat("mm:ss")
            formatter.timeZone = TimeZone.getTimeZone("UTC")
            var callString: String? = "通话时长 "
            callString += formatter.format(callTime)
        }

        //获取声网token
        override fun onGenerateToken(
            userId: String,
            channelName: String,
            appKey: String,
            callback: EaseCallKitTokenCallback
        ) {
            KLog.e(TAG, "onGenerateToken userId:$userId channelName:$channelName appKey:$appKey")
            callback.onSetToken(null, 0)
//            requestRtcToken(channelName, callback)
        }

        //被叫收到通话邀请
        override fun onReceivedCall(callType: EaseCallType, fromUserId: String, ext: JSONObject?) {
            //收到接听电话
//            KLog.e(TAG, "onRecivedCall" + callType.name + " fromUserId:" + fromUserId)
//            if (!TextUtils.isEmpty(ext.toString())) {
//                //设置用户昵称 头像
//                val userId = ext?.optString("userId")
//                val userName = ext?.optString("userName")
//                val avatar = ext?.optString("avatar")
//                EMClientHelper.setUserInfoCallKit(userId!!, userName, avatar!!)
//            }
        }

        //通话异常回调
        override fun onCallError(
            type: EaseCallKit.EaseCallError,
            errorCode: Int,
            description: String
        ) {
        }

        //通话邀请消息回调
        override fun onInViteCallMessageSent() {}

        //远端用户加入频道成功回调
        override fun onRemoteUserJoinChannel(
            channelName: String,
            userName: String?,
            uid: Int,
            callback: EaseGetUserAccountCallback
        ) {
            val account = EaseUserAccount(uid, userName)
            val accounts: MutableList<EaseUserAccount> = ArrayList()
            accounts.add(account)
            callback.onUserAccount(accounts)
        }
    }


    fun requestRtcToken(
        channelName: String,
        callback: EaseCallKitTokenCallback
    ) {
//        val requestBean = ChannelNameRequestBean(channelName)
//        CoroutineScope(Dispatchers.IO).launch {
//            .requestRtcToken(
//                RequestUtil.getPostBody(requestBean), callback = RequestCallback(
//                    onSuccess = {
//                        callback.onSetToken(it.data.rtcToken, it.data.uid)
//                    },
//                    onFailed = {
//                        callback.onGetTokenError(it.errorCode, it.errorMessage)
//                    }
//                ))
//        }
    }

}