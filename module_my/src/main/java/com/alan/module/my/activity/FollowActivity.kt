package com.alan.module.my.activity

import android.text.TextUtils
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alan.module.my.R
import com.alan.module.my.adapter.FollowAdapter
import com.alan.module.my.databinding.ActivityFollowBinding
import com.alan.module.my.viewmodol.FollowViewModel
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.base.ktx.dp2px
import com.alan.mvvm.base.utils.MyColorDecoration
import com.alan.mvvm.common.constant.RouteUrl
import com.alan.mvvm.common.ui.BaseActivity
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
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
    private var cursor: Long = 0
    private val hasMore = false
    private var isLoad = false

    @Autowired
    lateinit var type: String

    /**
     * 初始化View
     */
    override fun ActivityFollowBinding.initView() {
        ivBack.clickDelay { finish() }
        initRV()
    }

    private fun initRV() {
        mBinding.srfList.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                cursor = 0
                isLoad = false
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                if (hasMore) {
                    isLoad = true
                } else {
                    mBinding.srfList.finishLoadMore()
                }
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
    }

    /**
     * 订阅数据
     */
    override fun initObserve() {

    }

    /**
     * 获取数据
     */
    override fun initRequestData() {

    }


    /**
     * 请求列表数据
     */
    fun requestList() {
        if (TextUtils.equals(type, "1")) {
            requestFollowList();
        } else {
            requestFansList();
        }
    }

    /**
     * 更改关注与被关注
     */
    fun requestFollow() {
//        viewModel.requestChangeFollow(userId, tag, new RequestHandler<HeartBaseResponse<ChangeFollowBean>>() {
//            @Override
//            public void onError(String message) {
//                ToastUtil.showToast(mActivity, message);
//            }
//
//            @Override
//            public void onSucceed(HeartBaseResponse<ChangeFollowBean> response) {
//                if (response.getResultCode() == 0) {
//                    mAdapter.getData().get(position).setFollowStatus(response.getData().getFollowStatus());
//                    mAdapter.notifyItemChanged(position);
//                }
//            }
//
//        });
    }


    fun requestFollowList() {
//        viewModel.requestFollowList(cursor, new RequestHandler<HeartBaseResponse<ArrayList<UserInfoBean>>>() {
//
//
//            @Override
//            public void onError(String message) {
//                ToastUtil.showToast(mActivity, message);
//                binding.srfFollow.finishRefresh();
//            }
//
//            @Override
//            public void onSucceed(HeartBaseResponse<ArrayList<UserInfoBean>> response) {
//                if (response.getResultCode() == 0) {
//                    cursor = response.getCursor();
//                    hasMore = response.isHasMore();
//                    ArrayList<UserInfoBean> data = response.getData();
//                    if (isLoad) {
//                        binding.srfFollow.finishLoadMore();
//                        mAdapter.addData(data);
//                    } else {
//                        binding.srfFollow.finishRefresh();
//                        mAdapter.setList(data);
//                    }
//                }
//                mAdapter.setEmptyView(R.layout.item_null);
//            }
//        });
    }

    fun requestFansList() {
//        viewModel.requestFansList(cursor, new RequestHandler<HeartBaseResponse<ArrayList<UserInfoBean>>>() {
//
//
//            @Override
//            public void onError(String message) {
//                ToastUtil.showToast(mActivity, message);
//                binding.srfFollow.finishRefresh();
//            }
//
//            @Override
//            public void onSucceed(HeartBaseResponse<ArrayList<UserInfoBean>> response) {
//                if (response.getResultCode() == 0) {
//                    cursor = response.getCursor();
//                    hasMore = response.isHasMore();
//                    ArrayList<UserInfoBean> data = response.getData();
//                    if (isLoad) {
//                        binding.srfFollow.finishLoadMore();
//                        mAdapter.addData(data);
//                    } else {
//                        binding.srfFollow.finishRefresh();
//                        mAdapter.setList(data);
//                    }
//                }
//                mAdapter.setEmptyView(R.layout.item_null);
//            }
//        });
    }
}