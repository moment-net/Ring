package com.alan.module.my.dialog

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.alan.module.my.R
import com.alan.module.my.databinding.LayoutDialogPayBinding
import com.alan.module.my.viewmodol.PayViewModel
import com.alan.mvvm.base.http.apiservice.HttpBaseUrlConstant
import com.alan.mvvm.base.http.responsebean.GoodBean
import com.alan.mvvm.base.http.responsebean.OrderBean
import com.alan.mvvm.base.http.responsebean.PayResultBean
import com.alan.mvvm.base.http.responsebean.PrepayBean
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.base.ktx.dp2px
import com.alan.mvvm.base.mvvm.v.BaseFrameDialogFragment
import com.alan.mvvm.base.utils.EventBusRegister
import com.alan.mvvm.base.utils.jumpARoute
import com.alan.mvvm.base.utils.toast
import com.alan.mvvm.common.constant.Constants
import com.alan.mvvm.common.constant.RouteUrl
import com.alan.mvvm.common.dialog.DialogHelper
import com.alan.mvvm.common.event.WXPayEvent
import com.alan.mvvm.common.helper.PayHelper
import com.alan.mvvm.common.helper.SpHelper
import com.alan.mvvm.common.report.DataPointUtil
import com.lxj.xpopup.core.BasePopupView
import com.socks.library.KLog
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

@EventBusRegister
@AndroidEntryPoint
class PayFragmentDialog : BaseFrameDialogFragment<LayoutDialogPayBinding, PayViewModel>() {


    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<PayViewModel>()
    var payType = 1 //0是未选择；1是微信；2是支付宝
    lateinit var orderBean: OrderBean
    lateinit var goodBean: GoodBean
    lateinit var showMultipleDialog: BasePopupView
    var isRecharge = false
    lateinit var api: IWXAPI


    companion object {
        fun newInstance(isRecharge: Boolean, goodBean: GoodBean): PayFragmentDialog {
            val bundle = Bundle()
            bundle.putBoolean("isRecharge", isRecharge);
            bundle.putParcelable("bean", goodBean);
            val dialog = PayFragmentDialog()
            dialog.setArguments(bundle)
            return dialog
        }
    }


    override fun LayoutDialogPayBinding.initView() {
        ivClose.clickDelay {
            DataPointUtil.reportDiamondCancel(SpHelper.getUserInfo()?.userId!!, goodBean.goodsId!!)
            showClose()
        }
        ivWx.clickDelay {
            payType = 1
            ivWx.setImageResource(R.drawable.icon_pay_checkon)
            ivZfb.setImageResource(R.drawable.icon_pay_checkoff)
        }
        ivZfb.clickDelay {
            payType = 2
            ivWx.setImageResource(R.drawable.icon_pay_checkoff)
            ivZfb.setImageResource(R.drawable.icon_pay_checkon)
        }
        tvCommit.clickDelay {
            mViewModel.requestOrder(goodBean?.goodsId!!);
        }

        isRecharge = arguments?.getBoolean("isRecharge", false)!!
        goodBean = arguments?.getParcelable("bean")!!
        DataPointUtil.reportBuyDiamond(SpHelper.getUserInfo()?.userId!!, goodBean.goodsId!!)

        tvMoney.setText("${goodBean?.amount!!}");

        regToWx()
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
        params.width = dp2px(360f)
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        params.gravity = Gravity.BOTTOM
        setCanceledOnTouchOutside(true)
        isCancelable = true
        window.attributes = params
    }

    override fun initObserve() {
        mViewModel.ldSuccess.observe(this) {
            when (it) {
                is OrderBean -> {
                    orderBean = it
                    if (payType == 1) {
                        mViewModel.requestPayWX(it.orderId)
                    } else if (payType == 2) {
                        mViewModel.requestPayZFB(it.orderId)
                    }
                }

                is PrepayBean -> {
                    requestWxPay(it)
                    showPayResult()
                }

                is String -> {
                    requestZFBPay(it)
                    showPayResult()
                }
            }
        }

    }

    override fun initRequestData() {
    }

    //获取微信Code
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun handleWXCode(event: WXPayEvent) {
        KLog.e("xujm", "WXPayEvent支付结果：" + event.code)
        showMultipleDialog.dismiss()
        if (event.code == 0) {
            dismiss()
            val bundle = Bundle().apply {
                putString("orderId", orderBean.orderId)
            }
            jumpARoute(RouteUrl.MyModule.ACTIVITY_MY_PAYRESULT, bundle)
        } else if (event.code == -2) {
            toast("支付已取消")
        }
    }

    /**
     * 显示支付中弹框
     */
    fun showClose() {
        val content: String
        content = if (isRecharge) {
            "确定要放弃充值？"
        } else {
            "确定要放弃充值？首次充值可享受新人首充有礼优惠"
        }
        DialogHelper.showMultipleDialog(mActivity, content, "确定放弃", "取消", {
            dismiss()
        }, {

        })
    }

    /**
     * 显示支付中弹框
     */
    fun showPayResult() {
        showMultipleDialog =
            DialogHelper.showMultipleDialog(mActivity, "正在等待支付结果...", "支付成功", "支付遇到问题", {
                val bundle = Bundle().apply {
                    putString("orderId", if (orderBean == null) "" else orderBean.orderId)
                }
                jumpARoute(RouteUrl.MyModule.ACTIVITY_MY_PAYRESULT, bundle)
            }, {
                val bundle = Bundle().apply {
                    putString("webUrl", HttpBaseUrlConstant.BASE_URL + "page/QnA")
                    putString("webTitle", "常见问题")
                }
                jumpARoute(RouteUrl.WebModule.ACTIVITY_WEB_WEB, bundle)
            })
    }


    //注册微信
    fun regToWx() {
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(mActivity, Constants.APP_ID, true)
        // 将应用的appId注册到微信
        api.registerApp(Constants.APP_ID)
    }

    /**
     * 调起微信支付
     */
    fun requestWxPay(prepayBean: PrepayBean) {
        var request = PayReq()
        request.appId = prepayBean.appid
        request.partnerId = prepayBean.partnerid
        request.prepayId = prepayBean.prepay_id
        request.packageValue = prepayBean.packageX
        request.nonceStr = prepayBean.noncestr
        request.timeStamp = prepayBean.timestamp
        request.sign = prepayBean.sign
        api.sendReq(request);
    }


    /**
     * 调起支付宝
     */
    fun requestZFBPay(orderInfo: String) {
        if (TextUtils.isEmpty(orderInfo)) {
            return
        }
        lifecycleScope.launch {
            PayHelper.requestPayResult(mActivity, orderInfo)
                .collect {
                    val payResult = PayResultBean(it);

                    //对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                    val resultStatus: String = payResult.resultStatus!!
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        dismiss();
                        val bundle = Bundle().apply {
                            putString("orderId", orderBean.orderId)
                        }
                        jumpARoute(RouteUrl.MyModule.ACTIVITY_MY_PAYRESULT, bundle)
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        toast("支付已取消")
                    }
                }
        }
    }

}