package com.alan.module.my.adapter

import com.alan.module.my.R
import com.alan.mvvm.base.ktx.getResColor
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import leifu.shapelibrary.ShapeView

class LabelShowAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_label) {


    override fun convert(holder: BaseViewHolder, item: String) {
        val tvTitle = holder.getView<ShapeView>(R.id.tv_title)
        tvTitle.setText(item)
        tvTitle.setTextColor(R.color._FFBF2F.getResColor())
        tvTitle.setShapeSolidColor(R.color._33FFBF2F.getResColor())
            .setShapeStrokeColor(R.color._33FFBF2F.getResColor()).setUseShape()
    }

}