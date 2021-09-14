package com.alan.module.chat.adapter.itemprovider

import android.graphics.drawable.AnimationDrawable
import android.media.MediaPlayer.OnCompletionListener
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.alan.module.chat.R
import com.alan.mvvm.base.BaseApplication
import com.alan.mvvm.base.coil.CoilUtils
import com.alan.mvvm.common.constant.IMConstant
import com.alan.mvvm.common.im.utils.EMVoiceLengthUtils
import com.alan.mvvm.common.im.utils.VoicePlayerUtil
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.hyphenate.chat.EMMessage
import com.hyphenate.chat.EMVoiceMessageBody
import com.hyphenate.util.DateUtils
import java.util.*

class ChatVoiceLeftProvider() : BaseItemProvider<EMMessage>() {
    lateinit var ivVoice: ImageView
    lateinit var voiceAnimation: AnimationDrawable
    var voicePlayerUtil: VoicePlayerUtil

    init {
        voicePlayerUtil = VoicePlayerUtil.getInstance(BaseApplication.mContext)
    }

    override val itemViewType: Int
        get() = IMConstant.MESSAGE_TYPE_VOICE_LEFT


    override val layoutId: Int
        get() = R.layout.item_receive_voice


    override fun convert(helper: BaseViewHolder, item: EMMessage) {
        val tv_time = helper.getView<TextView>(R.id.tv_time);
        val iv_avatar = helper.getView<ImageView>(R.id.iv_avatar);
        val iv_voice = helper.getView<ImageView>(R.id.iv_voice);
        val tv_length = helper.getView<TextView>(R.id.tv_length);
        val cl_voice = helper.getView<ConstraintLayout>(R.id.cl_voice);

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

        cl_voice.setOnClickListener {
            if (voicePlayerUtil.isPlaying) {
                //无论播放的语音项是这个还是其他，都先停止语音播放
                voicePlayerUtil.stop()
                // 停止语音播放动画。
                stopVoicePlayAnimation()

                // 如果正在播放的语音项是此项，则只需停止播放即可。
                val playingId: String = voicePlayerUtil.getCurrentPlayingId()
                if (item.msgId == playingId) {
                    return@setOnClickListener
                }
            }
            val voice = item.getBody() as EMVoiceMessageBody
            if (!TextUtils.isEmpty(voice.localUri.toString())) {
                ivVoice = iv_voice
                playVoice(item)
                // 启动语音播放动画
                startVoicePlayAnimation()
            }

        }
    }


    fun startVoicePlayAnimation() {
        ivVoice.setImageResource(R.drawable.anim_voice_black)
        voiceAnimation = ivVoice.getDrawable() as AnimationDrawable
        voiceAnimation.start()
    }

    fun stopVoicePlayAnimation() {
        if (voiceAnimation != null) {
            voiceAnimation.stop()
        }
        ivVoice.setImageResource(R.drawable.icon_voice_black4)
    }

    private fun playVoice(msg: EMMessage) {
        voicePlayerUtil.play(msg, OnCompletionListener {
            stopVoicePlayAnimation()
        })
    }


    override fun onViewDetachedFromWindow(holder: BaseViewHolder) {
        super.onViewDetachedFromWindow(holder)
        if (voicePlayerUtil.isPlaying()) {
            voicePlayerUtil.stop()
        }
    }
}