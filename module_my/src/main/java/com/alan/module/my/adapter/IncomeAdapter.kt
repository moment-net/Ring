package com.alan.module.my.adapter

import com.alan.module.my.R
import com.alan.mvvm.base.http.responsebean.ListBean
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class IncomeAdapter : BaseQuickAdapter<ListBean, BaseViewHolder>(R.layout.item_income) {

    override fun convert(holder: BaseViewHolder, item: ListBean) {
        holder.setText(R.id.tv_content, item.detail)
        holder.setText(R.id.tv_time, item.receiveTime)
        holder.setText(R.id.tv_amount, "+${item.totalPoints}个钻石")

    }

}