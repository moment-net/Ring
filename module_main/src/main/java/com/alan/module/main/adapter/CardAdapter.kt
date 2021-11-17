package com.alan.module.main.adapter

import android.graphics.Color
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import com.alan.module.main.R
import com.alan.mvvm.base.coil.CoilUtils
import com.alan.mvvm.base.http.responsebean.CardInfoBean
import com.alan.mvvm.base.ktx.gone
import com.alan.mvvm.base.ktx.visible
import com.alan.mvvm.common.helper.SpHelper
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import leifu.shapelibrary.ShapeView


class CardAdapter : BaseQuickAdapter<CardInfoBean, BaseViewHolder>(R.layout.item_card) {

    init {
        addChildClickViewIds(R.id.tv_label_bg)
        addChildClickViewIds(R.id.tv_add)
    }

    override fun convert(holder: BaseViewHolder, item: CardInfoBean) {
        val itemPosition = getItemPosition(item)
        val tvLabelBg = holder.getView<ShapeView>(R.id.tv_label_bg)
        val ivLabel = holder.getView<ImageView>(R.id.iv_label)
        val tvType = holder.getView<TextView>(R.id.tv_type)
        val tvLevel = holder.getView<TextView>(R.id.tv_level)
        val group = holder.getView<Group>(R.id.group)
        val tvAdd = holder.getView<TextView>(R.id.tv_add)
        val ivRed = holder.getView<ImageView>(R.id.iv_red)

        if (itemPosition == data.size - 1) {
            group.gone()
            tvAdd.visible()
            if (SpHelper.isClickCard()) {
                ivRed.gone()
            } else {
                ivRed.visible()
            }
        } else {
            group.visible()
            tvAdd.gone()
            ivRed.gone()

            tvLabelBg.setShapeSolidColor(Color.parseColor(item.style?.bgColor)).setUseShape()
            tvLabelBg.alpha = item.style?.bgOpacity!!
            tvType.setTextColor(Color.parseColor(item.style?.textColor))
            tvLevel.setTextColor(Color.parseColor(item.style?.textColor))

            CoilUtils.load(ivLabel, item.style?.picUrl!!)
            tvType.setText(item.cardName)
            tvLevel.setText(item.desc)
        }


    }


}