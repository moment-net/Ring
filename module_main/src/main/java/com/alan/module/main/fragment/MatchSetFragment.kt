package com.alan.module.main.fragment

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.viewModels
import com.alan.module.main.R
import com.alan.module.main.databinding.FragmentMatchSetBinding
import com.alan.module.main.viewmodel.MatchSetViewModel
import com.alan.mvvm.base.http.responsebean.AcceptBean
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.common.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint


/**
 * 作者：alan
 * 时间：2021/7/28
 * 备注：匹配设置页面
 */
@AndroidEntryPoint
class MatchSetFragment : BaseFragment<FragmentMatchSetBinding, MatchSetViewModel>() {

    override val mViewModel by viewModels<MatchSetViewModel>()
    var isAccept: Int = 0

    companion object {
        @JvmStatic
        fun newInstance() =
            MatchSetFragment().apply {
                arguments = Bundle().apply {}
            }
    }


    override fun FragmentMatchSetBinding.initView() {
        ivCheck.clickDelay {
            if (isAccept == 1) {
                mViewModel.requestMatchSet("0")
            } else {
                mViewModel.requestMatchSet("1")
            }
        }
    }

    override fun initObserve() {
        mViewModel.ldSuccess.observe(this) {
            when (it) {
                is AcceptBean -> {
                    if (TextUtils.equals(it.acceptAudio, "1")) {
                        isAccept = 1
                        change(true)
                    } else {
                        isAccept = 0
                        change(false)
                    }
                }

                is Boolean -> {
                    if (isAccept == 0) {
                        isAccept = 1
                        change(true)
                    } else {
                        isAccept = 0
                        change(false)
                    }
                }
            }
        }
    }

    override fun initRequestData() {
        mViewModel.requestSetInfo()
    }


    fun change(isCheck: Boolean) {
        if (isCheck) {
            mBinding.ivCheck.setImageResource(R.drawable.icon_check_on)
        } else {
            mBinding.ivCheck.setImageResource(R.drawable.icon_check_off)
        }
    }


}