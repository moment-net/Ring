package com.alan.module.main.adapter

import com.alan.module.main.R
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import leifu.shapelibrary.ShapeView

class ManagerTagAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_tag) {

    override fun convert(holder: BaseViewHolder, item: String) {
        val tvTag: ShapeView = holder.getView(R.id.tv_tag)

    }

}