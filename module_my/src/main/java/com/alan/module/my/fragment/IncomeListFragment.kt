package com.alan.module.my.fragment

import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alan.module.my.R
import com.alan.module.my.adapter.IncomeDataAdapter
import com.alan.module.my.databinding.FragmentIncomeListBinding
import com.alan.module.my.viewmodol.IncomeListViewModel
import com.alan.mvvm.base.http.baseresp.BaseResponse
import com.alan.mvvm.base.http.responsebean.ReceivedBean
import com.alan.mvvm.base.ktx.dp2px
import com.alan.mvvm.base.utils.MyColorDecoration
import com.alan.mvvm.base.utils.toast
import com.alan.mvvm.common.http.exception.BaseHttpException
import com.alan.mvvm.common.ui.BaseFragment
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import dagger.hilt.android.AndroidEntryPoint

/**
 * 作者：alan
 * 时间：2021/7/28
 * 备注：
 */
@AndroidEntryPoint
class IncomeListFragment : BaseFragment<FragmentIncomeListBinding, IncomeListViewModel>() {

    override val mViewModel by viewModels<IncomeListViewModel>()
    private lateinit var mAdapter: IncomeDataAdapter
    private var mCursor: Int = 0
    private var isLoad = false


    override fun FragmentIncomeListBinding.initView() {
        initRV()
    }

    /**
     * 初始化ReclerView
     */
    fun initRV() {
        mBinding.srlList.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                isLoad = true
                requestList()
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                requestRefresh()
            }
        })
        mAdapter = IncomeDataAdapter()
        mBinding.rvIncome.apply {
            addItemDecoration(
                MyColorDecoration(
                    0, 0, 0,
                    dp2px(10f), ContextCompat.getColor(requireActivity(), R.color.white)
                )
            )
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = mAdapter
        }

    }


    override fun initObserve() {
        mViewModel.ldData.observe(this) {
            when (it) {
                is BaseResponse<*> -> {
                    mCursor = it.cursor
                    var list: ArrayList<ReceivedBean> = it.data as ArrayList<ReceivedBean>
                    if (isLoad) {
                        mBinding.srlList.finishLoadMore()
                        mAdapter.addData(list)
                    } else {
                        mBinding.srlList.finishRefresh()
                        mAdapter.setList(list)
                    }

                }

                is BaseHttpException -> {
                    if (isLoad) {
                        mBinding.srlList.finishLoadMore()
                    } else {
                        mBinding.srlList.finishRefresh()
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