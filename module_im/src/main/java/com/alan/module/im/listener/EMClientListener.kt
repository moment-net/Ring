package com.alan.module.im.listener

import android.content.Context
import com.alan.module.im.EMClientHelper
import com.hyphenate.*
import com.hyphenate.chat.EMMessage
import com.hyphenate.chat.EMMucSharedFile
import com.hyphenate.easecallkit.EaseCallKit
import com.hyphenate.easecallkit.EaseCallKit.EaseCallError
import com.hyphenate.easecallkit.base.*
import com.hyphenate.util.EMLog
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
object EMClientListener {
    const val TAG: String = "IM"

    init {
        //添加网络连接状态监听
        EMClientHelper.eMClient.addConnectionListener(ChatConnectionListener())
        //添加多端登录监听
        EMClientHelper.eMClient.addMultiDeviceListener(ChatMultiDeviceListener())
        //添加消息接受监听
        EMClientHelper.chatManager.addMessageListener(ChatMessageListener())
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
     * 将需要登录成功进入MainActivity中初始化的逻辑，放到此处进行处理
     */
    fun init() {}


    /**
     * IM消息监听
     */
    class ChatMessageListener : EMMessageListener {
        override fun onMessageReceived(messages: List<EMMessage>) {
            for (message in messages) {
                KLog.e(TAG, "收到IM消息$message")
            }
        }

        override fun onCmdMessageReceived(list: List<EMMessage>) {}
        override fun onMessageRead(list: List<EMMessage>) {}
        override fun onMessageDelivered(list: List<EMMessage>) {}
        override fun onMessageRecalled(list: List<EMMessage>) {}
        override fun onMessageChanged(emMessage: EMMessage, o: Any) {}
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
            //            LiveDataBus.get().with(BusConstant.ACCOUNT_CHANGE).postValue(new BusEvent(BusConstant.ACCOUNT_CONNECTION, BusEvent.TYPE.ACCOUNT));
        }

        /**
         * 用来监听账号异常
         *
         * @param error
         */
        override fun onDisconnected(error: Int) {
            KLog.e(TAG, "环信断开连接onDisconnected =$error")
            val event: String? = null
            //            if (error == EMError.USER_REMOVED) {
//                event = BusConstant.ACCOUNT_REMOVED;
//            } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
//                event = BusConstant.ACCOUNT_CONFLICT;
//            } else if (error == EMError.SERVER_SERVICE_RESTRICTED) {
//                event = BusConstant.ACCOUNT_FORBIDDEN;
//            } else if (error == EMError.USER_KICKED_BY_CHANGE_PASSWORD) {
//                event = BusConstant.ACCOUNT_KICKED_BY_CHANGE_PASSWORD;
//            } else if (error == EMError.USER_KICKED_BY_OTHER_DEVICE) {
//                event = BusConstant.ACCOUNT_KICKED_BY_OTHER_DEVICE;
//            }
//            if (!TextUtils.isEmpty(event)) {
//                LiveDataBus.get().with(BusConstant.ACCOUNT_CHANGE).postValue(new BusEvent(event, BusEvent.TYPE.ACCOUNT));
//            } else {
//                LiveDataBus.get().with(BusConstant.ACCOUNT_CHANGE).postValue(new BusEvent(BusConstant.ACCOUNT_DISCONNECT, BusEvent.TYPE.ACCOUNT));
//            }
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
        override fun onInviteUsers(context: Context, userId: Array<String>, ext: JSONObject) {
//                Intent intent = new Intent(context, ConferenceInviteActivity.class).addFlags(FLAG_ACTIVITY_NEW_TASK);
//                String groupId = null;
//                if(ext != null && ext.length() > 0){
//                    try {
//                        groupId = ext.getString("groupId");
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//                intent.putExtra(IMConstant.EXTRA_CONFERENCE_GROUP_ID, groupId);
//                intent.putExtra(IMConstant.EXTRA_CONFERENCE_GROUP_EXIST_MEMBERS, userId);
//                context.startActivity(intent);
        }

        override fun onEndCallWithReason(
            callType: EaseCallType,
            channelName: String,
            reason: EaseCallEndReason,
            callTime: Long
        ) {
            EMLog.d(
                TAG,
                "onEndCallWithReason" + callType.name + " reason:" + reason + " time:" + callTime
            )
            val formatter = SimpleDateFormat("mm:ss")
            formatter.timeZone = TimeZone.getTimeZone("UTC")
            var callString: String? = "通话时长 "
            callString += formatter.format(callTime)
        }

        override fun onGenerateToken(
            userId: String,
            channelName: String,
            appKey: String,
            callback: EaseCallKitTokenCallback
        ) {
            EMLog.d(TAG, "onGenerateToken userId:$userId channelName:$channelName appKey:$appKey")
            //            String url = tokenUrl;
//            url += "?";
//            url += "userAccount=";
//            url += userId;
//            url += "&channelName=";
//            url += channelName;
//            url += "&appkey=";
//            url += appKey;
//            private String tokenUrl = "http://a1.easemob.com/token/rtcToken/v1";
//            private String uIdUrl = "http://a1.easemob.com/channel/mapper";
            //获取声网Token
//                getRtcToken(url, callback);
        }

        override fun onReceivedCall(callType: EaseCallType, fromUserId: String, ext: JSONObject) {
            //收到接听电话
            EMLog.d(TAG, "onRecivedCall" + callType.name + " fromUserId:" + fromUserId)
        }

        override fun onCallError(type: EaseCallError, errorCode: Int, description: String) {}
        override fun onInViteCallMessageSent() {}
        override fun onRemoteUserJoinChannel(
            channelName: String,
            userName: String,
            uid: Int,
            callback: EaseGetUserAccountCallback
        ) {
            if (userName == null || userName === "") {
//                String url = uIdUrl;
//                url += "?";
//                url += "channelName=";
//                url += channelName;
//                url += "&userAccount=";
//                url += EMClient.getInstance().getCurrentUser();
//                url += "&appkey=";
//                url += EMClient.getInstance().getOptions().getAppKey();
                //获取所有人的uid
//                    getUserIdAgoraUid(uid, url, callback);
            } else {
                //设置用户昵称 头像
            }
        }
    }


}