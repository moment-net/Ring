package com.alan.module.main.adapter

import com.alan.module.main.R
import com.alan.mvvm.base.ktx.getResColor
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import leifu.shapelibrary.ShapeView


class StarAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_star) {

    init {
        addChildClickViewIds(R.id.tv_content)
    }

    override fun convert(holder: BaseViewHolder, item: String) {
        val tv_content: ShapeView = holder.getView<ShapeView>(R.id.tv_content)

        if (true) {
            tv_content.setTextColor(R.color.white.getResColor())
            tv_content.setShapeSolidColor(R.color._6F9BFD.getResColor())
                .setShapeStrokeColor(R.color._6F9BFD.getResColor()).setUseShape()
        } else {
            tv_content.setTextColor(R.color._803A3A3A.getResColor())
            tv_content.setShapeSolidColor(R.color.white.getResColor())
                .setShapeStrokeColor(R.color._803A3A3A.getResColor()).setUseShape()
        }
        tv_content.setText(item.toString())


    }


}