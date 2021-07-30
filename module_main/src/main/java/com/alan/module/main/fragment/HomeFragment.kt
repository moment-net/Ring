package com.alan.module.main.fragment

import androidx.fragment.app.viewModels
import com.alan.module.main.databinding.FragmentHomeBinding
import com.alan.module.main.viewmodel.HomeViewModel
import com.alan.mvvm.common.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * 作者：alan
 * 时间：2021/7/28
 * 备注：测试fragment
 */
@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

    override val mViewModel by viewModels<HomeViewModel>()

    override fun FragmentHomeBinding.initView() {}

    override fun initObserve() {

    }

    override fun initRequestData() {

    }
}