package com.alan.module.chat.activity

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.PopupWindow
import android.widget.TextView
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.jpush.android.api.JPushInterface
import com.alan.module.chat.R
import com.alan.module.chat.adapter.ChatMessageAdapter
import com.alan.module.chat.databinding.ActivityChatBinding
import com.alan.module.chat.view.VoiceRecorderView
import com.alan.module.chat.viewmodol.ChatDetailViewModel
import com.alan.mvvm.base.coil.CoilUtils
import com.alan.mvvm.base.http.apiservice.HttpBaseUrlConstant
import com.alan.mvvm.base.http.responsebean.CallBean
import com.alan.mvvm.base.http.responsebean.MatchStatusBean
import com.alan.mvvm.base.http.responsebean.UserInfoBean
import com.alan.mvvm.base.ktx.*
import com.alan.mvvm.base.utils.*
import com.alan.mvvm.common.constant.Constants
import com.alan.mvvm.common.constant.IMConstant
import com.alan.mvvm.common.constant.RouteUrl
import com.alan.mvvm.common.db.entity.UserEntity
import com.alan.mvvm.common.helper.SpHelper
import com.alan.mvvm.common.im.EMClientHelper
import com.alan.mvvm.common.im.listener.ChatMsgListener
import com.alan.mvvm.common.im.listener.EMClientListener
import com.alan.mvvm.common.ui.BaseActivity
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.huantansheng.easyphotos.EasyPhotos
import com.huantansheng.easyphotos.models.album.entity.Photo
import com.hyphenate.EMValueCallBack
import com.hyphenate.chat.*
import com.hyphenate.chat.EMMessage
import com.hyphenate.exceptions.HyphenateException
import com.jaeger.library.StatusBarUtil
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException


/**
 * 作者：alan
 * 时间：2021/7/30
 * 备注：
 */
@Route(path = RouteUrl.ChatModule.ACTIVITY_CHAT_DETAIL)
@AndroidEntryPoint
class ChatActivity : BaseActivity<ActivityChatBinding, ChatDetailViewModel>() {

    @JvmField
    @Autowired
    var userId = ""

    var userName = ""
    var avatar = ""


    val CAMERA_CODE = 200
    lateinit var popupWindow: PopupWindow
    lateinit var userInfoBean: UserInfoBean
    lateinit var mAdapter: ChatMessageAdapter
    lateinit var conversation: EMConversation
    var recyclerViewLastHeight: Int = 0;
    var currentMessages: List<EMMessage>? = null
    var PAGE_SIZE = 20
    var isFirst: Boolean = true

    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<ChatDetailViewModel>()

    override fun setStatusBar() {
        StatusBarUtil.setColor(this, com.alan.mvvm.common.R.color.white.getResColor(), 0)
        getWindow()?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        mViewModel.ld_state.observe(this) {
            when {
                it == StateLayoutEnum.LOADING -> {
                    showDialog()
                }
                it == StateLayoutEnum.HIDE -> {
                    dismissDialog()
                }
                it == StateLayoutEnum.ERROR -> {
                }
                it == StateLayoutEnum.NO_DATA -> {
                }
            }
        }
    }

