package com.alan.module.chat.adapter.itemprovider

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.alan.module.chat.R
import com.alan.mvvm.base.coil.CoilUtils
import com.alan.mvvm.common.constant.IMConstant
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.hyphenate.chat.EMMessage
import com.hyphenate.util.DateUtils
import java.util.*

class ChatVoiceCallLeftProvider() : BaseItemProvider<EMMessage>() {
    override val itemViewType: Int
        get() = IMConstant.MESSAGE_TYPE_VOICECALL_LEFT


    override val layoutId: Int
        get() = R.layout.item_receive_voicecall


    override fun convert(helper: BaseViewHolder, item: EMMessage) {
        val tv_time = helper.getView<TextView>(R.id.tv_time);
        val iv_avatar = helper.getView<ImageView>(R.id.iv_avatar);
        val tv_msg = helper.getView<TextView>(R.id.tv_msg);

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


    }
}