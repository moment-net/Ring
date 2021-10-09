package com.alan.module.main.fragment

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alan.module.main.R
import com.alan.module.main.adapter.ManagerAdapter
import com.alan.module.main.databinding.FragmentHomeBinding
import com.alan.module.main.dialog.StateFragmentDialog
import com.alan.module.main.viewmodel.HomeViewModel
import com.alan.mvvm.base.coil.CoilUtils
import com.alan.mvvm.base.http.baseresp.BaseResponse
import com.alan.mvvm.base.http.responsebean.CookerBean
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.base.ktx.dp2px
import com.alan.mvvm.base.ktx.getResColor
import com.alan.mvvm.base.utils.EventBusRegister
import com.alan.mvvm.base.utils.MyColorDecoration
import com.alan.mvvm.base.utils.jumpARoute
import com.alan.mvvm.base.utils.toast
import com.alan.mvvm.common.constant.RouteUrl
import com.alan.mvvm.common.event.RefreshEvent
import com.alan.mvvm.common.helper.SpHelper
import com.alan.mvvm.common.http.exception.BaseHttpException
import com.alan.mvvm.common.ui.BaseFragment
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 作者：alan
 * 时间：2021/7/28
 * 备注：测试fragment
 */
@EventBusRegister
@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment().apply {
            arguments.apply { }
        }
    }

    override val mViewModel by viewModels<HomeViewModel>()
    lateinit var mAdapter: ManagerAdapter
    var isLoad = false
    var mCursor: Int = 0


    override fun FragmentHomeBinding.initView() {
        ivAvatar.clickDelay {
//            val dialog = MatchFragmentDialog.newInstance(SpHelper.getUserInfo()?.userId!!)
//            dialog.show(requireActivity().supportFragmentManager)
            jumpARoute(RouteUrl.MyModule.ACTIVITY_MY_MY)
        }
        tvState.clickDelay {
            val dialog = StateFragmentDialog.newInstance()
            dialog.show(requireActivity().supportFragmentManager)
        }
        initRV()
    }

    override fun initObserve() {
        mViewModel.ldData.observe(this) {
            when (it) {
                is BaseResponse<*> -> {
                    mCursor = it.cursor
                    val list: ArrayList<CookerBean> = it.data as ArrayList<CookerBean>
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

        mViewModel.ldState.observe(this) {
            when (it) {
                is BaseResponse<*> -> {
                    if (it.data == null) {
                        changeState(true)
                    } else {
                        changeState(false)
                    }
                }
                is BaseHttpException -> {

                }
            }
        }
    }

    override fun initRequestData() {

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

        mAdapter.setOnItemClickListener { adapter, view, position ->
            val userId = mAdapter.data.get(position).user.userId
            val bundle = Bundle().apply {
                putString("userId", userId)
            }
            jumpARoute(RouteUrl.HomeModule.ACTIVITY_HOME_MANAGER, bundle)
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


    fun changeState(isClose: Boolean) {
        if (isClose) {
            mBinding.tvState.setText("暂未匹配饭友…")
            mBinding.tvState.setTextColor(R.color._FFE26B.getResColor())
            mBinding.tvState.setCompoundDrawablesRelativeWithIntrinsicBounds(
                R.drawable.icon_home_cooking,
                0,
                0,
                0
            )
            mBinding.tvState.setShapeSolidColor(R.color._3F3317.getResColor()).setUseShape()
        } else {
            mBinding.tvState.setText("正在匹配饭友…")
            mBinding.tvState.setTextColor(R.color._221800.getResColor())
            mBinding.tvState.setCompoundDrawablesRelativeWithIntrinsicBounds(
                R.drawable.icon_home_cooked,
                0,
                0,
                0
            )
            mBinding.tvState.setShapeSolidColor(R.color._FFC94F.getResColor()).setUseShape()
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun refreshHome(event: RefreshEvent?) {
        mViewModel.requestMealStatus()
        requestRefresh()
    }

    override fun onResume() {
        super.onResume()
        setUserInfo()
        refreshHome(null)
    }

    fun setUserInfo() {
        val userInfoBean = SpHelper.getUserInfo()
        CoilUtils.loadRoundBorder(
            mBinding.ivAvatar,
            userInfoBean?.avatar!!,
            17f,
            1f,
            R.color.white.getResColor()
        )
        mBinding.tvName.setText(userInfoBean.userName)
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
