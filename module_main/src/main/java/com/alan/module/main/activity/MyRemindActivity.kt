package com.alan.module.main.activity

import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alan.module.main.R
import com.alan.module.main.adapter.MyRemindAdapter
import com.alan.module.main.databinding.ActivityMyRemindBinding
import com.alan.module.main.viewmodel.MyRemindViewModel
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
 * 备注：我的订阅提醒
 */
@Route(path = RouteUrl.MainModule.ACTIVITY_MAIN_MYREMIND)
@AndroidEntryPoint
class MyRemindActivity : BaseActivity<ActivityMyRemindBinding, MyRemindViewModel>() {

    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<MyRemindViewModel>()
    lateinit var mAdapter: MyRemindAdapter
    private var cursor: Long = 0
    private val hasMore = false
    private var isLoad = false

    /**
     * 初始化View
     */
    override fun ActivityMyRemindBinding.initView() {
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
        mAdapter = MyRemindAdapter()
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