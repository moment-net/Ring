package com.alan.module.my.dialog

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.alan.module.my.R
import com.alan.module.my.adapter.CardInfoAdapter
import com.alan.module.my.databinding.LayoutDialogCardinfoBinding
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.base.ktx.dp2px
import com.alan.mvvm.base.mvvm.v.BaseFrameDialogFragment
import com.alan.mvvm.base.mvvm.vm.EmptyViewModel
import com.alan.mvvm.base.utils.MyGridItemDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CardInfoFragmentDialog :
    BaseFrameDialogFragment<LayoutDialogCardinfoBinding, EmptyViewModel>() {

    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<EmptyViewModel>()
    lateinit var mAdapter: CardInfoAdapter

    companion object {
        fun newInstance(): CardInfoFragmentDialog {
            val args = Bundle()
            val fragment = CardInfoFragmentDialog()
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
        params.width = dp2px(300f)
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        params.gravity = Gravity.CENTER
        setCanceledOnTouchOutside(false)
        isCancelable = false
        window.attributes = params
    }

    override fun LayoutDialogCardinfoBinding.initView() {
        ivClose.clickDelay { dismiss() }

        initRV()
    }


    override fun initObserve() {

    }

    override fun initRequestData() {
    }

    fun initRV() {
        mAdapter = CardInfoAdapter()
        mBinding.rvList.apply {
            addItemDecoration(
                MyGridItemDecoration(
                    dp2px(25f),
                    ContextCompat.getColor(requireActivity(), R.color.white)
                )
            )
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }
        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.tv_copy -> {

                }
            }
        }
    }


}