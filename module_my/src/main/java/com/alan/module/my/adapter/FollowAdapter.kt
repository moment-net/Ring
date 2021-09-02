package com.alan.module.my.adapter

import android.text.TextUtils
import android.widget.ImageView
import com.alan.module.my.R
import com.alan.mvvm.base.coil.CoilUtils
import com.alan.mvvm.base.http.responsebean.UserInfoBean
import com.alan.mvvm.base.ktx.getResColor
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import leifu.shapelibrary.ShapeView

class FollowAdapter : BaseQuickAdapter<UserInfoBean, BaseViewHolder>(R.layout.item_follow) {

    init {
        addChildClickViewIds(R.id.tv_follow)
    }

    override fun convert(holder: BaseViewHolder, item: UserInfoBean) {
        holder.setText(R.id.tv_name, item.userName)
        val iv_avator: ImageView = holder.getView<ImageView>(R.id.iv_avator)
        val tv_follow = holder.getView<ShapeView>(R.id.tv_follow)
        if (!TextUtils.isEmpty(item.avatar)) {
            CoilUtils.loadCircle(iv_avator, item.avatar)
        }
        // 0是关注我 1是我关注 2是互相关注
        val status: Int = item.followStatus!!
        if (status == 0) {
            tv_follow.setText("关注")
            tv_follow.setTextColor(R.color.white.getResColor())
            tv_follow.apply {
                setShapeSolidColor(R.color._FFC843.getResColor())
                setUseShape()
            }
        } else if (status == 1) {
            tv_follow.setText("已关注")
            tv_follow.setTextColor(R.color._52565E.getResColor())
            tv_follow.apply {
                setShapeSolidColor(R.color._1A4A4A4A.getResColor())
                setUseShape()
            }
        } else if (status == 2) {
            tv_follow.setText("互相关注")
            tv_follow.setTextColor(R.color._52565E.getResColor())
            tv_follow.apply {
                setShapeSolidColor(R.color._1A4A4A4A.getResColor())
                setUseShape()
            }
        }

    }

}