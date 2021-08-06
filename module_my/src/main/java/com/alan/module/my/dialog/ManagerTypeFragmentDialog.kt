package com.alan.module.my.dialog

import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.alan.module.my.R
import com.alan.module.my.adapter.SkilAdapter
import com.alan.module.my.databinding.LayoutDialogSkilBinding
import com.alan.module.my.viewmodol.ManagerTypeViewModel
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.base.ktx.dp2px
import com.alan.mvvm.base.mvvm.v.BaseFrameDialogFragment
import com.alan.mvvm.base.utils.MyGridItemDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ManagerTypeFragmentDialog :
    BaseFrameDialogFragment<LayoutDialogSkilBinding, ManagerTypeViewModel>() {


    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<ManagerTypeViewModel>()
    private lateinit var mAdapter: SkilAdapter

    override fun LayoutDialogSkilBinding.initView() {
        tvTitle.setText("管家类型")
        ivClose.clickDelay { dismiss() }
        tvCommit.clickDelay { }
        initRV()
    }

    private fun initRV() {
        mAdapter = SkilAdapter()
        mBinding.rvSkil.apply {
            addItemDecoration(
                MyGridItemDecoration(
                    dp2px(10f),
                    ContextCompat.getColor(requireActivity(), R.color.white)
                )
            )
            layoutManager = GridLayoutManager(context, 2)
            adapter = mAdapter
        }
        mAdapter.addData("")
        mAdapter.addData("")
        mAdapter.addData("")
        mAdapter.addData("")
        mAdapter.addData("")
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