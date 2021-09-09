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
import com.alan.mvvm.base.http.responsebean.AvatarInfoBean
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.base.ktx.dp2px
import com.alan.mvvm.base.utils.GsonUtil
import com.alan.mvvm.base.utils.MyColorDecoration
import com.alan.mvvm.base.utils.jumpARoute
import com.alan.mvvm.common.constant.RouteUrl
import com.alan.mvvm.common.ui.BaseFragment
import com.hyphenate.chat.EMConversation
import dagger.hilt.android.AndroidEntryPoint

/**
 * 作者：alan
 * 时间：2021/7/28
 * 备注：
 */
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


    override fun FragmentChatBinding.initView() {
        tvOpen.clickDelay {

        }
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
                val avatarInfoBean = GsonUtil.jsonToBean(item.extField, AvatarInfoBean::class.java)
                val bundle = Bundle().apply {
                    putString("userId", item.conversationId())
                    putString("userName", avatarInfoBean?.userName)
                    putString("avatar", avatarInfoBean?.avatar)
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
        messageAdapter.setList(list)
        mBinding.srfList.finishRefresh()
    }


}