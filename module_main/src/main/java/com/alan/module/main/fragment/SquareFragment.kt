package com.alan.module.main.fragment

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.alan.module.main.databinding.FragmentSquareBinding
import com.alan.module.main.viewmodel.SquareViewModel
import com.alan.mvvm.common.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint


/**
 * 作者：alan
 * 时间：2021/7/28
 * 备注：
 */
@AndroidEntryPoint
class SquareFragment : BaseFragment<FragmentSquareBinding, SquareViewModel>() {
    companion object {
        @JvmStatic
        fun newInstance() =
            SquareFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override val mViewModel by viewModels<SquareViewModel>()


    override fun FragmentSquareBinding.initView() {

    }


    override fun initObserve() {

    }

    override fun initRequestData() {

    }


}