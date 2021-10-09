package com.alan.module.my.adapter

import com.alan.module.my.R
import com.alan.mvvm.base.http.responsebean.SystemMessageBean
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class SystemMsgAdapter : BaseQuickAdapter<SystemMessageBean, BaseViewHolder>(R.layout.item_system) {

    override fun convert(holder: BaseViewHolder, item: SystemMessageBean) {
        holder.setText(R.id.tv_title, item.title)
        holder.setText(R.id.tv_time, item.createTime)
        if (item.content == null) {
            holder.setGone(R.id.tv_content, true)
        } else {
            holder.setGone(R.id.tv_content, false)
            holder.setText(R.id.tv_content, item.content?.msg)
        }

    }

}