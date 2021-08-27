package com.alan.module.main.fragment

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.alan.module.main.R
import com.alan.module.main.databinding.FragmentMyBinding
import com.alan.module.main.viewmodel.MyViewModel
import com.alan.mvvm.base.coil.CoilUtils
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.base.ktx.dp2px
import com.alan.mvvm.base.utils.jumpARoute
import com.alan.mvvm.common.constant.RouteUrl
import com.alan.mvvm.common.ui.BaseFragment
import com.socks.library.KLog
import dagger.hilt.android.AndroidEntryPoint


/**
 * 作者：alan
 * 时间：2021/7/28
 * 备注：
 */
@AndroidEntryPoint
class MyFragment : BaseFragment<FragmentMyBinding, MyViewModel>() {

    override val mViewModel by viewModels<MyViewModel>()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun FragmentMyBinding.initView() {
        ivSet.clickDelay {
            jumpARoute(RouteUrl.MyModule.ACTIVITY_MY_SET)
        }
        ivMsg.clickDelay {
            jumpARoute(RouteUrl.MyModule.ACTIVITY_MY_MSG)
        }
        ivEdit.clickDelay {
            jumpARoute(RouteUrl.MyModule.ACTIVITY_MY_PERSONINFO)
        }
        tvFocusNum.clickDelay {
            val bundle = Bundle().apply { putString("type", "1") }
            jumpARoute(RouteUrl.MyModule.ACTIVITY_MY_FOCUS, bundle)
        }
        tvFollowNum.clickDelay {
            val bundle = Bundle().apply { putString("type", "2") }
            jumpARoute(RouteUrl.MyModule.ACTIVITY_MY_FOCUS, bundle)
        }
        clDiamond.clickDelay { jumpARoute(RouteUrl.MyModule.ACTIVITY_MY_DIAMOND) }
        clWallet.clickDelay { jumpARoute(RouteUrl.MyModule.ACTIVITY_MY_WALLET) }

        initScrollView()
        CoilUtils.loadRoundBorder(
            ivAvatar,
            "",
            15f,
            2f,
            ContextCompat.getColor(requireContext(), R.color.white)
        )
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun initScrollView() {
        mBinding.scrollView.setOnScrollChangeListener { view: View, i: Int, i1: Int, i2: Int, i3: Int ->
            val location = IntArray(2)
            mBinding.ivAvatar.getLocationOnScreen(location)
            val locationY = location[1]
            var scale: Float = ((locationY - dp2px(52f).toFloat()) / dp2px(40f).toFloat()).toFloat()
            if (scale < 0) {
                scale = 0f
            }
            KLog.d("xujm", "locationY:$locationY===scale:$scale")
            mBinding.llTitle.background.alpha = (255 * (1 - scale)).toInt();
        }
    }

    override fun initObserve() {

    }

    override fun initRequestData() {

    }
}