package com.alan.module.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alan.module.main.R
import com.alan.module.main.adapter.ThinkAdapter
import com.alan.module.main.databinding.FragmentThinkBinding
import com.alan.module.main.viewmodel.ThinkViewModel
import com.alan.mvvm.base.http.apiservice.HttpBaseUrlConstant
import com.alan.mvvm.base.http.baseresp.BaseResponse
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.base.ktx.dp2px
import com.alan.mvvm.base.ktx.getResColor
import com.alan.mvvm.base.utils.MyColorDecoration
import com.alan.mvvm.base.utils.jumpARoute
import com.alan.mvvm.base.utils.toast
import com.alan.mvvm.common.constant.RouteUrl
import com.alan.mvvm.common.helper.SpHelper
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
class ThinkFragment : BaseFragment<FragmentThinkBinding, ThinkViewModel>() {

    override val mViewModel by viewModels<ThinkViewModel>()
    lateinit var mAdapter: ThinkAdapter
    var isLoad = false
    var mCursor: Int = 0
    lateinit var popupWindow: PopupWindow


    companion object {
        @JvmStatic
        fun newInstance() = ThinkFragment().apply {
            arguments.apply { }
        }
    }

    override fun FragmentThinkBinding.initView() {
        initRV()
    }

    override fun initObserve() {
        mViewModel.ldData.observe(this) {
            when (it) {
                is BaseResponse<*> -> {
                    mCursor = it.cursor
//                    val list: ArrayList<CookerBean> = it.data as ArrayList<CookerBean>
//                    if (isLoad) {
//                        mBinding.srfList.finishLoadMore()
//                        mAdapter.addData(list)
//                    } else {
//                        mBinding.srfList.finishRefresh()
//                        mAdapter.setList(list)
//                    }

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

    fun initRV() {
        mAdapter = ThinkAdapter()
        mBinding.rvList.apply {
            addItemDecoration(
                MyColorDecoration(
                    0,
                    0,
                    0,
                    dp2px(30f),
                    R.color.transparent.getResColor()
                )
            )
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = mAdapter
        }

        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.iv_avatar -> {
                    val bundle = Bundle().apply {
                        putString("userId", "")
                    }
                    jumpARoute(RouteUrl.HomeModule.ACTIVITY_HOME_MANAGER, bundle)
                }
                R.id.iv_more -> {
                    showPopupWindow(view)
                }
                R.id.iv_zan -> {

                }
            }
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

        mAdapter.addData("")
        mAdapter.addData("")
        mAdapter.addData("")
        mAdapter.addData("")
        mAdapter.addData("")
        mAdapter.addData("")
    }

    /**
     * 显示菜单项
     */
    fun showPopupWindow(view: View) {
        val contentview: View =
            LayoutInflater.from(requireActivity()).inflate(R.layout.layout_more_menu, null)
        contentview.isFocusable = true
        contentview.isFocusableInTouchMode = true
        val tvReport = contentview.findViewById<TextView>(R.id.tv_report)
        val tvShield = contentview.findViewById<TextView>(R.id.tv_shield)


        tvReport.clickDelay {
            popupWindow.dismiss()
            val bundle = Bundle().apply {
                putString(
                    "webUrl",
                    HttpBaseUrlConstant.BASE_URL + "&reportFromUserid=${SpHelper.getUserInfo()!!.userId}&reportToUserid=${view.id}"
                )
                putString("webTitle", "举报")
            }
            jumpARoute(RouteUrl.WebModule.ACTIVITY_WEB_WEB, bundle)
        }
        tvShield.clickDelay {
            popupWindow.dismiss()

        }

        popupWindow = PopupWindow(
            contentview,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        popupWindow.setFocusable(true)
        popupWindow.setOutsideTouchable(false)
        popupWindow.showAsDropDown(view, -dp2px(130f), 10)
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