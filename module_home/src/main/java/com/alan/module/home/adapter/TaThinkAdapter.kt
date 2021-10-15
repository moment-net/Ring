package com.alan.module.home.adapter

import com.alan.module.home.R
import com.alan.mvvm.base.http.responsebean.ThinkBean
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder


class TaThinkAdapter : BaseQuickAdapter<ThinkBean, BaseViewHolder>(R.layout.item_think_ta),
    LoadMoreModule {

    init {
        addChildClickViewIds(R.id.iv_zan)
    }

    override fun convert(holder: BaseViewHolder, item: ThinkBean) {
        holder.setText(R.id.tv_content, item.content)
        holder.setText(R.id.tv_time, item.createTime)

        if (item.isFavorite) {
            holder.setImageResource(R.id.iv_zan, R.drawable.icon_home_zan_on)
        } else {
            holder.setImageResource(R.id.iv_zan, R.drawable.icon_home_zan_off)
        }
        holder.setText(R.id.tv_num, "${item.favoriteCount}")
    }


}