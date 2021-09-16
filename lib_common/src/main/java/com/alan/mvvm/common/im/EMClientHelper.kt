package com.alan.mvvm.common.im

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.alan.mvvm.base.utils.ProcessUtils
import com.alan.mvvm.common.constant.Constants
import com.alan.mvvm.common.http.exception.BaseHttpException
import com.alan.mvvm.common.im.callkit.EaseCallKit
import com.alan.mvvm.common.im.callkit.base.EaseCallKitConfig
import com.alan.mvvm.common.im.callkit.base.EaseCallType
import com.alan.mvvm.common.im.callkit.base.EaseCallUserInfo
import com.alan.mvvm.common.im.callkit.utils.EaseCallKitNotifier
import com.alan.mvvm.common.im.listener.EMClientListener
import com.heytap.msp.push.HeytapPushManager
import com.hyphenate.EMCallBack
import com.hyphenate.chat.*
import com.hyphenate.push.EMPushConfig
import com.hyphenate.push.EMPushHelper
import com.hyphenate.push.EMPushType
import com.hyphenate.push.PushListener
import com.hyphenate.util.EMLog
import com.socks.library.KLog

/**
 * 作者：alan
 * 时间：2021/8/26
 * 备注：作为hyphenate-sdk的入口控制类，获取sdk下的基础类均通过此类
 */
object EMClientHelper {
    const val TAG: String = "RingIM"
    lateinit var notifier: EaseCallKitNotifier

    /**
     * 设置SDK是否初始化
     *
     * @param init
     */
    //SDK是否初始化
    var isSDKInit = false

    fun init(context: Context) {
        if (!ProcessUtils.isMainProcess(context)) {
            KLog.e(TAG, "非主进程")
            return
        }

        //初始化IM SDK
        if (isSDKInit) {
            return
        }
        // 根据项目需求对SDK进行配置
        val options = initChatOptions(context)
        EMClient.getInstance().init(context, options)
        EMClient.getInstance().setDebugMode(true)
        isSDKInit = true

        //初始化推送
        initPush(context)
        //callKit初始化
        initCallKit(context)

        //链接状态监听事件初始化
        EMClientListener.init()
    }

    /**
     * 根据自己的需要进行配置
     *
     * @param context
     * @return
     */
    fun initChatOptions(context: Context): EMOptions {
        Log.d(TAG, "init HuanXin Options")
        val options = EMOptions()
        // 设置是否自动接受加好友邀请,默认是true
        options.acceptInvitationAlways = false
        // 设置是否需要接受方已读确认
        options.requireAck = true
        // 设置是否需要接受方送达确认,默认false
        options.requireDeliveryAck = false
        /**
         * NOTE:你需要设置自己申请的账号来使用三方推送功能，详见集成文档
         */
        val builder = EMPushConfig.Builder(context)
        builder.enableVivoPush() // 需要在AndroidManifest.xml中配置appId和appKey
            .enableMiPush(Constants.MI_APPID, Constants.MI_APPKEY)
            .enableOppoPush(Constants.OPPO_APPKEY, Constants.OPPO_APPSECRET)
            .enableVivoPush()
            .enableHWPush() // 需要在AndroidManifest.xml中配置appId
        options.pushConfig = builder.build()
        val imServer = options.imServer
        val restServer = options.restServer

        // 设置是否允许聊天室owner离开并删除会话记录，意味着owner再不会受到任何消息
        options.allowChatroomOwnerLeave(false)
        // 设置退出(主动和被动退出)群组时是否删除聊天消息
        options.isDeleteMessagesAsExitGroup = false
        // 设置是否自动接受加群邀请
        options.isAutoAcceptGroupInvitation = false
        // 是否自动将消息附件上传到环信服务器，默认为True是使用环信服务器上传下载
        options.autoTransferMessageAttachments = true
        // 是否自动下载缩略图，默认是true为自动下载
        options.setAutoDownloadThumbnail(true)
        return options
    }

    /**
     * callKit初始化
     *
     * @param context
     */
    fun initCallKit(context: Context) {
        val callKitConfig = EaseCallKitConfig()
        //设置呼叫超时时间
        callKitConfig.callTimeOut = (30 * 1000).toLong()
        //设置声网AgoraAppId
        callKitConfig.setAgoraAppId(Constants.AGORA_ID)
        callKitConfig.isEnableRTCToken = true
        EaseCallKit.getInstance().init(context, callKitConfig)
    }

    fun startSingleVoiceCall(userId: String, ext: Map<String, String>) {
        EaseCallKit.getInstance().startSingleCall(EaseCallType.SINGLE_VOICE_CALL, userId, ext)
    }

    fun setUserInfoCallKit(userId: String, username: String?, avatar: String) {
        val userInfo = EaseCallUserInfo()
        userInfo.userId = userId
        userInfo.nickName = username
        userInfo.headImage = avatar
        EaseCallKit.getInstance().callKitConfig.setUserInfo(userId, userInfo)
    }

