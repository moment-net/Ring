package com.alan.module.main.adapter

import android.text.SpannableString
import android.text.Spanned
import android.text.style.ImageSpan
import com.alan.module.main.R
import com.alan.mvvm.base.coil.CoilUtils
import com.alan.mvvm.base.http.responsebean.CookerBean
import com.alan.mvvm.base.ktx.getResColor
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import leifu.shapelibrary.ShapeView


class ManagerAdapter : BaseQuickAdapter<CookerBean, BaseViewHolder>(R.layout.item_manager) {

    override fun convert(holder: BaseViewHolder, item: CookerBean) {
        CoilUtils.loadRound(holder.getView(R.id.iv_avatar), item.user.avatar, 3f)
        holder.setText(R.id.tv_name, item.user.userName)
        var tvAge = holder.getView<ShapeView>(R.id.tv_age)
        var address = item.user.address.split("-")[2]
        tvAge.setText("${item.user.age}岁  ${address}")
        if (item.user.gender == 1) {
            tvAge.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_home_boy, 0, 0, 0)
            tvAge.setTextColor(R.color._515FFF.getResColor())
            tvAge.setShapeSolidColor(R.color._1A515FFF.getResColor())
        } else {
            tvAge.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_home_girl, 0, 0, 0)
            tvAge.setTextColor(R.color._FF517A.getResColor())
            tvAge.setShapeSolidColor(R.color._1AFF517A.getResColor())
        }

        holder.setText(R.id.tv_state, item.title)
        if (item.user.onlineStatus) {
            holder.setGone(R.id.iv_online, false)
        } else {
            holder.setGone(R.id.iv_online, true)
        }

        var duration: Int = (item.user.greeting?.duration ?: 0) / 1000
        holder.setText(R.id.tv_voice, "${duration}s")


        var tagList = item.tag
        if (tagList != null && !tagList.isEmpty()) {
            holder.setGone(R.id.tv_tag, false)
            var stringBuilder = StringBuilder()
            var list = arrayListOf<Int>()
            for (position in 0..tagList.size - 1) {
                if (position < tagList.size - 1) {
                    stringBuilder.append("${tagList.get(position)} ")
                    list.add(stringBuilder.length - 1)
                } else {
                    stringBuilder.append("${tagList.get(position)}")
                }
            }
            var spannableString = SpannableString(stringBuilder.toString());
            for (index in list) {
                val imageSpan = ImageSpan(context, R.drawable.icon_home_line)
                spannableString.setSpan(
                    imageSpan,
                    index,
                    index + 1,
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE
                );
            }
            holder.setText(R.id.tv_tag, spannableString)
        } else {
            holder.setGone(R.id.tv_tag, true)
        }

        var likeList = item.likes
        if (likeList != null && !likeList.isEmpty()) {
            holder.setGone(R.id.tv_like, false)
            var stringBuilder = StringBuilder()
            var list = arrayListOf<Int>()
            for (position in 0..likeList.size - 1) {
                if (position < likeList.size - 1) {
                    stringBuilder.append("${likeList.get(position)} ")
                    list.add(stringBuilder.length - 1)
                } else {
                    stringBuilder.append("${likeList.get(position)}")
                }
            }
            var spannableString = SpannableString(stringBuilder.toString());

            for (index in list) {
                val imageSpan = ImageSpan(context, R.drawable.icon_home_line)
                spannableString.setSpan(
                    imageSpan,
                    index,
                    index + 1,
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE
                );
            }
            holder.setText(R.id.tv_like, spannableString)
        } else {
            holder.setGone(R.id.tv_like, true)
        }

    }

}