    /**
     * 初始化View
     */
    @SuppressLint("ClickableViewAccessibility")
    override fun ActivityChatBinding.initView() {

        initListener()

        initRV()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initListener() {
        mBinding.ivBack.clickDelay { finish() }
        mBinding.ivMore.clickDelay { showPopupWindow() }
        mBinding.tvInvite.clickDelay {
            when (mBinding.tvInvite.text) {
                "立即聊天" -> {
                    mViewModel.requestChatStart(userId)
                }
                "立即投喂" -> {

                }
                "立即关注" -> {
                    mViewModel.requestChangeFollow(userId, 1)
                }
            }
        }

        mBinding.tvOpen.clickDelay {
            JPushInterface.goToAppNotificationSettings(this@ChatActivity)
        }

        mBinding.ivCamera.clickDelay {
            jumpARoute(RouteUrl.ChatModule.ACTIVITY_CHAT_CAMERA, this@ChatActivity, CAMERA_CODE)
        }
        mBinding.ivVoice.clickDelay {
            if (mBinding.llPress.isVisible) {
                mBinding.ivVoice.setImageResource(R.drawable.icon_chat_voice)
                mBinding.llPress.gone()
            } else {
                mBinding.ivVoice.setImageResource(R.drawable.icon_chat_input)
                mBinding.llPress.visible()
            }
        }
        mBinding.ivCall.clickDelay {
            mViewModel.requestChatStart(userId)
        }
        mBinding.ivPic.clickDelay {
            ImageSelectUtil.singlePic(this@ChatActivity)
        }
        mBinding.ivGift.clickDelay {
            toast("即将上线")
        }


        mBinding.llPress.setOnTouchListener { v, event ->
            mBinding.rlRecording.onPressToSpeakBtnTouch(
                v,
                event,
                object : VoiceRecorderView.EaseVoiceRecorderCallback {
                    override fun onVoiceRecordComplete(
                        voiceFilePath: String?,
                        voiceTimeLength: Int
                    ) {
                        sendVoiceMessage(voiceFilePath, voiceTimeLength)
                    }
                })
            true
        }

        mBinding.etMsg.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEND ||
                event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN
            ) {
                val msg: String = mBinding.etMsg.getText().toString()
                mBinding.etMsg.setText("")
                sendTextMessage(msg)
                true
            } else {
                false
            }
        })
    }

    fun initRV() {
        val userEntity = EMClientHelper.getUserById(userId)


        mAdapter = ChatMessageAdapter(userEntity)
        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.iv_avatar -> {
                    val message = mAdapter.getItem(position)
                    val userId: String = message.from
                    val bundle = Bundle().apply {
                        putString("userId", userId)
                    }
                    jumpARoute(RouteUrl.HomeModule.ACTIVITY_HOME_MANAGER, bundle)
                }
                R.id.iv_pic -> {
                    val message = mAdapter.getItem(position).body as EMImageMessageBody
                    val imgUri: String = message.remoteUrl
                    val bundle = Bundle().apply {
                        putString("uri", imgUri)
                    }
                    jumpARoute(RouteUrl.ChatModule.ACTIVITY_CHAT_IMAGE, bundle)
                }
                R.id.iv_video -> {
                    val message = mAdapter.getItem(position).body as EMVideoMessageBody
                    val videoUri: String = message.getRemoteUrl()
                    val bundle = Bundle().apply {
                        putString("uri", videoUri)
                    }
                    jumpARoute(RouteUrl.ChatModule.ACTIVITY_CHAT_VIDEO, bundle)
                }
            }
        }
        mBinding.rvMsg.apply {
            addItemDecoration(
                MyColorDecoration(
                    0,
                    0,
                    0,
                    dp2px(10f),
                    R.color.transparent.getResColor()
                )
            )
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = mAdapter
        }
        mBinding.rvMsg.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {

                } else {
                    KeyBoardUtils.closeKeybord(mBinding.etMsg, this@ChatActivity)
                }
            }
        })
        mBinding.rvMsg.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val height: Int = mBinding.rvMsg.getHeight()
                if (recyclerViewLastHeight == 0) {
                    recyclerViewLastHeight = height
                }
                if (recyclerViewLastHeight != height) {
                    //RecyclerView高度发生变化，刷新页面
                    if (currentMessages != null) {
                        seekToPosition(currentMessages!!.size - 1)
                    }
                }
                recyclerViewLastHeight = height
            }
        })


        mBinding.srfList.setOnRefreshListener {
            loadMoreServerMessages(PAGE_SIZE, false)
        }
        mBinding.srfList.setEnableLoadMore(false)
    }


    /**
     * 检查通知权限
     */
    fun checkNotifi() {
        //是否有通知权限
        val notificationEnabled = JPushInterface.isNotificationEnabled(this@ChatActivity)
        if (notificationEnabled != 1) {
            //没开启通知权限
            mBinding.clNotifi.visible()
        } else {
            mBinding.clNotifi.gone()
        }
    }

    /**
     * 显示菜单项
     */
    fun showPopupWindow() {
        val contentview: View = LayoutInflater.from(this).inflate(R.layout.layout_more, null)
        contentview.isFocusable = true
        contentview.isFocusableInTouchMode = true
        val tv_focus = contentview.findViewById<TextView>(R.id.tv_focus)
        val tvReport = contentview.findViewById<TextView>(R.id.tv_report)
        val view_line = contentview.findViewById<View>(R.id.view_line)

        if (userInfoBean.followStatus == 0) {
            tv_focus.setText("关注")
        } else if (userInfoBean.followStatus == 1) {
            tv_focus.setText("已关注")
        } else if (userInfoBean.followStatus == 2) {
            tv_focus.setText("互相关注")
        }


        tvReport.clickDelay {
            popupWindow.dismiss()
            val bundle = Bundle().apply {
                putString(
                    "webUrl",
                    HttpBaseUrlConstant.BASE_URL + "&reportFromUserid=${SpHelper.getUserInfo()!!.userId}&reportToUserid=${userId}"
                )
                putString("webTitle", "举报")
            }
            jumpARoute(RouteUrl.WebModule.ACTIVITY_WEB_WEB, bundle)
        }
        tv_focus.clickDelay {
            popupWindow.dismiss()
            if (userInfoBean.followStatus == 0) {
                mViewModel.requestChangeFollow(userId, 1)
            } else {
                mViewModel.requestChangeFollow(userId, 0)
            }
        }

        popupWindow = PopupWindow(
            contentview,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        popupWindow.setFocusable(true)
        popupWindow.setOutsideTouchable(false)
        popupWindow.showAsDropDown(mBinding.ivMore, -dp2px(88f), 10)
    }


    /**
     * 订阅数据
     */
    override fun initObserve() {
        mViewModel.ldSuccess.observe(this) {
            when (it) {
                is UserInfoBean -> {
                    userInfoBean = it
                    mBinding.tvTitle.setText(userInfoBean.userName)
                    mBinding.tvTime.setText(userInfoBean.onlineStatusDesc)
                    CoilUtils.loadCircle(mBinding.ivAvatar, userInfoBean.avatar)
                    //加入数据库
                    userName = userInfoBean.userName
                    avatar = userInfoBean.avatar
                    EMClientHelper.saveUser(
                        UserEntity(
                            userId,
                            userInfoBean.userName,
                            userInfoBean.avatar
                        )
                    )
                }
                is MatchStatusBean -> {
                    if (it.inMatch) {
                        mBinding.tvInfo.setText("${userName}${it.orderStatus},和Ta聊聊吧！")
                        mBinding.tvInvite.setText("立即聊天")
                    } else {
                        if (it.isFollowed) {
                            mBinding.tvInfo.setText("${userName}正在等待投喂，投喂Ta周饭卡可开启语音聊天，边吃边聊")
                            mBinding.tvInvite.setText("立即投喂")
                        } else {
                            mBinding.tvInfo.setText("关注${userName}，Ta干饭时会给你发送通知")
                            mBinding.tvInvite.setText("立即关注")
                        }
                    }
                }
                is CallBean -> {
                    val userInfoBean = SpHelper.getUserInfo()
                    val map = mutableMapOf<String, String>().apply {
                        put("userId", userInfoBean?.userId!!)
                        put("userName", userInfoBean.userName)
                        put("avatar", userInfoBean.avatar)
                    }
                    EMClientHelper.setUserInfoCallKit(userId, userName, avatar)
                    EMClientHelper.startSingleVoiceCall(userId, map)
                }
            }
        }
    }

    /**
     * 获取数据
     */
    override fun initRequestData() {
        initConversation()

        mViewModel.requestUserInfo(userId)
        mViewModel.requestCheckMatch(userId)
    }


    /**
     * 初始化聊天对话
     */
    protected fun initConversation() {
        conversation = EMClientHelper.chatManager.getConversation(
            userId,
            EMConversation.EMConversationType.Chat,
            true
        )
        //会话已读回执
        conversation.markAllMessagesAsRead()
        try {
            EMClientHelper.chatManager.ackConversationRead(conversation.conversationId())
        } catch (e: HyphenateException) {
            e.printStackTrace()
        }
        loadMoreServerMessages(PAGE_SIZE, true)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                ImageSelectUtil.REQUESTCODE -> {
                    //照片的回调
                    val selectList =
                        data?.getParcelableArrayListExtra<Photo>(EasyPhotos.RESULT_PHOTOS)
                    val uri: Uri = selectList?.get(0)?.uri!!
                    sendImageMessage(uri)
                }
                CAMERA_CODE -> {
                    val type = data?.getIntExtra("type", Constants.TYPE_IMAGE)
                    when (type) {
                        Constants.TYPE_IMAGE -> {
                            val path = data.getStringExtra("bitmap")
                            sendImageMessage(path)

                        }
                        Constants.TYPE_VIDEO -> {
                            val path = data.getStringExtra("bitmap")
                            val url = data.getStringExtra("url")
                            var duration = 0
                            val player = MediaPlayer()
                            try {
                                player.setDataSource(url)
                                player.prepare()
                                duration = player.duration / 1000
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }

                            sendVideoMessage(url, path, if (duration == 0) 1 else duration)
                        }
                    }
                }
            }
        }
    }


    override fun onResume() {
        super.onResume()
        //判断是否是第一次或者有新的消息，如果有新的消息，则刷新到最近的一条消息
        if (isFirst || haveNewMessages()) {
            refreshToLatest()
        } else {
            refreshMessages()
        }
        isFirst = false
        registerListener()
        checkNotifi()
    }

    override fun onPause() {
        super.onPause()
    }


    /**
     * 消息监听
     */
    fun registerListener() {
        EMClientListener.instance.chatMsgListener = object : ChatMsgListener {
            override fun onMessageReceived(messages: List<EMMessage>) {
                refreshToLatest()
                for (msg in messages) {
                    sendReadAck(msg)
                }
            }

            override fun onCmdMessageReceived(messages: List<EMMessage>) {
            }

            override fun onMessageRead(messages: List<EMMessage>) {
                refreshMessages()
            }

            override fun onMessageDelivered(messages: List<EMMessage>) {
                refreshMessages()
            }

            override fun onMessageRecalled(messages: List<EMMessage>) {
                refreshMessages()
            }

            override fun onMessageChanged(message: EMMessage, change: Any) {
                refreshMessages()
            }
        }
    }



    /**
     * 从服务器加载更多数据
     * @param pageSize 一次加载的条数
     * @param moveToLast 是否移动到最后
     */
    fun loadMoreServerMessages(pageSize: Int, moveToLast: Boolean) {
        val count: Int = getCacheMessageCount()
        val msgId = if (moveToLast) "" else if (count > 0) conversation.allMessages[0].msgId else ""
        EMClientHelper.chatManager.asyncFetchHistoryMessage(userId,
            EMConversation.EMConversationType.Chat, pageSize, msgId,
            object : EMValueCallBack<EMCursorResult<EMMessage?>?> {
                override fun onSuccess(value: EMCursorResult<EMMessage?>?) {
                    mBinding.rvMsg.post(Runnable { loadMoreLocalMessages(pageSize, moveToLast) })
                }

                override fun onError(error: Int, errorMsg: String) {
                    mBinding.rvMsg.post(Runnable { loadMoreLocalMessages(pageSize, moveToLast) })
                }
            })
    }


    /**
     * 获取内存中消息数目
     * @return
     */
    protected fun getCacheMessageCount(): Int {
        val messageList = conversation.allMessages
        return messageList?.size ?: 0
    }

    /**
     * 加载更多的本地数据
     * @param pageSize
     * @param moveToLast
     */
    private fun loadMoreLocalMessages(pageSize: Int, moveToLast: Boolean) {
        val messageList = conversation.allMessages
        val msgCount = messageList?.size ?: 0
        val allMsgCount = conversation.allMsgCount
        if (msgCount < allMsgCount) {
            var msgId: String? = null
            if (msgCount > 0) {
                msgId = messageList!![0].msgId
            }
            var moreMsgs: List<EMMessage?>? = null
            try {
                moreMsgs = conversation.loadMoreMsgFromDB(msgId, pageSize)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            // 刷新数据，一则刷新数据，二则需要消息进行定位
            if (moreMsgs == null || moreMsgs.isEmpty()) {
                return
            }
            refreshMessages()
            // 对消息进行定位
            seekToPosition(if (moveToLast) conversation.allMessages.size - 1 else moreMsgs.size - 1)
        } else {
            finishRefresh()
            // 对消息进行定位
            if (moveToLast) {
                seekToPosition(conversation.allMessages.size - 1)
            } else {
                toast("没有更多的消息了")
            }
        }
    }

    fun refreshToLatest() {
        if (isActivityDisable()) {
            return
        }
        refreshMessages()
        seekToPosition(conversation.allMessages.size - 1)
    }

    /**
     * 刷新对话列表
     */
    fun refreshMessages() {
        if (isActivityDisable()) {
            return
        }
        runOnUiThread(Runnable {
            val messages = conversation.allMessages
            conversation.markAllMessagesAsRead()
            mAdapter.setList(messages)
            currentMessages = messages
            finishRefresh()
        })
    }


    /**
     * 移动到指定位置
     * @param position
     */
    private fun seekToPosition(toPosition: Int) {
        var position = toPosition;
        if (isActivityDisable()) {
            return
        }
        if (position < 0) {
            position = 0
        }
        val manager: RecyclerView.LayoutManager? = mBinding.rvMsg.layoutManager
        if (manager is LinearLayoutManager) {
            if (isActivityDisable()) {
                return
            }
            val finalPosition = position
            mBinding.rvMsg.post(Runnable { setMoveAnimation(manager, finalPosition) })
        }
    }


    /**
     * 移动动画
     */
    private fun setMoveAnimation(manager: RecyclerView.LayoutManager, position: Int) {
        val animator = ValueAnimator.ofInt(-200, 0)
        animator.addUpdateListener { animation ->
            val value = animation.animatedValue as Int
            (manager as LinearLayoutManager).scrollToPositionWithOffset(
                position,
                value
            )
        }
        animator.duration = 500
        animator.start()
    }

    /**
     * 停止刷新
     */
    private fun finishRefresh() {
        mBinding.srfList.finishRefresh()
    }

    /**
     * 是否有新的消息
     * 判断依据为：数据库中最新的一条数据的时间戳是否大于页面上的最新一条数据的时间戳
     * @return
     */
    fun haveNewMessages(): Boolean {
        return if (currentMessages == null || currentMessages!!.isEmpty() || conversation == null || conversation.lastMessage == null) {
            false
        } else conversation.lastMessage.msgTime > currentMessages!![currentMessages!!.size - 1].msgTime
    }


    /**
     * 判断当前activity是否不可用
     * @return
     */
    fun isActivityDisable(): Boolean {
        return this == null || isDestroyed() || isFinishing()
    }


    //==================================== 发送消息模块 start ======================================
    /**
     * 发送文本消息
     * @param content
     */
    protected fun sendTextMessage(content: String?) {
        val message = EMMessage.createTxtSendMessage(content, userId)
        sendMessage(message)
    }


    /**
     * 发送语音消息
     * @param filePath
     * @param length
     */
    protected fun sendVoiceMessage(filePath: String?, length: Int) {
        val message = EMMessage.createVoiceSendMessage(filePath, length, userId)
        sendMessage(message)
    }

    /**
     * 发送图片
     * @param imagePath
     */
    protected fun sendImageMessage(imagePath: String?) {
        val message = EMMessage.createImageSendMessage(imagePath, false, userId)
        sendMessage(message)
    }

    /**
     * 发送图片
     * @param imagePath
     */
    protected fun sendImageMessage(imageUri: Uri?) {
        val message = EMMessage.createImageSendMessage(imageUri, false, userId)
        sendMessage(message)
    }

    /**
     * 发送视频消息
     * @param videoPath
     * @param thumbPath
     * @param videoLength
     */
    protected fun sendVideoMessage(videoPath: String?, thumbPath: String?, videoLength: Int) {
        val message = EMMessage.createVideoSendMessage(videoPath, thumbPath, videoLength, userId)
        sendMessage(message)
    }

    /**
     * 发送视频消息
     * @param videoPath
     * @param thumbPath
     * @param videoLength
     */
    protected fun sendVideoMessage(videoUri: Uri?, thumbPath: String?, videoLength: Int) {
        val message = EMMessage.createVideoSendMessage(videoUri, thumbPath, videoLength, userId)
        sendMessage(message)
    }

    /**
     * 最终发送消息
     * @param message
     */
    protected fun sendMessage(message: EMMessage?) {
        if (message == null) {
            toast("请检查消息附件是否存在！")
            return
        }
        // 增加自己特定的属性
        message.setAttribute(IMConstant.MESSAGE_ATTR_AVATAR, SpHelper.getUserInfo()?.avatar);
        message.setAttribute(IMConstant.MESSAGE_ATTR_USERNAME, SpHelper.getUserInfo()?.userName);

        // 设置自定义扩展字段-强制推送
//        message.setAttribute(IMConstant.MESSAGE_ATTR_FORCEPUSH, true);
        // 设置自定义扩展字段-发送静默消息（不推送）
//        message.setAttribute(IMConstant.MESSAGE_ATTR_IGNOREPUSH, true);

        EMClientHelper.chatManager.sendMessage(message)
        refreshToLatest()
    }


    /**
     * 发送已读回执
     * @param message
     */
    fun sendReadAck(message: EMMessage) {
        //是接收的消息，未发送过read ack消息且是单聊
        if (message.direct() == EMMessage.Direct.RECEIVE && !message.isAcked
            && message.chatType == EMMessage.ChatType.Chat
        ) {
//            val type = message.type
//            //视频，语音及文件需要点击后再发送,这个可以根据需求进行调整
//            if (type == EMMessage.Type.VIDEO || type == EMMessage.Type.VOICE || type == EMMessage.Type.FILE) {
//                return
//            }
            try {
                EMClientHelper.chatManager.ackMessageRead(message.from, message.msgId)
            } catch (e: HyphenateException) {
                e.printStackTrace()
            }
        }
    }
//============================== 发送消息模块 end ==============================================


}