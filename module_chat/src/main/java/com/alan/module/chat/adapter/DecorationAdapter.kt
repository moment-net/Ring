package com.alan.module.chat.adapter

import com.alan.module.chat.R
import com.alan.mvvm.base.http.responsebean.ChatBgBean
import com.alan.mvvm.base.ktx.getResColor
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import leifu.shapelibrary.ShapeView

class DecorationAdapter : BaseQuickAdapter<ChatBgBean, BaseViewHolder>(R.layout.item_decoration) {

    init {
        addChildClickViewIds(R.id.tv_content)
    }

    override fun convert(holder: BaseViewHolder, item: ChatBgBean) {
        val tv_content: ShapeView = holder.getView<ShapeView>(R.id.tv_content)

        if (item.selected) {
            tv_content.setTextColor(R.color.white.getResColor())
            tv_content.setShapeSolidColor(R.color._6F9BFD.getResColor())
                .setShapeStrokeColor(R.color._6F9BFD.getResColor()).setUseShape()
        } else {
            tv_content.setTextColor(R.color._803A3A3A.getResColor())
            tv_content.setShapeSolidColor(R.color.white.getResColor())
                .setShapeStrokeColor(R.color._803A3A3A.getResColor()).setUseShape()
        }
        tv_content.setText(item.desc)

    }
}