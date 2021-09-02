package com.alan.module.my.activity

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
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.base.ktx.dp2px
import com.alan.mvvm.base.utils.MyColorDecoration
import com.alan.mvvm.base.utils.jumpARoute
import com.alan.mvvm.base.utils.toast
import com.alan.mvvm.common.constant.RouteUrl
import com.alan.mvvm.common.http.exception.BaseHttpException
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
    private var isLoad = false

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

        //添加头布局
        val ll_msg: View = View.inflate(this, R.layout.layout_system, null)
        val clSystem = ll_msg.findViewById<ConstraintLayout>(R.id.cl_system)
        val ivRed = ll_msg.findViewById<ImageView>(R.id.iv_red)
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
                    var list: ArrayList<MessageBean> = it.data as ArrayList<MessageBean>
                    if (isLoad) {
                        mBinding.srfList.finishLoadMore()
                        mAdapter.addData(list)
                    } else {
                        mBinding.srfList.finishRefresh()
                        mAdapter.setList(list)
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
        mViewModel.requestList(mCursor)
    }
}