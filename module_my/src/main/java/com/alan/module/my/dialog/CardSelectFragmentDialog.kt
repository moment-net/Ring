package com.alan.module.my.dialog

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.alan.module.my.R
import com.alan.module.my.adapter.LabelAdapter
import com.alan.module.my.databinding.LayoutDialogCardSelectBinding
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.base.ktx.dp2px
import com.alan.mvvm.base.mvvm.v.BaseFrameDialogFragment
import com.alan.mvvm.base.mvvm.vm.EmptyViewModel
import com.alan.mvvm.base.utils.MyGridItemDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CardSelectFragmentDialog :
    BaseFrameDialogFragment<LayoutDialogCardSelectBinding, EmptyViewModel>() {

    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<EmptyViewModel>()
    lateinit var mAdapter: LabelAdapter

    companion object {
        fun newInstance(): CardSelectFragmentDialog {
            val args = Bundle()
            val fragment = CardSelectFragmentDialog()
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

    override fun LayoutDialogCardSelectBinding.initView() {
        ivBack.clickDelay { dismiss() }

        initRV()
    }


    override fun initObserve() {

    }

    override fun initRequestData() {
    }

    fun initRV() {
        mAdapter = LabelAdapter()
        mBinding.rvList.apply {
            addItemDecoration(
                MyGridItemDecoration(
                    dp2px(20f),
                    ContextCompat.getColor(requireActivity(), R.color.white)
                )
            )
            layoutManager = GridLayoutManager(requireActivity(), 3)
            adapter = mAdapter
        }
        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.tv_title -> {
                    val title = mAdapter.data.get(position)
                    if (mAdapter.chooseList.contains(title)) {
                        mAdapter.chooseList.remove(title)
                    } else {
                        mAdapter.chooseList.add(title)
                    }
                    mAdapter.notifyDataSetChanged()
                }
            }
        }
    }


}