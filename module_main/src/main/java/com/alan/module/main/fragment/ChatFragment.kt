package com.alan.module.main.fragment

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alan.module.main.R
import com.alan.module.main.adapter.ChatListAdapter
import com.alan.module.main.databinding.FragmentChatBinding
import com.alan.module.main.viewmodel.ChatViewModel
import com.alan.mvvm.base.ktx.dp2px
import com.alan.mvvm.base.utils.EventBusRegister
import com.alan.mvvm.base.utils.MyColorDecoration
import com.alan.mvvm.base.utils.jumpARoute
import com.alan.mvvm.common.constant.IMConstant
import com.alan.mvvm.common.constant.RouteUrl
import com.alan.mvvm.common.event.MessageEvent
import com.alan.mvvm.common.ui.BaseFragment
import com.hyphenate.chat.EMConversation
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 作者：alan
 * 时间：2021/7/28
 * 备注：
 */
@EventBusRegister
@AndroidEntryPoint
class ChatFragment : BaseFragment<FragmentChatBinding, ChatViewModel>() {
    companion object {
        @JvmStatic
        fun newInstance() =
            ChatFragment().apply {
                arguments = Bundle().apply {}
            }
    }

    override val mViewModel by viewModels<ChatViewModel>()
    lateinit var messageAdapter: ChatListAdapter
    val conList by lazy(mode = LazyThreadSafetyMode.NONE) { arrayListOf<EMConversation>() }


    override fun FragmentChatBinding.initView() {
        initRVMessage()
    }

    override fun initObserve() {

    }

    override fun initRequestData() {

    }


    fun initRVMessage() {
        mBinding.srfList.setOnRefreshListener {
            requestList()
        }
        mBinding.srfList.setEnableLoadMore(false)
        messageAdapter = ChatListAdapter()
        mBinding.rvMessage.apply {
            addItemDecoration(
                MyColorDecoration(
                    0, 0, 0, dp2px(30f),
                    ContextCompat.getColor(requireActivity(), R.color.white)
                )
            )
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = messageAdapter
        }
        messageAdapter.setOnItemClickListener { adapter, view, position ->
            val item: Any = messageAdapter.getItem(position)
            if (item is EMConversation) {
                val bundle = Bundle().apply {
                    putString("userId", item.conversationId())
                }
                jumpARoute(RouteUrl.ChatModule.ACTIVITY_CHAT_DETAIL, bundle)
            }
        }
    }


    override fun onResume() {
        super.onResume()
        requestList()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        requestList()
    }


    fun requestList() {
        val list = mViewModel.requestConversations()
        //过滤系统消息
        if (!conList.isEmpty()) {
            conList.clear()
        }
        for (conversation in list) {
            if (conversation.conversationId().length >= 16) {
                conList.add(conversation)
            }
        }

        messageAdapter.setList(conList)
        mBinding.srfList.finishRefresh()
    }

    //获取新消息
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun receiveMsg(event: MessageEvent) {
        when (event.type) {
            IMConstant.EVENT_TYPE_MESSAGE -> {
                requestList()
            }
        }
    }
}