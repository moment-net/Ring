package com.alan.module.my.adapter

import android.text.TextUtils
import androidx.core.content.ContextCompat
import com.alan.module.my.R
import com.alan.mvvm.base.coil.CoilUtils
import com.alan.mvvm.base.http.responsebean.MessageBean
import com.alan.mvvm.base.ktx.getResColor
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import leifu.shapelibrary.ShapeView

class MessageAdapter() : BaseMultiItemQuickAdapter<MessageBean, BaseViewHolder>() {

    init {
        addItemType(MessageBean.FOCUS_MSG, R.layout.item_msg_focus)
        addItemType(MessageBean.GIFT_MSG, R.layout.item_msg_chat)
        addItemType(MessageBean.MEAL_MSG, R.layout.item_msg_chat)
        addChildClickViewIds(R.id.iv_avatar)
        addChildClickViewIds(R.id.tv_follow)
        addChildClickViewIds(R.id.tv_chat)
    }


    override fun convert(holder: BaseViewHolder, item: MessageBean) {
        when (holder.itemViewType) {
            MessageBean.FOCUS_MSG -> {
                var followStatus = 0
                if (item.content?.user != null) {
                    val avatar: String = item.content?.user?.avatar!!
                    if (!TextUtils.isEmpty(avatar)) {
                        CoilUtils.loadCircle(holder.getView(R.id.iv_avatar), avatar)
                    }
                    holder.setText(R.id.tv_name, item.content?.user?.userName)
                    followStatus = item.content?.user?.followStatus!!
                }
                holder.setText(R.id.tv_content, item.title)
                holder.setText(R.id.tv_time, item.createTime)
                if (followStatus == 0) {
                    holder.setText(R.id.tv_follow, "关注")
                    holder.setTextColor(
                        R.id.tv_follow,
                        ContextCompat.getColor(context, R.color.white)
                    )
                    holder.getView<ShapeView>(R.id.tv_follow)
                        .setShapeSolidColor(R.color._FF4F78.getResColor()).setUseShape()
                } else if (followStatus == 1) {
                    holder.setText(R.id.tv_follow, "已关注")
                    holder.setTextColor(
                        R.id.tv_follow,
                        ContextCompat.getColor(context, R.color.white)
                    )
                    holder.getView<ShapeView>(R.id.tv_follow)
                        .setShapeSolidColor(R.color._988778.getResColor()).setUseShape()
                } else if (followStatus == 2) {
                    holder.setText(R.id.tv_follow, "互相关注")
                    holder.setTextColor(
                        R.id.tv_follow,
                        ContextCompat.getColor(context, R.color.white)
                    )
                    holder.getView<ShapeView>(R.id.tv_follow)
                        .setShapeSolidColor(R.color._988778.getResColor()).setUseShape()
                }
            }
            MessageBean.GIFT_MSG -> {
                if (item.content?.user != null) {
                    val avatar: String = item.content?.user?.avatar!!
                    if (!TextUtils.isEmpty(avatar)) {
                        CoilUtils.loadCircle(holder.getView(R.id.iv_avatar), avatar)
                    }
                    holder.setText(R.id.tv_name, item.content?.user?.userName)
                }
                holder.setText(R.id.tv_content, item.content?.msg)
            }
            MessageBean.MEAL_MSG -> {
                if (item.content?.user != null) {
                    val avatar: String = item.content?.user?.avatar!!
                    if (!TextUtils.isEmpty(avatar)) {
                        CoilUtils.loadCircle(holder.getView(R.id.iv_avatar), avatar)
                    }
                    holder.setText(R.id.tv_name, item.content?.user?.userName)
                }
                holder.setText(R.id.tv_content, item.content?.msg)
            }
        }
    }

}