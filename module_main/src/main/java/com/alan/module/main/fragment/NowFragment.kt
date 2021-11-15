package com.alan.module.main.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alan.module.main.R
import com.alan.module.main.adapter.NowAdapter
import com.alan.module.main.databinding.FragmentNowBinding
import com.alan.module.main.viewmodel.NowViewModel
import com.alan.mvvm.base.http.apiservice.HttpBaseUrlConstant
import com.alan.mvvm.base.http.baseresp.BaseResponse
import com.alan.mvvm.base.http.responsebean.NowBean
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.base.ktx.dp2px
import com.alan.mvvm.base.ktx.getResColor
import com.alan.mvvm.base.utils.*
import com.alan.mvvm.common.constant.RouteUrl
import com.alan.mvvm.common.db.entity.UserEntity
import com.alan.mvvm.common.event.ChangeThinkEvent
import com.alan.mvvm.common.helper.SpHelper
import com.alan.mvvm.common.http.exception.BaseHttpException
import com.alan.mvvm.common.im.EMClientHelper
import com.alan.mvvm.common.report.DataPointUtil
import com.alan.mvvm.common.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 作者：alan
 * 时间：2021/7/28
 * 备注：
 */
@EventBusRegister
@AndroidEntryPoint
class NowFragment : BaseFragment<FragmentNowBinding, NowViewModel>() {

    override val mViewModel by viewModels<NowViewModel>()
    lateinit var mAdapter: NowAdapter
    lateinit var popupWindow: PopupWindow
    var isLoad = false
    var mCursor: Int = 0

    companion object {
        @JvmStatic
        fun newInstance() = NowFragment().apply {
            arguments.apply { }
        }
    }

    override fun FragmentNowBinding.initView() {
        initRV()
    }

    override fun initObserve() {
        mViewModel.ldData.observe(this) {
            when (it) {
                is BaseResponse<*> -> {
                    mCursor = it.cursor
                    val list: ArrayList<NowBean> = it.data as ArrayList<NowBean>
                    if (isLoad) {
                        mAdapter.addData(list)
                    } else {
                        mAdapter.setList(list)
                    }

                    if (it.hasMore) {
                        mAdapter.loadMoreModule.loadMoreComplete()
                    } else {
                        mAdapter.loadMoreModule.loadMoreEnd()
                    }

                    if (mAdapter.data.size == 0) {
                        EventBusUtils.postEvent(ChangeThinkEvent(1))
                    }
                }

                is BaseHttpException -> {
                    toast(it.errorMessage)
                }

                is Int -> {
                    mAdapter.removeAt(it)
                    mAdapter.notifyItemRemoved(it)
                }

                is Pair<*, *> -> {
                    val userId = it.first as String
                    val content = it.second as String


                    val bundle = Bundle().apply {
                        putString("userId", userId)
                        putString("content", content)
                    }
                    jumpARoute(RouteUrl.ChatModule.ACTIVITY_CHAT_DETAIL, bundle)
//                    val inputDialog = InputFragmentDialog.newInstance(userId, userName)
//                    inputDialog.show(requireActivity().supportFragmentManager)
                }
            }
        }
    }

    override fun initRequestData() {
        requestList()
    }

    fun initRV() {
        mAdapter = NowAdapter()
        mBinding.rvList.apply {
            addItemDecoration(
                MyColorDecoration(
                    0,
                    0,
                    0,
                    dp2px(15f),
                    R.color.transparent.getResColor()
                )
            )
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = mAdapter
        }

        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            val id = mAdapter.data.get(position).id
            val userId = mAdapter.data.get(position).user.userId
            val userName = mAdapter.data.get(position).user.userName
            val avatar = mAdapter.data.get(position).user.avatar
            when (view.id) {
                R.id.iv_avatar -> {
                    val bundle = Bundle().apply {
                        putString("userId", userId)
                    }
                    jumpARoute(RouteUrl.MyModule.ACTIVITY_MY_MANAGER, bundle)
                }
                R.id.iv_more -> {
                    showPopupWindow(view, id, userId, position)
                    DataPointUtil.reportHomeMenu(SpHelper.getUserInfo()?.userId!!)
                }

            }
        }
        mAdapter.listener = object : NowAdapter.OnReplyClickListener {
            override fun onReply(position: Int, content: String) {
                if (context == null) {
                    toast("请输入回复内容")
                    return
                }
                val userId = mAdapter.data.get(position).user.userId
                val userName = mAdapter.data.get(position).user.userName
                val avatar = mAdapter.data.get(position).user.avatar

                DataPointUtil.reportTogether(SpHelper.getUserInfo()?.userId!!, userId)
                if (TextUtils.equals(userId, SpHelper.getUserInfo()?.userId)) {
                    toast("不可以和自己一起，试试点击他人一起按钮吧！")
                }
                EMClientHelper.saveUser(
                    UserEntity(
                        userId,
                        userName,
                        avatar
                    )
                )
                mViewModel.requestIsReply(userId, content)
            }
        }
        mAdapter.setEmptyView(TextView(requireActivity()).apply {
            setText("当前还没有正在状态，\n快去点击发布描述一下你当前的状态吧！")
            setTextSize(14f)
            setTextColor(R.color._263A3A3A.getResColor())
            gravity = Gravity.CENTER
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                dp2px(200f)
            )
        })

        mAdapter.loadMoreModule.isEnableLoadMoreIfNotFullPage = false
        mAdapter.loadMoreModule.setOnLoadMoreListener {
            mBinding.rvList.postDelayed(Runnable {
                isLoad = true
                requestList()
            }, 1000)
        }
//        mBinding.srfList.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
//            override fun onLoadMore(refreshLayout: RefreshLayout) {
//                isLoad = true
//                requestList()
//            }
//
//            override fun onRefresh(refreshLayout: RefreshLayout) {
//                requestRefresh()
//            }
//        })
    }

    /**
     * 显示菜单项
     */
    fun showPopupWindow(view: View, id: String, userId: String, position: Int) {
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
                    HttpBaseUrlConstant.BASE_H5URL + "#/freedomspeak-report?reportFromUserid=${SpHelper.getUserInfo()!!.userId}&reportToUserid=${userId}"
                )
                putString("webTitle", "举报")
            }
            jumpARoute(RouteUrl.WebModule.ACTIVITY_WEB_WEB, bundle)
            DataPointUtil.reportReport(SpHelper.getUserInfo()?.userId!!)
        }
        tvShield.clickDelay {
            popupWindow.dismiss()
            mViewModel.requestBanNow(id, position)
            DataPointUtil.reportBlock(SpHelper.getUserInfo()?.userId!!)
        }

        popupWindow = PopupWindow(
            contentview,
            dp2px(88f),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        popupWindow.setFocusable(true)
        popupWindow.setOutsideTouchable(false)
        popupWindow.showAsDropDown(view, -dp2px(50f), 0)
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

    //切换
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun showCall(event: ChangeThinkEvent) {
        if (event.position == 0) {
            requestRefresh()
        }
    }


    override fun onResume() {
        super.onResume()
        requestRefresh()
    }
}