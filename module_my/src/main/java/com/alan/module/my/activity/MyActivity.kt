package com.alan.module.my.activity

import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.alan.module.my.R
import com.alan.module.my.databinding.ActivityMyBinding
import com.alan.module.my.viewmodol.MyViewModel
import com.alan.mvvm.base.coil.CoilUtils
import com.alan.mvvm.base.http.responsebean.DiamondBean
import com.alan.mvvm.base.ktx.*
import com.alan.mvvm.base.utils.jumpARoute
import com.alan.mvvm.common.constant.RouteUrl
import com.alan.mvvm.common.helper.SpHelper
import com.alan.mvvm.common.ui.BaseActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.socks.library.KLog
import dagger.hilt.android.AndroidEntryPoint

/**
 * 作者：alan
 * 时间：2021/7/30
 * 备注：我的页面
 */
@Route(path = RouteUrl.MyModule.ACTIVITY_MY_MY)
@AndroidEntryPoint
class MyActivity : BaseActivity<ActivityMyBinding, MyViewModel>() {

    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<MyViewModel>()

    /**
     * 初始化View
     */
    override fun ActivityMyBinding.initView() {
        ivBack.clickDelay { finish() }
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


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            initScrollView()
        }
        CoilUtils.loadRoundBorder(
            ivAvatar,
            "",
            15f,
            2f,
            R.color.white.getResColor()
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

    /**
     * 订阅数据
     */
    override fun initObserve() {
        mViewModel.ldSuccess.observe(this) {
            when (it) {
                is DiamondBean -> {
                    if (it.recharge) {
                        mBinding.ivFirst.gone()
                        mBinding.tvDiamondNum.visible()
                        mBinding.tvDiamondNum.setText("${it.points}个钻石")
                    } else {
                        mBinding.ivFirst.visible()
                        mBinding.tvDiamondNum.gone()
                    }
                }
            }
        }
    }

    /**
     * 获取数据
     */
    override fun initRequestData() {

    }

    override fun onResume() {
        super.onResume()
        mViewModel.requestDiamond()
        setUserInfo()
    }


    fun setUserInfo() {
        val userInfo = SpHelper.getUserInfo()
        CoilUtils.loadRoundBorder(
            mBinding.ivAvatar,
            userInfo?.avatar ?: "",
            15f,
            2f,
            R.color.white.getResColor()
        )
        mBinding.ivGender.setImageResource(if (userInfo?.gender == 1) R.drawable.icon_bing_boy else R.drawable.icon_bing_girl)
        mBinding.tvName.setText(userInfo?.userName)
        mBinding.tvAge.setText("${userInfo?.age}岁")
        if (TextUtils.isEmpty(userInfo?.address!!)) {
            mBinding.tvLocation.gone()
        } else {
            mBinding.tvLocation.visible()
            var address = userInfo?.address!!.split("-")[2]
            mBinding.tvLocation.setText("${address}")
        }
        mBinding.tvFollowNum.setText("${userInfo?.fansCount}")
        mBinding.tvFocusNum.setText("${userInfo?.followCount}")

    }
}