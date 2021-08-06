package com.alan.module.my.adapter

import com.alan.module.my.R
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class IncomeAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_income) {

    override fun convert(holder: BaseViewHolder, item: String) {
//        helper.setText(R.id.tv_content, item.getDetail())
//        helper.setText(R.id.tv_time, item.getReceiveTime())
//        helper.setText(R.id.tv_amount, "+" + item.getTotalPoints().toString() + "个钻石")

    }

}