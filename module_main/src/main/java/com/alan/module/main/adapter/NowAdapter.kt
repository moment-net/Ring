package com.alan.module.main.adapter

import com.alan.module.main.R
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder


class NowAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_now) {

    init {
        addChildClickViewIds(R.id.iv_avatar)
        addChildClickViewIds(R.id.iv_more)
        addChildClickViewIds(R.id.tv_chat)
    }

    override fun convert(holder: BaseViewHolder, item: String) {

    }


}