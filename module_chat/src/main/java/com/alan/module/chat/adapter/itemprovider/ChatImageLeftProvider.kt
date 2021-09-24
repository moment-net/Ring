package com.alan.module.chat.adapter.itemprovider

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.alan.module.chat.R
import com.alan.mvvm.base.coil.CoilUtils
import com.alan.mvvm.common.constant.IMConstant
import com.alan.mvvm.common.db.entity.UserEntity
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.hyphenate.chat.EMImageMessageBody
import com.hyphenate.chat.EMMessage
import com.hyphenate.util.DateUtils
import java.util.*

class ChatImageLeftProvider(val userEntity: UserEntity) : BaseItemProvider<EMMessage>() {
    override val itemViewType: Int
        get() = IMConstant.MESSAGE_TYPE_IMAGE_LEFT


    override val layoutId: Int
        get() = R.layout.item_receive_image


    override fun convert(helper: BaseViewHolder, item: EMMessage) {
        val tv_time = helper.getView<TextView>(R.id.tv_time);
        val iv_avatar = helper.getView<ImageView>(R.id.iv_avatar);
        val iv_pic = helper.getView<ImageView>(R.id.iv_pic);

        val position = getAdapter()?.getItemPosition(item);
        if (position == 0) {
            tv_time.setText(DateUtils.getTimestampString(Date(item.getMsgTime())))
            tv_time.setVisibility(View.VISIBLE)
        } else {
            val prevMessage = getAdapter()?.data?.get(position!! - 1)
            if (prevMessage != null && DateUtils.isCloseEnough(
                    item.getMsgTime(),
                    prevMessage.msgTime
                )
            ) {
                tv_time.setVisibility(View.GONE)
            } else {
                tv_time.setText(DateUtils.getTimestampString(Date(item.getMsgTime())))
                tv_time.setVisibility(View.VISIBLE)
            }
        }
        CoilUtils.loadCircle(iv_avatar, userEntity.avatar)

        val txtBody = item.getBody() as EMImageMessageBody
        CoilUtils.loadRound(iv_pic, txtBody.remoteUrl.toString(), 7f)
    }
}