package com.alan.module.main.fragment

import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alan.module.main.R
import com.alan.module.main.adapter.MyThinkAdapter
import com.alan.module.main.databinding.FragmentMyBinding
import com.alan.module.main.viewmodel.MyViewModel
import com.alan.mvvm.base.coil.CoilUtils
import com.alan.mvvm.base.http.baseresp.BaseResponse
import com.alan.mvvm.base.http.responsebean.DiamondBean
import com.alan.mvvm.base.http.responsebean.ThinkBean
import com.alan.mvvm.base.http.responsebean.UnreadBean
import com.alan.mvvm.base.ktx.*
import com.alan.mvvm.base.utils.MyColorDecoration
import com.alan.mvvm.base.utils.jumpARoute
import com.alan.mvvm.common.constant.RouteUrl
import com.alan.mvvm.common.helper.SpHelper
import com.alan.mvvm.common.ui.BaseFragment
import com.socks.library.KLog
import dagger.hilt.android.AndroidEntryPoint


/**
 * 作者：alan
 * 时间：2021/7/28
 * 备注：
 */
@AndroidEntryPoint
class MyFragment : BaseFragment<FragmentMyBinding, MyViewModel>() {

    companion object {
        @JvmStatic
        fun newInstance() =
            MyFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override val mViewModel by viewModels<MyViewModel>()
    lateinit var mAdapter: MyThinkAdapter
    var isLoad = false
    var mCursor: Int = 0

    @RequiresApi(Build.VERSION_CODES.M)
    override fun FragmentMyBinding.initView() {
        ivSet.clickDelay {
            jumpARoute(RouteUrl.MyModule.ACTIVITY_MY_SET)
        }
        ivMsg.clickDelay {
            jumpARoute(RouteUrl.MyModule.ACTIVITY_MY_MSG)
        }
        ivEdit.clickDelay {
            jumpARoute(RouteUrl.MyModule.ACTIVITY_MY_PERSONINFO)
        }
        tvFocusNum.clickDelay {
            val bundle = Bundle().apply { putString("type", "1") }
            jumpARoute(RouteUrl.MyModule.ACTIVITY_MY_FOCUS, bundle)
        }
        tvFollowNum.clickDelay {
            val bundle = Bundle().apply { putString("type", "2") }
            jumpARoute(RouteUrl.MyModule.ACTIVITY_MY_FOCUS, bundle)
        }
        clDiamond.clickDelay { jumpARoute(RouteUrl.MyModule.ACTIVITY_MY_DIAMOND) }
        clWallet.clickDelay { jumpARoute(RouteUrl.MyModule.ACTIVITY_MY_WALLET) }

        initScrollView()
        CoilUtils.loadRoundBorder(
            ivAvatar,
            "",
            15f,
            2f,
            ContextCompat.getColor(requireContext(), R.color.white)
        )

        initRv()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun initScrollView() {
        mBinding.scrollView.setOnScrollChangeListener { view: View, i: Int, i1: Int, i2: Int, i3: Int ->
            val location = IntArray(2)
            mBinding.ivAvatar.getLocationOnScreen(location)
            val locationY = location[1]
            var scale: Float = ((locationY - dp2px(52f).toFloat()) / dp2px(40f).toFloat()).toFloat()
            if (scale < 0) {
                scale = 0f
            }
            KLog.d("xujm", "locationY:$locationY===scale:$scale")
            mBinding.llTitle.background.alpha = (255 * (1 - scale)).toInt();
        }
    }

    override fun initObserve() {
        mViewModel.ldSuccess.observe(this) {
            when (it) {
                is DiamondBean -> {
                    if (it.recharge) {
                        mBinding.ivFirst.gone()
                        mBinding.tvDiamondNum.visible()
                        mBinding.tvDiamondNum.setText("${it.points}个钻石")
                    } else {
                        mBinding.ivFirst.visible()
                        mBinding.tvDiamondNum.gone()
                    }
                }

                is UnreadBean -> {
                    if (it.newNoticeTotal > 0) {
                        mBinding.ivRed.visible()
                    } else {
                        mBinding.ivRed.gone()
                    }
                }

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

                is Pair<*, *> -> {
                    val action = it.first
                    val position = it.second as Int
                    val favoriteCount = mAdapter.data.get(position).favoriteCount
                    mAdapter.data.get(position).isFavorite = if (action == 0) {
                        mAdapter.data.get(position).favoriteCount = favoriteCount - 1
                        false
                    } else {
                        mAdapter.data.get(position).favoriteCount = favoriteCount + 1
                        true
                    }
                    mAdapter.notifyItemChanged(position)
                }
            }
        }
    }

    override fun initRequestData() {

    }

    override fun onResume() {
        super.onResume()
        mViewModel.requestDiamond()
        mViewModel.requestUnRead()
        requestRefresh()
        setUserInfo()
    }


    fun setUserInfo() {
        val userInfo = SpHelper.getUserInfo()
        CoilUtils.loadRoundBorder(
            mBinding.ivAvatar,
            userInfo?.avatar ?: "",
            15f,
            2f,
            R.color.white.getResColor()
        )
        mBinding.ivGender.setImageResource(if (userInfo?.gender == 1) R.drawable.icon_bing_boy else R.drawable.icon_bing_girl)
        mBinding.tvName.setText(userInfo?.userName)
        if (userInfo?.age != 0) {
            mBinding.tvAge.visible()
            mBinding.tvAge.setText("${userInfo?.age}岁")
        } else {
            mBinding.tvAge.gone()
        }
        if (userInfo?.gender == 1) {
            mBinding.tvAge.setShapeSolidColor(R.color._698DEE.getResColor()).setUseShape()
        } else {
            mBinding.tvAge.setShapeSolidColor(R.color._F87585.getResColor()).setUseShape()
        }
        if (TextUtils.isEmpty(userInfo?.address)) {
            mBinding.tvLocation.gone()
        } else {
            mBinding.tvLocation.visible()
            val address = userInfo?.address!!.split("-")[1]
            mBinding.tvLocation.setText("${address}")
        }
        mBinding.tvFollowNum.setText("${userInfo?.fansCount}")
        mBinding.tvFocusNum.setText("${userInfo?.followCount}")

    }


    fun initRv() {
        mAdapter = MyThinkAdapter()
        mBinding.rvList.apply {
            addItemDecoration(
                MyColorDecoration(
                    0, 0, 0, dp2px(30f),
                    ContextCompat.getColor(context, R.color.white)
                )
            )
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = mAdapter
        }
        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.iv_zan -> {
                    val item = mAdapter.data.get(position)
                    val favorite = item.isFavorite
                    if (favorite) {
                        mViewModel.requestZan(item.id, 0, position)
                    } else {
                        mViewModel.requestZan(item.id, 1, position)
                    }
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
        mViewModel.requestList(mCursor, SpHelper.getUserInfo()?.userId!!)
    }
}