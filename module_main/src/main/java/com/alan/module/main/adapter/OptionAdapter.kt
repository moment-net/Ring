package com.alan.module.main.adapter

import android.widget.ImageView
import com.alan.module.main.R
import com.alan.mvvm.base.coil.CoilUtils
import com.alan.mvvm.base.http.responsebean.AppearanceBean
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder


class OptionAdapter(var gender: Int) :
    BaseQuickAdapter<AppearanceBean, BaseViewHolder>(R.layout.item_option) {

    var currentCheck: Int = 0

    override fun convert(holder: BaseViewHolder, item: AppearanceBean) {
        val iv_bg = holder.getView<ImageView>(R.id.iv_bg)
        val iv_option = holder.getView<ImageView>(R.id.iv_option)
        val itemPosition = getItemPosition(item)
        if (itemPosition == currentCheck) {
            if (gender == 1) {
                iv_bg.setImageResource(R.drawable.icon_plan_boy)
            } else {
                iv_bg.setImageResource(R.drawable.icon_plan_girl)
            }
        } else {
            iv_bg.setImageResource(R.drawable.icon_plan_off)
        }
        CoilUtils.load(iv_option, item.buttton)

    }


}