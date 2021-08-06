package com.alan.module.my.fragment

import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alan.module.my.R
import com.alan.module.my.adapter.WithDrawAdapter
import com.alan.module.my.databinding.FragmentWithDrawListBinding
import com.alan.module.my.viewmodol.WithdrawListViewModel
import com.alan.mvvm.base.ktx.dp2px
import com.alan.mvvm.base.utils.MyColorDecoration
import com.alan.mvvm.common.ui.BaseFragment
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import dagger.hilt.android.AndroidEntryPoint

/**
 * 作者：alan
 * 时间：2021/7/28
 * 备注：
 */
@AndroidEntryPoint
class WithDrawListFragment : BaseFragment<FragmentWithDrawListBinding, WithdrawListViewModel>() {

    override val mViewModel by viewModels<WithdrawListViewModel>()
    private lateinit var mAdapter: WithDrawAdapter
    private var cursor: Long = 0
    private var hasMore = false
    private var isLoad = false


    override fun FragmentWithDrawListBinding.initView() {
        initRV()
    }


    /**
     * 初始化ReclerView
     */
    fun initRV() {
        mBinding.srlList.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                if (hasMore) {
                    isLoad = true
                    requestList()
                } else {
                    mBinding.srlList.finishLoadMore()
                }
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                cursor = 0
                isLoad = false
                requestList()
            }
        })
        mAdapter = WithDrawAdapter()
        mBinding.rvWithdraw.apply {
            addItemDecoration(
                MyColorDecoration(
                    0, 0, 0,
                    dp2px(10f), ContextCompat.getColor(requireActivity(), R.color.white)
                )
            )
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = mAdapter
        }
        mAdapter.setOnItemClickListener(OnItemClickListener { adapter, view, position ->
//            val withdrawBean: WithdrawBean = mAdapter.getData().get(position)
//            val tradeId: String = withdrawBean.getTradeId()
//            ARouter.getInstance().build(IConstantRoom.MyConstant.MY_WITHDRAWDETAIL)
//                .withString("tradeId", tradeId).navigation()
        })
//        mAdapter.setEmptyView(R.layout.item_null_record)
    }

    override fun initObserve() {

    }

    override fun initRequestData() {
        requestList()
    }

    /**
     * 获取列表数据
     */
    fun requestList() {

    }
}