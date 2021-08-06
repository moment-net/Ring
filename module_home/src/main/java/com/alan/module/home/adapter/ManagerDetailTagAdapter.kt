package com.alan.module.home.adapter

import com.alan.module.home.R
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import leifu.shapelibrary.ShapeView

class ManagerDetailTagAdapter :
    BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_manager_tag) {

    override fun convert(holder: BaseViewHolder, item: String) {
        val tvTag: ShapeView = holder.getView(R.id.tv_tag)

    }

}