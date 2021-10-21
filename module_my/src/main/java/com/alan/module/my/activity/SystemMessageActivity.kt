package com.alan.module.my.activity

import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alan.module.my.R
import com.alan.module.my.adapter.SystemMsgAdapter
import com.alan.module.my.databinding.ActivitySystemMessageBinding
import com.alan.module.my.viewmodol.SystemMessageViewModel
import com.alan.mvvm.base.http.baseresp.BaseResponse
import com.alan.mvvm.base.http.responsebean.SystemMessageBean
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.base.ktx.dp2px
import com.alan.mvvm.base.ktx.getResColor
import com.alan.mvvm.base.utils.MyColorDecoration
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
@Route(path = RouteUrl.MyModule.ACTIVITY_MY_SYSTEMMSG)
@AndroidEntryPoint
class SystemMessageActivity : BaseActivity<ActivitySystemMessageBinding, SystemMessageViewModel>() {

    /**
     * 通过 viewModels() + Hilt获取 ViewModel 实例
     */
    override val mViewModel by viewModels<SystemMessageViewModel>()
    lateinit var mAdapter: SystemMsgAdapter
    private var mCursor: Int = 0
    private var isLoad = false

    /**
     * 初始化View
     */
    override fun ActivitySystemMessageBinding.initView() {
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
        mAdapter = SystemMsgAdapter()
        mBinding.rvMsg.apply {
            addItemDecoration(
                MyColorDecoration(
                    0, 0, 0, dp2px(20f),
                    ContextCompat.getColor(this@SystemMessageActivity, R.color.white)
                )
            )
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = mAdapter
        }

        mAdapter.setEmptyView(TextView(this).apply {
            setText("暂无系统消息")
            setTextSize(16f)
            setTextColor(R.color._263A3A3A.getResColor())
            gravity = Gravity.CENTER
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        })
    }

    /**
     * 订阅数据
     */
    override fun initObserve() {
        mViewModel.ldData.observe(this) {
            when (it) {
                is BaseResponse<*> -> {
                    mCursor = it.cursor
                    var list: ArrayList<SystemMessageBean> = it.data as ArrayList<SystemMessageBean>
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