package com.alan.module.main.dialog

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.alan.module.main.R
import com.alan.module.main.databinding.LayoutDialogFilterBinding
import com.alan.module.main.viewmodel.FilterViewModel
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.base.ktx.dp2px
import com.alan.mvvm.base.ktx.getResColor
import com.alan.mvvm.base.mvvm.v.BaseFrameDialogFragment
import com.alan.mvvm.base.utils.EventBusUtils
import com.alan.mvvm.common.event.RefreshEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilterFragmentDialog : BaseFrameDialogFragment<LayoutDialogFilterBinding, FilterViewModel>() {

    companion object {
        fun newInstance(gender: Int): FilterFragmentDialog {
            val bundle = Bundle()
            bundle.putInt("gender", gender)
            val dialog = FilterFragmentDialog()
            dialog.setArguments(bundle)
            return dialog
        }
    }

    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<FilterViewModel>()
    var gender: Int = 2

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
        params.width = dp2px(320f)
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        params.gravity = Gravity.CENTER
        setCanceledOnTouchOutside(false)
        isCancelable = false
        window.attributes = params
    }


    override fun LayoutDialogFilterBinding.initView() {
        arguments?.apply {
            gender = getInt("gender", 2)
            changeMatch(gender)
        }
        ivClose.clickDelay {
            dismiss()
        }

        tvBoy.clickDelay { changeMatch(1) }
        tvGirl.clickDelay { changeMatch(2) }
        tvAll.clickDelay { changeMatch(3) }

        tvCommit.clickDelay {
            mViewModel.requestMatchFilter("$gender")
        }
    }


    override fun initObserve() {
        mViewModel.ldData.observe(this) {
            EventBusUtils.postEvent(RefreshEvent("main"))
            dismiss()
        }
    }

    override fun initRequestData() {

    }

    /**
     * 更改匹配
     */
    fun changeMatch(position: Int) {
        mBinding.tvBoy.setTextColor(R.color._9C9C9C.getResColor())
        mBinding.tvBoy.setShapeSolidColor(R.color._F4F4F4.getResColor()).setUseShape()
        mBinding.tvGirl.setTextColor(R.color._9C9C9C.getResColor())
        mBinding.tvGirl.setShapeSolidColor(R.color._F4F4F4.getResColor()).setUseShape()
        mBinding.tvAll.setTextColor(R.color._9C9C9C.getResColor())
        mBinding.tvAll.setShapeSolidColor(R.color._F4F4F4.getResColor()).setUseShape()
        if (position == 1) {
            gender = 1
            mBinding.tvBoy.setTextColor(R.color.black.getResColor())
            mBinding.tvBoy.setShapeSolidColor(R.color._FFD54A.getResColor()).setUseShape()
        } else if (position == 2) {
            gender = 2
            mBinding.tvGirl.setTextColor(R.color.black.getResColor())
            mBinding.tvGirl.setShapeSolidColor(R.color._FFD54A.getResColor()).setUseShape()
        } else if (position == 3) {
            gender = 3
            mBinding.tvAll.setTextColor(R.color.black.getResColor())
            mBinding.tvAll.setShapeSolidColor(R.color._FFD54A.getResColor()).setUseShape()
        }
    }


}