package com.alan.module.home.fragment

import androidx.fragment.app.viewModels
import com.alan.mvvm.common.ui.BaseFragment
import com.alan.module.home.databinding.HomeFragmentInternalLayoutBinding
import com.alan.module.home.viewmodel.InternalViewModel
import com.alan.mvvm.base.utils.StateLayoutEnum
import dagger.hilt.android.AndroidEntryPoint

/**
 * 作者：alan
 * 时间：2021/7/28
 * 备注：测试fragment
 */
@AndroidEntryPoint
class InternalFragment : BaseFragment<HomeFragmentInternalLayoutBinding, InternalViewModel>() {

    override val mViewModel by viewModels<InternalViewModel>()

    override fun HomeFragmentInternalLayoutBinding.initView() {}

    override fun initObserve() {
        mViewModel.recreatedCont.observe(viewLifecycleOwner) {
            mBinding.recreateContTv.text = "重建次数 $it"
        }
        mViewModel.firstData.observe(viewLifecycleOwner) {
            mBinding.loadDataTv.text = it
        }
        mViewModel.stateViewLD.observe(viewLifecycleOwner) {
            mBinding.loadingStatusTv.text = if (it== StateLayoutEnum.LOADING) {
                "正在加载..."
            } else {
                "加载完成！"
            }
        }
    }

    override fun initRequestData() {
        //每次重建都会累加数据
        mViewModel.increase()
        //当页面重建的时候不再重新请求数据，且当前页面数据数据有且没有刷新逻辑的情况下不再请求数据。
        if (isRecreate() && mViewModel.firstData.value != null) {
            return
        }
        mViewModel.getData()
    }
}