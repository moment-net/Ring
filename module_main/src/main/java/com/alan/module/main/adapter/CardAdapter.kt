package com.alan.module.main.adapter

import com.alan.module.main.R
import com.alan.mvvm.base.http.responsebean.NowTagBean
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder


class CardAdapter : BaseQuickAdapter<NowTagBean, BaseViewHolder>(R.layout.item_card) {

    init {
        addChildClickViewIds(R.id.tv_label_bg)
    }

    override fun convert(holder: BaseViewHolder, item: NowTagBean) {
//        val itemPosition = getItemPosition(item)
//        val tvLabelBg = holder.getView<ShapeView>(R.id.tv_label_bg)
//        val tvLabel = holder.getView<TextView>(R.id.tv_label)
//        val ivLabel = holder.getView<ImageView>(R.id.iv_label)
//
//        tvLabel.setText(item.tag)
//
//        tvLabelBg.setShapeSolidColor(Color.parseColor(item.bgColor)).setUseShape()
//        tvLabelBg.alpha = item.bgOpacity
//        tvLabel.setTextColor(Color.parseColor(item.textColor))
//        CoilUtils.load(ivLabel, item.highlightPicUrl)

    }


}