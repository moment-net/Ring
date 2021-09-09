package com.alan.module.chat.adapter.itemprovider

import android.graphics.drawable.AnimationDrawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.alan.module.chat.R
import com.alan.module.im.constants.IMConstant
import com.alan.module.im.utils.EMVoiceLengthUtils
import com.alan.mvvm.base.coil.CoilUtils
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.hyphenate.chat.EMMessage
import com.hyphenate.chat.EMVoiceMessageBody
import com.hyphenate.util.DateUtils
import java.util.*

class ChatVoiceLeftProvider(var avatar: String) : BaseItemProvider<EMMessage>() {
    lateinit var iv_voice: ImageView
    lateinit var voiceAnimation: AnimationDrawable

    override val itemViewType: Int
        get() = IMConstant.MESSAGE_TYPE_VOICE_LEFT


    override val layoutId: Int
        get() = R.layout.item_receive_voice


    override fun convert(helper: BaseViewHolder, item: EMMessage) {
        val tv_time = helper.getView<TextView>(R.id.tv_time);
        val iv_avatar = helper.getView<ImageView>(R.id.iv_avatar);
        iv_voice = helper.getView<ImageView>(R.id.iv_voice);
        val tv_length = helper.getView<TextView>(R.id.tv_length);

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

        CoilUtils.loadCircle(iv_avatar, avatar)

        val voiceBody = item.getBody() as EMVoiceMessageBody
        val len = voiceBody.length
        var padding = 0
        if (len > 0) {
            padding = EMVoiceLengthUtils.getVoiceLength(context, len)
            tv_length.text = "${voiceBody.length}s"
            tv_length.setVisibility(View.VISIBLE)
        } else {
            tv_length.setVisibility(View.INVISIBLE)
        }
        tv_length.setPadding(0, 0, padding, 0)
        iv_voice.setImageResource(R.drawable.icon_voice_black4)
    }


    fun startVoicePlayAnimation() {
        iv_voice.setImageResource(R.drawable.icon_voice_black4)
        voiceAnimation = iv_voice.getDrawable() as AnimationDrawable
        voiceAnimation.start()
    }

    fun stopVoicePlayAnimation() {
        if (voiceAnimation != null) {
            voiceAnimation.stop()
        }
        iv_voice.setImageResource(R.drawable.icon_voice_black4)
    }
}