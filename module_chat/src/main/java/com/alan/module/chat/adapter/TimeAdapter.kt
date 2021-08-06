package com.alan.module.chat.adapter

import androidx.constraintlayout.widget.ConstraintLayout
import com.alan.module.chat.R
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class TimeAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_time) {
    val currentPosition = 0

    override fun convert(holder: BaseViewHolder, item: String) {
        val cl_item: ConstraintLayout = holder.getView<ConstraintLayout>(R.id.cl_item)
//        holder.setText(R.id.tv_time, item.getNum().toString() + "天")
//        holder.setText(R.id.tv_diamond, item.getPoint().toString() + "")

        val itemPosition = getItemPosition(item)
        if (itemPosition == currentPosition) {
            cl_item.setBackgroundResource(R.drawable.shape_time_on10)
        } else {
            cl_item.setBackgroundResource(R.drawable.shape_time_off10)
        }

    }
}