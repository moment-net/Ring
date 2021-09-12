package com.alan.module.chat.adapter.itemprovider

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.alan.module.chat.R
import com.alan.module.im.constants.IMConstant
import com.alan.mvvm.base.coil.CoilUtils
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.hyphenate.chat.EMImageMessageBody
import com.hyphenate.chat.EMMessage
import com.hyphenate.util.DateUtils
import java.util.*

class ChatImageRightProvider() : BaseItemProvider<EMMessage>() {
    override val itemViewType: Int
        get() = IMConstant.MESSAGE_TYPE_IMAGE_RIGHT


    override val layoutId: Int
        get() = R.layout.item_send_image


    override fun convert(helper: BaseViewHolder, item: EMMessage) {
        val tv_time = helper.getView<TextView>(R.id.tv_time);
        val iv_avatar = helper.getView<ImageView>(R.id.iv_avatar);
        val iv_pic = helper.getView<ImageView>(R.id.iv_pic);
        val iv_ack = helper.getView<ImageView>(R.id.iv_ack);

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
        val avatar = item.getStringAttribute(IMConstant.MESSAGE_ATTR_AVATAR)
        CoilUtils.loadCircle(iv_avatar, avatar)

        val txtBody = item.getBody() as EMImageMessageBody
        CoilUtils.loadRound(iv_pic, txtBody.localUri.toString(), 7f)

        if (item.isAcked()) {
            iv_ack.setVisibility(View.VISIBLE)
        } else {
            iv_ack.setVisibility(View.INVISIBLE)
        }
    }
}