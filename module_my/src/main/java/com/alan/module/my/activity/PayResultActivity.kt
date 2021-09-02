package com.alan.module.my.activity

import androidx.activity.viewModels
import com.alan.module.my.R
import com.alan.module.my.databinding.ActivityPayResultBinding
import com.alan.module.my.viewmodol.PayResultViewModel
import com.alan.mvvm.base.http.responsebean.OrderBean
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.base.utils.NumberUtils
import com.alan.mvvm.common.constant.RouteUrl
import com.alan.mvvm.common.ui.BaseActivity
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import dagger.hilt.android.AndroidEntryPoint

/**
 * 作者：alan
 * 时间：2021/7/30
 * 备注：
 */
@Route(path = RouteUrl.MyModule.ACTIVITY_MY_PAYRESULT)
@AndroidEntryPoint
class PayResultActivity : BaseActivity<ActivityPayResultBinding, PayResultViewModel>() {

    @JvmField
    @Autowired
    var orderId: String = ""

    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<PayResultViewModel>()

    /**
     * 初始化View
     */
    override fun ActivityPayResultBinding.initView() {
        ivBack.clickDelay { finish() }
        tvHandle.clickDelay { finish() }

        tvWx.setText("微信：" + "zhishuoapp")
        tvOrder.setText("订单编号：$orderId")
    }

    /**
     * 订阅数据
     */
    override fun initObserve() {
        mViewModel.ldSuccess.observe(this) {
            when (it) {
                is OrderBean -> {
                    //支付状态:1: 未支付 2: 支付成功 3: 支付失败
                    mBinding.tvMoney.setText("${NumberUtils.getDoubleNum(it.totalFee)}")
                    mBinding.tvTime.setText("交易时间：${it.tradeTime}")
                    if (it.state == 2) {
                        mBinding.ivResult.setImageResource(R.drawable.icon_payresult_success)
                        mBinding.tvResult.setText("交易成功")
                        mBinding.tvHandle.setText("返回")
                    } else {
                        mBinding.ivResult.setImageResource(R.drawable.icon_payresult_error)
                        mBinding.tvResult.setText("交易失败")
                        mBinding.tvHandle.setText("继续支付")
                    }
                }
            }
        }
    }

    /**
     * 获取数据
     */
    override fun initRequestData() {
        mViewModel.requestOrderInfo(orderId)
    }
}