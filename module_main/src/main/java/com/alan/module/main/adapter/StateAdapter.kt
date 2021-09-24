package com.alan.module.main.adapter

import com.alan.module.main.R
import com.alan.mvvm.base.ktx.getResColor
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import leifu.shapelibrary.ShapeView

class StateAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_state) {

    var selectPositon: Int = -1

    init {
        addChildClickViewIds(R.id.ctv_label)
    }

    override fun convert(holder: BaseViewHolder, item: String) {
        val tv_label = holder.getView<ShapeView>(R.id.ctv_label)
        tv_label.setText(item)
        val position = getItemPosition(item)
        if (position == selectPositon) {
            tv_label.setTextColor(R.color.white.getResColor())
            tv_label.setShapeSolidColor(R.color._FFBF2F.getResColor()).setUseShape()
        } else {
            tv_label.setTextColor(R.color._FFBD00.getResColor())
            tv_label.setShapeSolidColor(R.color.white.getResColor()).setUseShape()
        }
    }

}