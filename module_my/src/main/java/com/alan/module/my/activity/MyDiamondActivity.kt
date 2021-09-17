package com.alan.module.my.activity

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alan.module.my.R
import com.alan.module.my.adapter.DiamondListAdapter
import com.alan.module.my.databinding.ActivityMyDiamondBinding
import com.alan.module.my.dialog.PayFragmentDialog
import com.alan.module.my.viewmodol.MyDiamondViewModel
import com.alan.mvvm.base.http.apiservice.HttpBaseUrlConstant
import com.alan.mvvm.base.http.baseresp.BaseResponse
import com.alan.mvvm.base.http.responsebean.DiamondBean
import com.alan.mvvm.base.http.responsebean.GoodBean
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.base.ktx.dp2px
import com.alan.mvvm.base.utils.MyColorDecoration
import com.alan.mvvm.base.utils.jumpARoute
import com.alan.mvvm.common.constant.RouteUrl
import com.alan.mvvm.common.ui.BaseActivity
import com.alibaba.android.arouter.facade.annotation.Route
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

/**
 * 作者：alan
 * 时间：2021/7/30
 * 备注：
 */
@Route(path = RouteUrl.MyModule.ACTIVITY_MY_DIAMOND)
@AndroidEntryPoint
class MyDiamondActivity : BaseActivity<ActivityMyDiamondBinding, MyDiamondViewModel>() {

    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<MyDiamondViewModel>()
    lateinit var mAdapter: DiamondListAdapter
    lateinit var diamondBean: DiamondBean
    /**
     * 初始化View
     */
    override fun ActivityMyDiamondBinding.initView() {
        ivBack.clickDelay { finish() }
        tvRight.clickDelay { jumpARoute(RouteUrl.MyModule.ACTIVITY_MY_PAYRECORD) }

        val spanText = SpannableString("已阅读并同意《充值服务协议》，充值成功后到账可能会存在延迟，有问题请查看《常见问题》")
        spanText.setSpan(object : ClickableSpan() {

            @RequiresApi(Build.VERSION_CODES.M)
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                //设置文件颜色
                ds.color = getColor(R.color._4FBDFF)
                //设置下划线
                ds.isUnderlineText = false
            }

            override fun onClick(view: View) {
                val bundle = Bundle().apply {
                    putString("webUrl", HttpBaseUrlConstant.BASE_URL + "page/consume-policy")
                    putString("webTitle", "充值协议")
                }
                jumpARoute(RouteUrl.WebModule.ACTIVITY_WEB_WEB, bundle)
            }
        }, 6, 14, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spanText.setSpan(object : ClickableSpan() {
            @RequiresApi(Build.VERSION_CODES.M)
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                //设置文件颜色
                ds.color = getColor(R.color._4FBDFF)
                //设置下划线
                ds.isUnderlineText = false
            }

            override fun onClick(view: View) {
                val bundle = Bundle().apply {
                    putString("webUrl", HttpBaseUrlConstant.BASE_URL + "page/QnA")
                    putString("webTitle", "常见问题")
                }
                jumpARoute(RouteUrl.WebModule.ACTIVITY_WEB_WEB, bundle)
            }
        }, spanText.length - 6, spanText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        tvAgreement.setHighlightColor(Color.TRANSPARENT) //设置点击后的颜色为透明，否则会一直出现高亮
        tvAgreement.setText(spanText)
        tvAgreement.setMovementMethod(LinkMovementMethod.getInstance()) //开始响应点击事件

        initRV()
    }

    private fun initRV() {
        mAdapter = DiamondListAdapter()
        mBinding.rvDiamond.apply {
            addItemDecoration(
                MyColorDecoration(
                    0, 0, 0, dp2px(10f),
                    ContextCompat.getColor(context, R.color.white)
                )
            )
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = mAdapter
        }
        mAdapter.setOnItemClickListener { adapter, view, position ->
            val goodBean: GoodBean = mAdapter.data.get(position)
            val payDialog: PayFragmentDialog =
                PayFragmentDialog.newInstance(diamondBean.recharge, goodBean)
            payDialog.show(supportFragmentManager)
        }
    }

    /**
     * 订阅数据
     */
    override fun initObserve() {
        mViewModel.ldSuccess.observe(this) {
            when (it) {
                is DiamondBean -> {
                    diamondBean = it
                    mBinding.tvNum.setText("${it.points}")
                }

                is BaseResponse<*> -> {
                    val list: ArrayList<GoodBean> = it.data as ArrayList<GoodBean>
                    mAdapter.setList(list)
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
        mViewModel.requestGoodsList()
    }
}