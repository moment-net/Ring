package com.alan.module.my.adapter

import com.alan.module.my.R
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class WithDrawAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_skil) {

    override fun convert(holder: BaseViewHolder, item: String) {
//        helper.setText(R.id.tv_name, "提现-微信：" + item.getWxName())
//        helper.setText(R.id.tv_time, item.getOperateTime())
//        helper.setText(R.id.tv_money, "-" + item.getCashCount().toString() + "元")
//        val tv_result: TextView = helper.getView<TextView>(R.id.tv_result)
//        if (TextUtils.equals(item.getStatus(), "-1")) {
//            tv_result.text = "提现失败"
//            tv_result.setTextColor(ContextCompat.getColor(context, R.color._FF6767))
//            tv_result.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_withdraw_fail, 0)
//        } else if (TextUtils.equals(item.getStatus(), "0")) {
//            tv_result.text = "正在提现…"
//            tv_result.setTextColor(ContextCompat.getColor(context, R.color._3A3A3A))
//            tv_result.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
//        } else if (TextUtils.equals(item.getStatus(), "1")) {
//            tv_result.text = "提现成功"
//            tv_result.setTextColor(ContextCompat.getColor(context, R.color._3BBB88))
//            tv_result.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_withdraw_ok, 0)
//        }

    }

}