package com.alan.module.main.activity

import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alan.module.main.R
import com.alan.module.main.adapter.TaRemindAdapter
import com.alan.module.main.databinding.ActivityTaRemindBinding
import com.alan.module.main.viewmodel.TARemindViewModel
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
@Route(path = RouteUrl.MainModule.ACTIVITY_MAIN_TAREMIND)
@AndroidEntryPoint
class TaRemindActivity : BaseActivity<ActivityTaRemindBinding, TARemindViewModel>() {

    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<TARemindViewModel>()
    lateinit var mAdapter: TaRemindAdapter
    private var cursor: Long = 0
    private val hasMore = false
    private var isLoad = false

    /**
     * 初始化View
     */
    override fun ActivityTaRemindBinding.initView() {
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
        mAdapter = TaRemindAdapter()
        mBinding.recyclerView.apply {
            addItemDecoration(
                MyColorDecoration(
                    0, 0, 0, dp2px(10f),
                    ContextCompat.getColor(context, R.color.white)
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