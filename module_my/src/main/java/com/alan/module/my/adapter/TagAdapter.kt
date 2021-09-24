package com.alan.module.my.adapter

import com.alan.module.my.R
import com.alan.mvvm.base.ktx.getResColor
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import leifu.shapelibrary.ShapeView

class TagAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_skil) {

    val selectList = arrayListOf<String>()

    override fun convert(holder: BaseViewHolder, item: String) {
        val tv_label = holder.getView<ShapeView>(R.id.ctv_label)
        tv_label.setText(item)
        if (selectList.contains(item)) {
            tv_label.setTextColor(R.color._CA8B00.getResColor())
            tv_label.setShapeSolidColor(R.color._4DFFBD2A.getResColor()).setUseShape()
        } else {
            tv_label.setTextColor(R.color._3A3A3A.getResColor())
            tv_label.setShapeSolidColor(R.color._0D3A3A3A.getResColor()).setUseShape()
        }

    }

}