package com.alan.module.my.activity

import android.os.Bundle
import androidx.activity.viewModels
import com.alan.module.my.databinding.ActivityWalletBinding
import com.alan.module.my.viewmodol.WalletViewModel
import com.alan.mvvm.base.http.apiservice.HttpBaseUrlConstant
import com.alan.mvvm.base.http.requestbean.AccountBean
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.base.utils.jumpARoute
import com.alan.mvvm.common.constant.RouteUrl
import com.alan.mvvm.common.ui.BaseActivity
import com.alibaba.android.arouter.facade.annotation.Route
import dagger.hilt.android.AndroidEntryPoint

/**
 * 作者：alan
 * 时间：2021/7/30
 * 备注：
 */
@Route(path = RouteUrl.MyModule.ACTIVITY_MY_WALLET)
@AndroidEntryPoint
class WalletActivity : BaseActivity<ActivityWalletBinding, WalletViewModel>() {

    lateinit var accountBean: AccountBean

    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<WalletViewModel>()

    /**
     * 初始化View
     */
    override fun ActivityWalletBinding.initView() {
        ivBack.clickDelay { finish() }
        tvRight.clickDelay {
            val bundle = Bundle().apply {
                putString("webUrl", HttpBaseUrlConstant.BASE_URL + "page/QnA")
                putString("webTitle", "常见问题")
            }
            jumpARoute(RouteUrl.WebModule.ACTIVITY_WEB_WEB, bundle)
        }
        tvBill.clickDelay {
            jumpARoute(RouteUrl.MyModule.ACTIVITY_MY_BILL)
        }
        tvWithdraw.clickDelay {
            val bundle = Bundle().apply {
                putParcelable("bean", accountBean)
            }
            jumpARoute(RouteUrl.MyModule.ACTIVITY_MY_WITHDRAW, bundle)
        }
    }

    /**
     * 订阅数据
     */
    override fun initObserve() {
        mViewModel.ldSuccess.observe(this) {
            when (it) {
                is AccountBean -> {
                    accountBean = it
                    mBinding.tvNum.setText("${it.assetsCount}")
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
        mViewModel.requestAccount()
    }
}