package com.alan.module.main.adapter

import android.graphics.Color
import android.text.TextUtils
import android.widget.ImageView
import android.widget.TextView
import com.alan.module.main.R
import com.alan.mvvm.base.coil.CoilUtils
import com.alan.mvvm.base.http.responsebean.NowBean
import com.alan.mvvm.base.ktx.dp2px
import com.alan.mvvm.base.ktx.getResColor
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import leifu.shapelibrary.ShapeView


class NowAdapter : BaseQuickAdapter<NowBean, BaseViewHolder>(R.layout.item_now), LoadMoreModule {

    init {
        addChildClickViewIds(R.id.iv_avatar)
        addChildClickViewIds(R.id.iv_more)
        addChildClickViewIds(R.id.tv_chat)
    }

    override fun convert(holder: BaseViewHolder, item: NowBean) {
        val ivAvatar = holder.getView<ImageView>(R.id.iv_avatar)
        CoilUtils.loadCircle(ivAvatar, item.user.avatar)
        holder.setText(R.id.tv_name, item.user.userName)
        val tvAge = holder.getView<ShapeView>(R.id.tv_age)
        val tvLabelBg = holder.getView<ShapeView>(R.id.tv_label_bg)
        val ivLabel = holder.getView<ImageView>(R.id.iv_label)
        val tvLabel = holder.getView<TextView>(R.id.tv_label)

        val age = if (item.user.age > 0) {
            "${item.user.age}岁"
        } else {
            ""
        }
        if (TextUtils.isEmpty(age)) {
            tvAge.setText("")
            tvAge.compoundDrawablePadding = 0
        } else {
            tvAge.setText("$age")
            tvAge.compoundDrawablePadding = context.dp2px(2f)
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

        holder.setText(R.id.tv_time, item.createTimeDesc)
        holder.setText(R.id.tv_content, item.content)

        //设置标签
        tvLabelBg.setShapeSolidColor(Color.parseColor(item.bgColor)).setUseShape()
        tvLabelBg.alpha = item.bgOpacity
        tvLabel.setText(item.tag)
        tvLabel.setTextColor(Color.parseColor(item.textColor))
        CoilUtils.load(ivLabel, item.tagPicUrl)
    }


}