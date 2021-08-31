package com.alan.module.main.fragment

import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alan.module.main.R
import com.alan.module.main.adapter.ManagerAdapter
import com.alan.module.main.databinding.FragmentHomeBinding
import com.alan.module.main.viewmodel.HomeViewModel
import com.alan.mvvm.base.http.baseresp.BaseResponse
import com.alan.mvvm.base.http.responsebean.CookerBean
import com.alan.mvvm.base.ktx.dp2px
import com.alan.mvvm.base.ktx.getResColor
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
 * 备注：测试fragment
 */
@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

    override val mViewModel by viewModels<HomeViewModel>()
    lateinit var mAdapter: ManagerAdapter
    var isLoad = false
    var mCursor: Int = 0


    override fun FragmentHomeBinding.initView() {
        initRV()
        changeState()
    }

    override fun initObserve() {
        mViewModel.ldData.observe(this) {
            when (it) {
                is BaseResponse<*> -> {
                    mCursor = it.cursor
                    var list: ArrayList<CookerBean> = it.data as ArrayList<CookerBean>
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
        requestRefresh()
    }


    fun initRV() {
        mAdapter = ManagerAdapter()
        mBinding.rvManager.apply {
            addItemDecoration(
                MyColorDecoration(
                    0,
                    0,
                    0,
                    dp2px(10f),
                    ContextCompat.getColor(requireActivity(), R.color.transparent)
                )
            )
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = mAdapter
        }

        mBinding.srfList.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                isLoad = true
                requestList()
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                requestRefresh()
            }
        })
    }


    fun changeState() {
        if (true) {
            mBinding.tvRecord.setText("暂未匹配饭友…")
            mBinding.tvRecord.setTextColor(R.color._FFE26B.getResColor())
            mBinding.tvRecord.setCompoundDrawablesRelativeWithIntrinsicBounds(
                R.drawable.icon_home_cooking,
                0,
                0,
                0
            )
            mBinding.tvRecord.setShapeSolidColor(R.color._3F3317.getResColor()).setUseShape()
        } else {
            mBinding.tvRecord.setText("正在匹配饭友…")
            mBinding.tvRecord.setTextColor(R.color._221800.getResColor())
            mBinding.tvRecord.setCompoundDrawablesRelativeWithIntrinsicBounds(
                R.drawable.icon_home_cooked,
                0,
                0,
                0
            )
            mBinding.tvRecord.setShapeSolidColor(R.color._FFC94F.getResColor()).setUseShape()
        }
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
