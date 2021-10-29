package com.alan.module.my.dialog

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.alan.module.my.R
import com.alan.module.my.adapter.CardAdapter
import com.alan.module.my.databinding.LayoutDialogCardBinding
import com.alan.module.my.viewmodol.CardSetViewModel
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.base.ktx.dp2px
import com.alan.mvvm.base.mvvm.v.BaseFrameDialogFragment
import com.alan.mvvm.base.utils.MyGridItemDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CardSetFragmentDialog :
    BaseFrameDialogFragment<LayoutDialogCardBinding, CardSetViewModel>() {

    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<CardSetViewModel>()
    lateinit var mAdapter: CardAdapter

    companion object {
        fun newInstance(): CardSetFragmentDialog {
            val args = Bundle()
            val fragment = CardSetFragmentDialog()
            fragment.arguments = args
            return fragment
        }
    }

    override fun initWindow() {
        super.initWindow()
        val window = dialog!!.window
        val params = window!!.attributes
        val colorDrawable = ColorDrawable(
            ContextCompat.getColor(
                mActivity, R.color.transparent
            )
        )
        window.setBackgroundDrawable(colorDrawable)
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        params.gravity = Gravity.BOTTOM
        setCanceledOnTouchOutside(false)
        isCancelable = false
        window.attributes = params
    }

    override fun LayoutDialogCardBinding.initView() {
        ivClose.clickDelay { dismiss() }
        tvCommit.clickDelay { dismiss() }

        initRV()
    }


    override fun initObserve() {

    }

    override fun initRequestData() {
    }

    fun initRV() {
        mAdapter = CardAdapter()
        mBinding.rvList.apply {
            addItemDecoration(
                MyGridItemDecoration(
                    dp2px(20f),
                    ContextCompat.getColor(requireActivity(), R.color.white)
                )
            )
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }
        mAdapter.setOnItemClickListener { adapter, view, position ->

        }
    }


}