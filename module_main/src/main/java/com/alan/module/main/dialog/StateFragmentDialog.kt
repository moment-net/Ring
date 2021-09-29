package com.alan.module.main.dialog

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.alan.module.main.R
import com.alan.module.main.adapter.MainVPAdapter
import com.alan.module.main.databinding.LayoutDialogStateBinding
import com.alan.module.main.fragment.MatchSetFragment
import com.alan.module.main.fragment.SelectStateFragment
import com.alan.module.main.viewmodel.StateViewModel
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.base.ktx.dp2px
import com.alan.mvvm.base.ktx.gone
import com.alan.mvvm.base.ktx.visible
import com.alan.mvvm.base.mvvm.v.BaseFrameDialogFragment
import com.alan.mvvm.base.utils.EventBusUtils
import com.alan.mvvm.common.event.RefreshEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StateFragmentDialog : BaseFrameDialogFragment<LayoutDialogStateBinding, StateViewModel>() {

    companion object {
        fun newInstance(): StateFragmentDialog {
            val bundle = Bundle()
            val dialog = StateFragmentDialog()
            dialog.setArguments(bundle)
            return dialog
        }
    }

    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<StateViewModel>()



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


    override fun LayoutDialogStateBinding.initView() {
        ivClose.clickDelay {
            EventBusUtils.postEvent(RefreshEvent("main"))
            dismiss()
        }
        ivBack.clickDelay {
            viewpager.setCurrentItem(0)
        }
        initViewPager()
    }


    fun initViewPager() {
        val mFragments = arrayListOf<Fragment>()
        val selectStateFragment = SelectStateFragment.newInstance()
        selectStateFragment.listener = object : SelectStateFragment.OnStateClickListener {
            override fun onClickSet() {
                mBinding.viewpager.setCurrentItem(1)
            }

            override fun onClickCommit() {
                mBinding.ivClose.performClick()
            }
        }
        mFragments.add(selectStateFragment)
        mFragments.add(MatchSetFragment.newInstance())

        mBinding.viewpager.adapter =
            MainVPAdapter(mFragments, requireActivity().supportFragmentManager, lifecycle)
        mBinding.viewpager.isUserInputEnabled = false
        mBinding.viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == 0) {
                    mBinding.tvTitle.setText("请选择你的干饭状态")
                    mBinding.ivBack.gone()
                    mBinding.ivClose.visible()
                } else {
                    mBinding.tvTitle.setText("匹配设置")
                    mBinding.ivBack.visible()
                    mBinding.ivClose.gone()
                }
            }
        })

    }


    override fun initObserve() {
    }

    override fun initRequestData() {

    }


}