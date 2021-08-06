package com.alan.module.main.fragment

import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alan.module.main.R
import com.alan.module.main.adapter.ChatListAdapter
import com.alan.module.main.adapter.ChatManagerAdapter
import com.alan.module.main.databinding.FragmentChatBinding
import com.alan.module.main.viewmodel.ChatViewModel
import com.alan.mvvm.base.ktx.dp2px
import com.alan.mvvm.base.utils.MyColorDecoration
import com.alan.mvvm.common.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * 作者：alan
 * 时间：2021/7/28
 * 备注：
 */
@AndroidEntryPoint
class ChatFragment : BaseFragment<FragmentChatBinding, ChatViewModel>() {


    override val mViewModel by viewModels<ChatViewModel>()
    lateinit var managerAdapter: ChatManagerAdapter
    lateinit var messageAdapter: ChatListAdapter


    override fun FragmentChatBinding.initView() {
        initRVManager()
        initRVMessage()
    }

    override fun initObserve() {

    }

    override fun initRequestData() {

    }

    private fun initRVManager() {
        managerAdapter = ChatManagerAdapter()
        mBinding.rvManager.apply {
            addItemDecoration(
                MyColorDecoration(
                    0, 0, 0, dp2px(10f),
                    ContextCompat.getColor(requireActivity(), R.color.white)
                )
            )
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = managerAdapter
        }
    }

    private fun initRVMessage() {
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
    }
}