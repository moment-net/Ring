package com.alan.module.main.adapter

import android.text.TextUtils
import com.alan.module.main.R
import com.alan.mvvm.base.coil.CoilUtils
import com.alan.mvvm.base.http.responsebean.ThinkBean
import com.alan.mvvm.base.ktx.getResColor
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import leifu.shapelibrary.ShapeView


class ThinkAdapter : BaseQuickAdapter<ThinkBean, BaseViewHolder>(R.layout.item_think) {

    init {
        addChildClickViewIds(R.id.iv_avatar)
        addChildClickViewIds(R.id.iv_more)
        addChildClickViewIds(R.id.iv_zan)
    }

    override fun convert(holder: BaseViewHolder, item: ThinkBean) {
        CoilUtils.loadCircle(holder.getView(R.id.iv_avatar), item.user.avatar)
        holder.setText(R.id.tv_name, item.user.userName)
        val tvAge = holder.getView<ShapeView>(R.id.tv_age)

        val age = if (item.user.age > 0) {
            "${item.user.age}岁"
        } else {
            ""
        }
        if (TextUtils.isEmpty(age)) {
            tvAge.setText("")
        } else {
            tvAge.setText("$age")
        }
        if (item.user.gender == 1) {
            tvAge.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_home_boy_white, 0, 0, 0)
            tvAge.setTextColor(R.color.white.getResColor())
            tvAge.setShapeSolidColor(R.color._515FFF.getResColor()).setUseShape()
        } else {
            tvAge.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_home_girl_white, 0, 0, 0)
            tvAge.setTextColor(R.color.white.getResColor())
            tvAge.setShapeSolidColor(R.color._FF517A.getResColor()).setUseShape()
        }

        if (item.user.onlineStatus!!) {
            holder.setGone(R.id.tv_online, false)
        } else {
            holder.setGone(R.id.tv_online, true)
        }

        holder.setText(R.id.tv_content, item.content)

        if (item.isFavorite) {
            holder.setImageResource(R.id.iv_zan, R.drawable.icon_home_zan_on)
        } else {
            holder.setImageResource(R.id.iv_zan, R.drawable.icon_home_zan_off)
        }
        holder.setText(R.id.tv_num, "${item.favoriteCount}")
    }


}