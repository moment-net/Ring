package com.alan.module.my.activity

import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alan.module.my.R
import com.alan.module.my.adapter.RecordAdapter
import com.alan.module.my.databinding.ActivityPayRecordBinding
import com.alan.module.my.viewmodol.PayRecordViewModel
import com.alan.mvvm.base.http.baseresp.BaseResponse
import com.alan.mvvm.base.http.responsebean.ConsumeBean
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.base.ktx.dp2px
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
@Route(path = RouteUrl.MyModule.ACTIVITY_MY_PAYRECORD)
@AndroidEntryPoint
class PayRecordActivity : BaseActivity<ActivityPayRecordBinding, PayRecordViewModel>() {

    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<PayRecordViewModel>()
    private lateinit var mAdapter: RecordAdapter
    private var mCursor: Int = 0
    private var isLoad = false

    /**
     * 初始化View
     */
    override fun ActivityPayRecordBinding.initView() {
        ivBack.clickDelay { finish() }
        initRv()
    }

    /**
     * 初始化曾经加入过RecyclerView
     */
    private fun initRv() {
        mBinding.srfList.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                isLoad = true
                requestList()
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                requestRefresh()
            }
        })

        mAdapter = RecordAdapter()
        mBinding.rvRecord.apply {
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
        mViewModel.ldData.observe(this) {
            when (it) {
                is BaseResponse<*> -> {
                    mCursor = it.cursor
                    var list: ArrayList<ConsumeBean> = it.data as ArrayList<ConsumeBean>
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