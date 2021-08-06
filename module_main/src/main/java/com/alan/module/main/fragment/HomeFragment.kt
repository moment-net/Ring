package com.alan.module.main.fragment

import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alan.module.main.R
import com.alan.module.main.adapter.ManagerAdapter
import com.alan.module.main.databinding.FragmentHomeBinding
import com.alan.module.main.viewmodel.HomeViewModel
import com.alan.mvvm.base.ktx.dp2px
import com.alan.mvvm.base.utils.MyColorDecoration
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
    lateinit var mAdapter: ManagerAdapter

    override fun FragmentHomeBinding.initView() {
        initRV()
    }

    override fun initObserve() {

    }

    override fun initRequestData() {

    }


    private fun initRV() {
        mAdapter = ManagerAdapter()
        mBinding.rvManager.apply {
            addItemDecoration(
                MyColorDecoration(
                    0,
                    0,
                    0,
                    dp2px(10f),
                    ContextCompat.getColor(requireActivity(), R.color.white)
                )
            )
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = mAdapter
        }
        mAdapter.addData("")
        mAdapter.addData("")
        mAdapter.addData("")
        mAdapter.addData("")
        mAdapter.addData("")
    }
}
