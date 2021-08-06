package com.alan.module.my.adapter

import com.alan.module.my.R
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class FollowAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_follow) {

    init {
        addChildClickViewIds(R.id.tv_follow)
    }

    override fun convert(holder: BaseViewHolder, item: String) {
//        holder.setText(R.id.tv_name, item.getUserName())
//        holder.setText(R.id.tv_desc, item.getDesc())
//        val iv_avator: ImageView = holder.getView<ImageView>(R.id.iv_avator)
//        val tv_follow = holder.getView<ShapeView>(R.id.tv_follow)
//        if (!TextUtils.isEmpty(item.getAvatar())) {
//            GlideUtils.loadRound(context, item.getAvatar(), iv_avator)
//        }
//        // 0是关注我 1是我关注 2是互相关注
//        val status: Int = item.getFollowStatus()
//        if (status == 0) {
//            tv_follow.text = "关注"
//            tv_follow.setTextColor(ContextCompat.getColor(context, R.color.white))
//        tv_follow.apply {
//            setShapeSolidColor(ContextCompat.getColor(context, R.color._FFC843))
//            setUseShape()
//        }
//        } else if (status == 1) {
//            tv_follow.text = "已关注"
//            tv_follow.setTextColor(ContextCompat.getColor(context, R.color._52565E))
//        tv_follow.apply {
//            setShapeSolidColor(ContextCompat.getColor(context, R.color._1A4A4A4A))
//            setUseShape()
//        }
//        } else if (status == 2) {
//            tv_follow.text = "互相关注"
//        tv_follow.setTextColor(ContextCompat.getColor(context, R.color._52565E))
//        tv_follow.apply {
//            setShapeSolidColor(ContextCompat.getColor(context, R.color._1A4A4A4A))
//            setUseShape()
//        }
//        }

    }

}