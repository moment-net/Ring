package com.alan.module.main.adapter

import com.alan.module.main.R
import com.alan.mvvm.base.http.responsebean.ThinkBean
import com.alan.mvvm.base.ktx.getResColor
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder


class MyThinkAdapter : BaseQuickAdapter<ThinkBean, BaseViewHolder>(R.layout.item_think_my),
    LoadMoreModule {

    init {
        addChildClickViewIds(R.id.iv_zan)
    }

    override fun convert(holder: BaseViewHolder, item: ThinkBean) {
        holder.setText(R.id.tv_content, item.content)
        holder.setText(R.id.tv_time, item.createTime)

        if (item.isFavorite) {
            holder.setTextColor(R.id.tv_num, R.color._FF6464.getResColor())
            holder.setImageResource(R.id.iv_zan, R.drawable.icon_home_zan_on)
        } else {
            holder.setTextColor(R.id.tv_num, R.color._BAB9B9.getResColor())
            holder.setImageResource(R.id.iv_zan, R.drawable.icon_home_zan_off)
        }
        holder.setText(R.id.tv_num, "${item.favoriteCount}")
    }

}