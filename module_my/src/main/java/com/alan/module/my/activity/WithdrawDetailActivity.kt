package com.alan.module.my.activity

import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alan.module.my.adapter.WithDrawDetailAdapter
import com.alan.module.my.databinding.ActivityWithdrawDetailBinding
import com.alan.module.my.viewmodol.WithdrawDetailViewModel
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.common.constant.RouteUrl
import com.alan.mvvm.common.ui.BaseActivity
import com.alibaba.android.arouter.facade.annotation.Route
import dagger.hilt.android.AndroidEntryPoint

/**
 * 作者：alan
 * 时间：2021/7/30
 * 备注：
 */
@Route(path = RouteUrl.MyModule.ACTIVITY_MY_WITHDRAWDETAIL)
@AndroidEntryPoint
class WithdrawDetailActivity :
    BaseActivity<ActivityWithdrawDetailBinding, WithdrawDetailViewModel>() {

    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<WithdrawDetailViewModel>()
    lateinit var mAdapter: WithDrawDetailAdapter

    /**
     * 初始化View
     */
    override fun ActivityWithdrawDetailBinding.initView() {
        ivBack.clickDelay { finish() }
        initRV()
    }

    private fun initRV() {
        mAdapter = WithDrawDetailAdapter()
        mBinding.rvWithdraw.apply {
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
}