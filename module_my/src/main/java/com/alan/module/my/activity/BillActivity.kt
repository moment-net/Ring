package com.alan.module.my.activity

import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.alan.module.my.adapter.BillVPAdapter
import com.alan.module.my.databinding.ActivityBillBinding
import com.alan.module.my.fragment.IncomeListFragment
import com.alan.module.my.fragment.WithDrawListFragment
import com.alan.module.my.viewmodol.BIllViewModel
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.common.constant.RouteUrl
import com.alan.mvvm.common.ui.BaseActivity
import com.alibaba.android.arouter.facade.annotation.Route
import dagger.hilt.android.AndroidEntryPoint

/**
 * 作者：alan
 * 时间：2021/7/30
 * 备注：我的账单
 */
@Route(path = RouteUrl.MyModule.ACTIVITY_MY_BILL)
@AndroidEntryPoint
class BillActivity : BaseActivity<ActivityBillBinding, BIllViewModel>() {

    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<BIllViewModel>()
    private val mFragments = arrayListOf<Fragment>()
    private var mPagePosition = 0

    /**
     * 初始化View
     */
    override fun ActivityBillBinding.initView() {
        ivBack.clickDelay { finish() }
        tvIncome.clickDelay { clickTab(0) }
        tvWithdraw.clickDelay { clickTab(1) }

        initFragment()
    }

    /**
     * 初始化fragment
     */
    private fun initFragment() {
        if (mFragments.isNotEmpty()) {
            mFragments.clear()
        }
        mFragments.add(IncomeListFragment())
        mFragments.add(WithDrawListFragment())

        mBinding.vpAnswer.adapter = BillVPAdapter(mFragments, supportFragmentManager, lifecycle)
        mBinding.vpAnswer.isUserInputEnabled = false
        mBinding.vpAnswer.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                clickTab(position)
            }
        })
    }


    /**
     * 点击tab切换
     * type:0、已回答；1、未回答
     */
    fun clickTab(mType: Int) {
        if (mType == 0) {
            mBinding.ivIncome.setVisibility(View.VISIBLE)
            mBinding.ivWithdraw.setVisibility(View.INVISIBLE)
            mBinding.vpAnswer.setCurrentItem(0)
        } else {
            mBinding.ivIncome.setVisibility(View.INVISIBLE)
            mBinding.ivWithdraw.setVisibility(View.VISIBLE)
            mBinding.vpAnswer.setCurrentItem(1)
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