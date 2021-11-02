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
import com.alan.mvvm.base.http.responsebean.ThinkBean
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.base.ktx.dp2px
import com.alan.mvvm.base.ktx.getResColor
import com.alan.mvvm.base.utils.EventBusRegister
import com.alan.mvvm.base.utils.MyColorDecoration
import com.alan.mvvm.base.utils.jumpARoute
import com.alan.mvvm.base.utils.toast
import com.alan.mvvm.common.constant.IMConstant
import com.alan.mvvm.common.constant.RouteUrl
import com.alan.mvvm.common.db.entity.UserEntity
import com.alan.mvvm.common.event.ChangeThinkEvent
import com.alan.mvvm.common.helper.SpHelper
import com.alan.mvvm.common.http.exception.BaseHttpException
import com.alan.mvvm.common.im.EMClientHelper
import com.alan.mvvm.common.report.DataPointUtil
import com.alan.mvvm.common.ui.BaseFragment
import com.hyphenate.chat.EMMessage
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
                    val list: ArrayList<ThinkBean> = it.data as ArrayList<ThinkBean>
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
                }

                is BaseHttpException -> {
                    toast(it.errorMessage)
                }


                is Int -> {
                    mAdapter.removeAt(it)
                    mAdapter.notifyItemRemoved(it)
                }

                is Pair<*, *> -> {
                    val action = it.first
                    val position = it.second as Int
                    val favoriteCount = mAdapter.data.get(position).favoriteCount
                    mAdapter.data.get(position).isFavorite = if (action == 0) {
                        mAdapter.data.get(position).favoriteCount = favoriteCount - 1
                        false
                    } else {
                        mAdapter.data.get(position).favoriteCount = favoriteCount + 1
                        val user = mAdapter.data.get(position).user
                        sendTextMessage("${SpHelper.getUserInfo()?.userName}认同了你的想法", user.userId)
                        true
                    }
                    mAdapter.notifyItemChanged(position)
                }
            }
        }
    }

    override fun initRequestData() {
        requestList()
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
            val item = mAdapter.data.get(position)
            val userId = item.user.userId
            when (view.id) {
                R.id.iv_avatar -> {
                    val bundle = Bundle().apply {
                        putString("userId", userId)
                    }
                    jumpARoute(RouteUrl.MyModule.ACTIVITY_MY_MANAGER, bundle)
                }
                R.id.iv_more -> {
                    showPopupWindow(view, item.id, userId, position)
                    DataPointUtil.reportHomeMenu(SpHelper.getUserInfo()?.userId!!)
                }
                R.id.iv_zan -> {
                    val favorite = item.isFavorite
                    if (favorite) {
                        mViewModel.requestZan(item.id, 0, position)
                    } else {
                        mViewModel.requestZan(item.id, 1, position)
                    }
                    DataPointUtil.reportLike(SpHelper.getUserInfo()?.userId!!, userId)
                    EMClientHelper.saveUser(
                        UserEntity(
                            userId,
                            item.user.userName,
                            item.user.avatar
                        )
                    )
                }
            }
        }

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
            mViewModel.requestBanThink(id, position)
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
        if (event.position == 1) {
            requestRefresh()
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            requestRefresh()
        }
    }


    override fun onResume() {
        super.onResume()
        requestRefresh()
    }


    /**
     * 发送点赞IM消息
     */
    fun sendTextMessage(content: String, userId: String) {
        val message = EMMessage.createTxtSendMessage(content, userId)
        // 增加自己特定的属性
        message.setAttribute(IMConstant.MESSAGE_ATTR_AVATAR, SpHelper.getUserInfo()?.avatar);
        message.setAttribute(IMConstant.MESSAGE_ATTR_USERNAME, SpHelper.getUserInfo()?.userName);

        // 设置自定义扩展字段-强制推送
//        message.setAttribute(IMConstant.MESSAGE_ATTR_FORCEPUSH, true);
        // 设置自定义扩展字段-发送静默消息（不推送）
//        message.setAttribute(IMConstant.MESSAGE_ATTR_IGNOREPUSH, true);

        EMClientHelper.chatManager.sendMessage(message)
    }
}