package com.alan.module.my.adapter

import com.alan.module.my.R
import com.alan.mvvm.base.http.responsebean.ConsumeBean
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class RecordAdapter : BaseQuickAdapter<ConsumeBean, BaseViewHolder>(R.layout.item_record) {

    override fun convert(holder: BaseViewHolder, item: ConsumeBean) {
        holder.setText(R.id.tv_name, item.msg)
        holder.setText(R.id.tv_time, item.createTime)
        if (item.points > 0) {
            holder.setText(R.id.tv_num, "+" + item.points)
        } else {
            holder.setText(R.id.tv_num, "" + item.points)
        }

    }

}