package com.alan.module.my.adapter

import android.graphics.Color
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import com.alan.module.my.R
import com.alan.mvvm.base.coil.CoilUtils
import com.alan.mvvm.base.http.responsebean.CardInfoBean
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import leifu.shapelibrary.ShapeView

class CardAdapter : BaseQuickAdapter<CardInfoBean, BaseViewHolder>(R.layout.item_card) {

    init {
        addChildClickViewIds(R.id.tv_label_bg)
    }

    override fun convert(holder: BaseViewHolder, item: CardInfoBean) {
        val itemPosition = getItemPosition(item)
        val tvLabelBg = holder.getView<ShapeView>(R.id.tv_label_bg)
        val ivLabel = holder.getView<ImageView>(R.id.iv_label)
        val tvType = holder.getView<TextView>(R.id.tv_type)
        val tvLevel = holder.getView<TextView>(R.id.tv_level)
        val group = holder.getView<Group>(R.id.group)
        val tvAdd = holder.getView<TextView>(R.id.tv_add)

        tvLabelBg.setShapeSolidColor(Color.parseColor(item.style?.bgColor)).setUseShape()
        tvLabelBg.alpha = item.style?.bgOpacity!!
        tvType.setTextColor(Color.parseColor(item.style?.textColor))
        tvLevel.setTextColor(Color.parseColor(item.style?.textColor))

        CoilUtils.load(ivLabel, item.style?.picUrl!!)
        tvType.setText(item.cardName)
        tvLevel.setText(item.desc)


    }

}