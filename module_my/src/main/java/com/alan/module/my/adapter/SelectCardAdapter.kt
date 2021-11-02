package com.alan.module.my.adapter

import android.graphics.Color
import android.widget.ImageView
import android.widget.TextView
import com.alan.module.my.R
import com.alan.mvvm.base.coil.CoilUtils
import com.alan.mvvm.base.http.responsebean.CardTagBean
import com.alan.mvvm.base.ktx.getResColor
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import leifu.shapelibrary.ShapeView


class SelectCardAdapter : BaseQuickAdapter<CardTagBean, BaseViewHolder>(R.layout.item_select_card) {
    var selectPosition: Int = -1

    init {
        addChildClickViewIds(R.id.tv_label_bg)
    }

    override fun convert(holder: BaseViewHolder, item: CardTagBean) {
        val itemPosition = getItemPosition(item)
        val tvLabelBg = holder.getView<ShapeView>(R.id.tv_label_bg)
        val tvLabel = holder.getView<TextView>(R.id.tv_label)
        val ivLabel = holder.getView<ImageView>(R.id.iv_label)

        tvLabel.setText(item.tag)

        if (selectPosition == itemPosition) {
            tvLabelBg.setShapeSolidColor(Color.parseColor(item.bgColor)).setUseShape()
            tvLabelBg.alpha = item.bgOpacity
            tvLabel.setTextColor(Color.parseColor(item.textColor))
            CoilUtils.load(ivLabel, item.highlightPicUrl)
        } else {
            tvLabelBg.setShapeSolidColor(R.color._F4F4F4.getResColor()).setUseShape()
            tvLabelBg.alpha = 1.0f
            tvLabel.setTextColor(R.color._9C9C9C.getResColor())
            CoilUtils.load(ivLabel, item.picUrl)
        }

    }


}