    fun initPush(context: Context?) {
        //初始化通知工具
        notifier = EaseCallKitNotifier(context)
        //OPPO SDK升级到2.1.0后需要进行初始化
        HeytapPushManager.init(context, true);
        //HMSPushHelper.getInstance().initHMSAgent(DemoApplication.getInstance());
        //仅支持小米、魅族、OPPO、VIVO
        EMPushHelper.getInstance().setPushListener(object : PushListener() {
            override fun onError(pushType: EMPushType?, errorCode: Long) {
                // TODO: 返回的errorCode仅9xx为环信内部错误，可从EMError中查询，其他错误请根据pushType去相应第三方推送网站查询。
                EMLog.e("PushClient", "Push client occur a error: " + pushType + " - " + errorCode);
            }

            override fun isSupportPush(
                pushType: EMPushType?,
                pushConfig: EMPushConfig?
            ): Boolean {
                return super.isSupportPush(pushType, pushConfig)
            }
        })
    }

    /**
     * oppo展示通知设置页面
     */
    fun showNotificationPermissionDialog() {
        val pushType = EMPushHelper.getInstance().pushType
        if (pushType == EMPushType.OPPOPUSH && HeytapPushManager.isSupportPush()) {
            HeytapPushManager.requestNotificationPermission();
        }
    }

    /**
     * 判断是否之前登录过
     *
     * @return
     */
    val isLoggedIn: Boolean
        get() = eMClient.isLoggedInBefore

    /**
     * 获取IM SDK的入口类
     *
     * @return
     */
    val eMClient: EMClient
        get() = EMClient.getInstance()

    /**
     * 获取联系人管理
     *
     * @return
     */
    val contactManager: EMContactManager
        get() = eMClient.contactManager()

    /**
     * 获取群组管理
     *
     * @return
     */
    val groupManager: EMGroupManager
        get() = eMClient.groupManager()

    /**
     * 获取聊天室管理
     *
     * @return
     */
    val chatroomManager: EMChatRoomManager
        get() = eMClient.chatroomManager()

    /**
     * 获取聊天管理
     *
     * @return
     */
    val chatManager: EMChatManager
        get() = eMClient.chatManager()

    /**
     * 获取推送管理
     *
     * @return
     */
    val pushManager: EMPushManager
        get() = eMClient.pushManager()


    /**
     * 获取当前用户
     */
    val currentUser: String
        get() = eMClient.currentUser


    /**
     * 获取某人的会话
     *
     * @param username
     * @param type
     * @param createIfNotExists
     * @return
     */
    fun getConversation(
        username: String?,
        type: EMConversation.EMConversationType?,
        createIfNotExists: Boolean
    ): EMConversation {
        return chatManager.getConversation(username, type, createIfNotExists)
    }


    /**
     * 获取会话列表
     * 按最后一次消息时间排序
     */
    fun getConversationList(): ArrayList<EMConversation> {
        val sortList = arrayListOf<EMConversation>()
        val conversations: Map<String, EMConversation> = chatManager.allConversations
        synchronized(conversations) {
            for (conversation in conversations.values) {
                if (conversation.allMessages.size != 0) {
                    sortList.add(conversation)
                }
            }
        }
        sortList.sortWith(compareBy({ it.lastMessage.msgTime }))
        sortList.reverse()
        return sortList;
    }


    /**
     * IM登录
     */
    fun loginEM(userId: String, ldResult: MutableLiveData<BaseHttpException>) {
        val password = userId + "122333"
        KLog.e(TAG, "IM账号：" + userId + "密码：" + password)
        EMClient.getInstance().login(userId, password, object : EMCallBack {
            override fun onSuccess() {
                KLog.e(TAG, "登录成功")
                groupManager.loadAllGroups();
                chatManager.loadAllConversations();
                ldResult.postValue(BaseHttpException(0, "", null))
            }

            override fun onError(code: Int, error: String) {
                KLog.e(TAG, code.toString() + "登录失败" + error)
                ldResult.postValue(BaseHttpException(code, error, null))
            }

            override fun onProgress(progress: Int, status: String) {}
        })
    }

    /**
     * IM退出登录
     * @param callback
     * 使用第三方推送时需要在退出登录时解绑设备 token
     */
    fun logoutEM(callback: EMCallBack?) {
        KLog.e(TAG, "IM退出登陆")
        EMClient.getInstance().logout(true, object : EMCallBack {
            override fun onSuccess() {
                KLog.e(TAG, "IM退出登陆成功")
                callback?.onSuccess()
            }

            override fun onProgress(progress: Int, status: String) {
                callback?.onProgress(progress, status)
            }

            override fun onError(code: Int, error: String) {
                KLog.e(TAG, "IM退出登陆失败")
                callback?.onError(code, error)
            }
        })
    }


}