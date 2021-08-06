package com.alan.module.my.adapter

import com.alan.module.my.R
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class SystemMsgAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_system) {

    override fun convert(holder: BaseViewHolder, item: String) {
//        holder.setText(R.id.tv_title, item.getTitle())
//        holder.setText(R.id.tv_time, item.getCreateTime())
//        if (item.getSource() === 8) {
//            holder.setGone(R.id.tv_content, true)
//        } else {
//            holder.setGone(R.id.tv_content, false)
//            holder.setText(R.id.tv_content, item.getContent())
//        }

    }

}