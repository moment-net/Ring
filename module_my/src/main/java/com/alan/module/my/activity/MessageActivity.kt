package com.alan.module.my.activity

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alan.module.my.R
import com.alan.module.my.adapter.MessageAdapter
import com.alan.module.my.databinding.ActivityMessageBinding
import com.alan.module.my.viewmodol.MessageViewModel
import com.alan.mvvm.base.http.baseresp.BaseResponse
import com.alan.mvvm.base.http.responsebean.MessageBean
import com.alan.mvvm.base.http.responsebean.UnreadBean
import com.alan.mvvm.base.http.responsebean.UserInfoBean
import com.alan.mvvm.base.ktx.*
import com.alan.mvvm.base.utils.MyColorDecoration
import com.alan.mvvm.base.utils.jumpARoute
import com.alan.mvvm.base.utils.toast
import com.alan.mvvm.common.constant.RouteUrl
import com.alan.mvvm.common.db.entity.UserEntity
import com.alan.mvvm.common.http.exception.BaseHttpException
import com.alan.mvvm.common.im.EMClientHelper
import com.alan.mvvm.common.ui.BaseActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import dagger.hilt.android.AndroidEntryPoint


/**
 * 作者：alan
 * 时间：2021/7/30
 * 备注：
 */
@Route(path = RouteUrl.MyModule.ACTIVITY_MY_MSG)
@AndroidEntryPoint
class MessageActivity : BaseActivity<ActivityMessageBinding, MessageViewModel>() {

    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<MessageViewModel>()
    lateinit var mAdapter: MessageAdapter
    private var mCursor: Int = 0
    private var followPosition: Int = 0
    private var isLoad = false
    lateinit var ivRed: ImageView

    /**
     * 初始化View
     */
    override fun ActivityMessageBinding.initView() {
        ivBack.clickDelay { finish() }
        initRV()
    }

    private fun initRV() {
        mBinding.srfList.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                requestRefresh()
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                isLoad = true
                requestList()
            }
        })
        mAdapter = MessageAdapter()
        mBinding.rvMsg.apply {
            addItemDecoration(
                MyColorDecoration(
                    0, 0, 0, dp2px(20f),
                    ContextCompat.getColor(this@MessageActivity, R.color.white)
                )
            )
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = mAdapter
        }
        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            val userInfoBean: UserInfoBean? = mAdapter.data.get(position).content?.user
            if (userInfoBean == null) {
                return@setOnItemChildClickListener
            }
            when (view.id) {
                R.id.iv_avatar -> {
                    val bundle = Bundle().apply {
                        putString("userId", userInfoBean.userId)
                    }
                    jumpARoute(RouteUrl.MyModule.ACTIVITY_MY_MANAGER, bundle)
                }

                R.id.tv_follow -> {
                    followPosition = position
                    if (userInfoBean.followStatus == 0) {
                        mViewModel.requestChangeFollow(userInfoBean.userId, 1)
                    } else {
                        mViewModel.requestChangeFollow(userInfoBean.userId, 0)
                    }
                }

                R.id.tv_chat -> {
                    val bundle = Bundle().apply {
                        putString("userId", userInfoBean.userId)
                    }
                    EMClientHelper.saveUser(
                        UserEntity(
                            userInfoBean.userId,
                            userInfoBean.userName,
                            userInfoBean.avatar
                        )
                    )
                    jumpARoute(RouteUrl.ChatModule.ACTIVITY_CHAT_DETAIL, bundle)
                }
            }
        }

        //添加头布局
        val ll_msg: View = View.inflate(this, R.layout.layout_system, null)
        val clSystem = ll_msg.findViewById<ConstraintLayout>(R.id.cl_system)
        ivRed = ll_msg.findViewById<ImageView>(R.id.iv_red)
        clSystem.clickDelay { jumpARoute(RouteUrl.MyModule.ACTIVITY_MY_SYSTEMMSG) }
        mAdapter.addHeaderView(ll_msg)
    }


    /**
     * 订阅数据
     */
    override fun initObserve() {
        mViewModel.ldData.observe(this) {
            when (it) {
                is BaseResponse<*> -> {
                    mCursor = it.cursor
                    val list: ArrayList<MessageBean> = it.data as ArrayList<MessageBean>
                    if (isLoad) {
                        mBinding.srfList.finishLoadMore()
                        mAdapter.addData(list)
                    } else {
                        mBinding.srfList.finishRefresh()
                        mAdapter.setList(list)
                    }
                    if (mAdapter.data.size == 0) {
                        mBinding.tvEmpty.visible()
                    } else {
                        mBinding.tvEmpty.gone()
                    }
                }

                is BaseHttpException -> {
                    if (isLoad) {
                        mBinding.srfList.finishLoadMore()
                    } else {
                        mBinding.srfList.finishRefresh()
                    }
                    toast(it.errorMessage)
                }

                is UnreadBean -> {
                    if (it.newSystemNoticeTotal > 0) {
                        ivRed.visible()
                    } else {
                        ivRed.gone()
                    }
                }

                is Int -> {
                    mAdapter.data.get(followPosition).content?.user?.followStatus = it
                    mAdapter.notifyItemChanged(followPosition + 1)
                }
            }
        }
    }

    /**
     * 获取数据
     */
    override fun initRequestData() {

    }

    override fun onResume() {
        super.onResume()
        requestRefresh()
    }

    /**
     * 刷新列表
     */
    fun requestRefresh() {
        isLoad = false
        mCursor = 0
        requestList()
    }


    fun requestList() {
        mViewModel.requestUnRead()
        mViewModel.requestList(mCursor)
    }
}