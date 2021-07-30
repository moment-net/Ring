package com.alan.module.home.activity

import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.alan.mvvm.base.mvvm.vm.EmptyViewModel
import com.alan.mvvm.common.ui.BaseActivity
import com.alan.module.home.databinding.HomeActivityInternalLayoutBinding
import com.alan.module.home.fragment.InternalFragment
import com.alan.mvvm.common.constant.RouteUrl
import com.alibaba.android.arouter.facade.annotation.Route
import dagger.hilt.android.AndroidEntryPoint

/**
 * 作者：alan
 * 时间：2021/7/28
 * 备注：ViewPager2+fragment 模拟Fragment页面重建。
 */
@Route(path = RouteUrl.MainModule.RING_ACTIVITY_MAIN)
@AndroidEntryPoint
class InternalPagerActivity : BaseActivity<HomeActivityInternalLayoutBinding, EmptyViewModel>() {

    override val mViewModel: EmptyViewModel by viewModels()

    override fun HomeActivityInternalLayoutBinding.initView() {
        initPager()
    }

    private fun initPager() {
        val fragments = mutableListOf<Fragment>(
            InternalFragment(),
            InternalFragment(),
            InternalFragment(),
            InternalFragment(),
            InternalFragment(),
            InternalFragment(),
            InternalFragment()
        )
        mBinding.viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = fragments.size
            override fun createFragment(position: Int): Fragment = fragments[position]
        }

    }

    override fun initObserve() {}

    override fun initRequestData() {}
}