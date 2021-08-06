package com.alan.module.my.activity

import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alan.module.my.R
import com.alan.module.my.adapter.SystemMsgAdapter
import com.alan.module.my.databinding.ActivitySystemMessageBinding
import com.alan.module.my.viewmodol.SystemMessageViewModel
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.base.ktx.dp2px
import com.alan.mvvm.base.utils.MyColorDecoration
import com.alan.mvvm.common.constant.RouteUrl
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
    private var cursor: Long = 0
    private val hasMore = false
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
                cursor = 0
                isLoad = false
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                if (hasMore) {
                    isLoad = true
                } else {
                    mBinding.srfList.finishLoadMore()
                }
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
    }

    /**
     * 订阅数据
     */
    override fun initObserve() {

    }

    /**
     * 获取数据
     */
    override fun initRequestData() {

    }
}