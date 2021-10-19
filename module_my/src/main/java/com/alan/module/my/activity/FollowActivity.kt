package com.alan.module.my.activity

import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alan.module.my.R
import com.alan.module.my.adapter.FollowAdapter
import com.alan.module.my.databinding.ActivityFollowBinding
import com.alan.module.my.viewmodol.FollowViewModel
import com.alan.mvvm.base.http.baseresp.BaseResponse
import com.alan.mvvm.base.http.responsebean.UserInfoBean
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.base.ktx.dp2px
import com.alan.mvvm.base.ktx.getResColor
import com.alan.mvvm.base.utils.MyColorDecoration
import com.alan.mvvm.common.constant.RouteUrl
import com.alan.mvvm.common.ui.BaseActivity
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import dagger.hilt.android.AndroidEntryPoint

/**
 * 作者：alan
 * 时间：2021/7/30
 * 备注：
 */
@Route(path = RouteUrl.MyModule.ACTIVITY_MY_FOCUS)
@AndroidEntryPoint
class FollowActivity : BaseActivity<ActivityFollowBinding, FollowViewModel>() {

    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<FollowViewModel>()
    lateinit var mAdapter: FollowAdapter
    private var mCursor: Int = 0
    private var isLoad = false

    @JvmField
    @Autowired
    var type: String = "0"

    /**
     * 初始化View
     */
    override fun ActivityFollowBinding.initView() {
        ivBack.clickDelay { finish() }
        if (TextUtils.equals(type, "1")) {
            tvTitle.setText("我关注的")
        } else {
            tvTitle.setText("关注我的")
        }


        initRV()
    }

    private fun initRV() {
        mBinding.srfList.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                requestRefresh()
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                isLoad = true
                requestList()
            }
        })
        mAdapter = FollowAdapter()
        mBinding.rvFollow.apply {
            addItemDecoration(
                MyColorDecoration(
                    0, 0, 0, dp2px(25f),
                    ContextCompat.getColor(context, R.color.white)
                )
            )
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = mAdapter
        }
        mAdapter.setOnItemChildClickListener(object : OnItemChildClickListener {
            override fun onItemChildClick(
                adapter: BaseQuickAdapter<*, *>,
                view: View,
                position: Int
            ) {
                val userInfoBean: UserInfoBean = mAdapter.data.get(position)
                if (userInfoBean.followStatus == 0) {
                    mViewModel.requestChangeFollow(position, userInfoBean.userId, 1)
                } else {
                    mViewModel.requestChangeFollow(position, userInfoBean.userId, 0)
                }
            }
        })
        mAdapter.setEmptyView(TextView(this).apply {
            setText("暂无数据")
            setTextSize(16f)
            setTextColor(R.color._263A3A3A.getResColor())
            gravity = Gravity.CENTER
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        })
    }

    /**
     * 订阅数据
     */
    override fun initObserve() {
        mViewModel.ldSuccess.observe(this) {
            when (it) {
                is BaseResponse<*> -> {
                    mCursor = it.cursor
                    val list: ArrayList<UserInfoBean> =
                        (it.data ?: arrayListOf<UserInfoBean>()) as ArrayList<UserInfoBean>
                    if (isLoad) {
                        mBinding.srfList.finishLoadMore()
                        mAdapter.addData(list)
                    } else {
                        mBinding.srfList.finishRefresh()
                        mAdapter.setList(list)
                    }
                }

                is Int -> {
                    val tag = mAdapter.data.get(it).followStatus
                    if (tag == 0) {
                        mAdapter.data.get(it).followStatus = 1
                    } else {
                        mAdapter.data.get(it).followStatus = 0
                    }
                    mAdapter.notifyItemChanged(it)
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

    /**
     * 请求列表数据
     */
    fun requestList() {
        if (TextUtils.equals(type, "1")) {
            mViewModel.requestFollowList(mCursor);
        } else {
            mViewModel.requestFansList(mCursor);
        }
    }
}