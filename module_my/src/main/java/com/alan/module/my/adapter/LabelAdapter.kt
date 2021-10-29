package com.alan.module.my.adapter

import com.alan.module.my.R
import com.alan.mvvm.base.ktx.getResColor
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import leifu.shapelibrary.ShapeView

class LabelAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_label) {

    lateinit var chooseList: ArrayList<String>


    init {
        addChildClickViewIds(R.id.tv_title)
    }

    override fun convert(holder: BaseViewHolder, item: String) {
        val tvTitle = holder.getView<ShapeView>(R.id.tv_title)
        tvTitle.setText(item)
        if (chooseList.contains(item)) {
            tvTitle.setTextColor(R.color._FFBF2F.getResColor())
            tvTitle.setShapeSolidColor(R.color._33FFBF2F.getResColor())
                .setShapeStrokeColor(R.color._33FFBF2F.getResColor()).setUseShape()
        } else {
            tvTitle.setTextColor(R.color._3A3A3A.getResColor())
            tvTitle.setShapeSolidColor(R.color.white.getResColor())
                .setShapeStrokeColor(R.color._EAEAEA.getResColor()).setUseShape()
        }

    }

}