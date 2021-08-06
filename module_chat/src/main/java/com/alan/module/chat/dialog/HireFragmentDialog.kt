package com.alan.module.chat.dialog

import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.alan.module.chat.R
import com.alan.module.chat.adapter.TimeAdapter
import com.alan.module.chat.databinding.LayoutHireBinding
import com.alan.module.chat.viewmodol.HireViewModel
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.base.ktx.dp2px
import com.alan.mvvm.base.mvvm.v.BaseFrameDialogFragment
import com.alan.mvvm.base.utils.MyGridItemDecoration
import dagger.hilt.android.AndroidEntryPoint

/**
 * 作者：alan
 * 时间：2021/8/4
 * 备注：聘请弹框
 */
@AndroidEntryPoint
class HireFragmentDialog : BaseFrameDialogFragment<LayoutHireBinding, HireViewModel>() {


    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<HireViewModel>()
    private lateinit var mAdapter: TimeAdapter

    override fun LayoutHireBinding.initView() {
        ivClose.clickDelay { dismiss() }
        tvPay.clickDelay { }
        initRV()
    }

    private fun initRV() {
        mAdapter = TimeAdapter()
        mBinding.rvTime.apply {
            addItemDecoration(
                MyGridItemDecoration(
                    dp2px(10f),
                    ContextCompat.getColor(requireActivity(), R.color.white)
                )
            )
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }

        mAdapter.addData("")
        mAdapter.addData("")
        mAdapter.addData("")
        mAdapter.addData("")
        mAdapter.addData("")
        mAdapter.addData("")
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
        setCanceledOnTouchOutside(true)
        isCancelable = true
        window.attributes = params
    }

    override fun initObserve() {
    }

    override fun initRequestData() {
    }


}