package com.alan.module.my.adapter

import android.text.TextUtils
import android.widget.TextView
import com.alan.module.my.R
import com.alan.mvvm.base.http.responsebean.WithdrawBean
import com.alan.mvvm.base.ktx.getResColor
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class WithDrawAdapter : BaseQuickAdapter<WithdrawBean, BaseViewHolder>(R.layout.item_skil) {

    override fun convert(holder: BaseViewHolder, item: WithdrawBean) {
        holder.setText(R.id.tv_name, "提现-微信：" + item.wxName)
        holder.setText(R.id.tv_time, item.operateTime)
        holder.setText(R.id.tv_money, "-${item.cashCount}元")
        val tv_result: TextView = holder.getView<TextView>(R.id.tv_result)
        if (TextUtils.equals(item.status, "-1")) {
            tv_result.text = "提现失败"
            tv_result.setTextColor(R.color._FF6767.getResColor())
            tv_result.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.icon_withdraw_fail,
                0
            )
        } else if (TextUtils.equals(item.status, "0")) {
            tv_result.text = "正在提现…"
            tv_result.setTextColor(R.color._3A3A3A.getResColor())
            tv_result.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        } else if (TextUtils.equals(item.status, "1")) {
            tv_result.text = "提现成功"
            tv_result.setTextColor(R.color._3BBB88.getResColor())
            tv_result.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_withdraw_ok, 0)
        }

    }

}