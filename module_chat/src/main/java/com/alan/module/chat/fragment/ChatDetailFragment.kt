package com.alan.module.chat.fragment

import android.animation.ValueAnimator
import android.content.Context
import android.os.Bundle
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alan.module.chat.R
import com.alan.module.chat.adapter.ChatMessageAdapter
import com.alan.module.chat.databinding.FragmentChatDetailBinding
import com.alan.module.chat.viewmodol.ChatViewModel
import com.alan.mvvm.base.ktx.dp2px
import com.alan.mvvm.base.ktx.getResColor
import com.alan.mvvm.base.utils.EventBusUtils
import com.alan.mvvm.base.utils.MyColorDecoration
import com.alan.mvvm.base.utils.jumpARoute
import com.alan.mvvm.base.utils.toast
import com.alan.mvvm.common.constant.RouteUrl
import com.alan.mvvm.common.event.EMMsgEvent
import com.alan.mvvm.common.im.EMClientHelper
import com.alan.mvvm.common.im.listener.ChatMsgListener
import com.alan.mvvm.common.im.listener.EMClientListener
import com.alan.mvvm.common.ui.BaseFragment
import com.hyphenate.EMValueCallBack
import com.hyphenate.chat.*
import com.hyphenate.exceptions.HyphenateException
import dagger.hilt.android.AndroidEntryPoint


/**
 * 作者：alan
 * 时间：2021/7/28
 * 备注：
 */
@AndroidEntryPoint
class ChatDetailFragment : BaseFragment<FragmentChatDetailBinding, ChatViewModel>() {


    companion object {
        @JvmStatic
        fun newInstance(userId: String) = ChatDetailFragment().apply {
            arguments = Bundle().apply {
                putString("userId", userId)
            }
        }
    }


    override val mViewModel by viewModels<ChatViewModel>()
    lateinit var mAdapter: ChatMessageAdapter
    lateinit var conversation: EMConversation
    var recyclerViewLastHeight: Int = 0;
    var currentMessages: List<EMMessage>? = null
    var PAGE_SIZE = 20
    var isFirst: Boolean = true
    var userId = ""


    override fun FragmentChatDetailBinding.initView() {
        arguments?.apply {
            userId = getString("userId")!!
        }


        initRV()
    }

    override fun initObserve() {

    }

    override fun initRequestData() {

        initConversation()
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
                    EventBusUtils.postEvent(EMMsgEvent(msg))
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
                    jumpARoute(RouteUrl.MyModule.ACTIVITY_MY_MANAGER, bundle)
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
                    hideKeyboard()
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
     * hide keyboard
     */
    protected fun hideKeyboard() {
        if (requireActivity()!!.window.attributes.softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (requireActivity()!!.currentFocus != null) {
                val inputManager =
                    requireActivity()!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        ?: return
                inputManager.hideSoftInputFromWindow(
                    requireActivity()!!.currentFocus!!.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
            }
        }
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
        requireActivity().runOnUiThread(Runnable {
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
        return requireActivity() == null || requireActivity().isDestroyed() || requireActivity().isFinishing()
    }


}