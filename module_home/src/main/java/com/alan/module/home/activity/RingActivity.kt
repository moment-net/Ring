package com.alan.module.home.activity

import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.alan.module.home.adapter.RingAdapter
import com.alan.module.home.databinding.ActivityRingBinding
import com.alan.module.home.viewmodol.RingViewModel
import com.alan.mvvm.common.constant.RouteUrl
import com.alan.mvvm.common.ui.BaseActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import dagger.hilt.android.AndroidEntryPoint


/**
 * 作者：alan
 * 时间：2021/7/30
 * 备注：选择铃声
 */
@Route(path = RouteUrl.HomeModule.ACTIVITY_HOME_RING)
@AndroidEntryPoint
class RingActivity : BaseActivity<ActivityRingBinding, RingViewModel>() {

    private lateinit var mAdapter: RingAdapter

    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<RingViewModel>()

    /**
     * 初始化View
     */
    override fun ActivityRingBinding.initView() {
        initRV()
    }


    private fun initRV() {
        mBinding.srfList.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {

            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {

            }
        })


        mAdapter = RingAdapter()
        mBinding.rvRing.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = mAdapter
        }
        mAdapter.addData("")
        mAdapter.addData("")
        mAdapter.addData("")
        mAdapter.addData("")
        mAdapter.addData("")
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


