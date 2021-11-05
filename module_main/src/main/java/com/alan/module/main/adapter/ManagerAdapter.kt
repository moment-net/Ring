package com.alan.module.main.adapter

import android.media.MediaPlayer
import android.os.CountDownTimer
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.ImageSpan
import android.widget.TextView
import com.alan.module.main.R
import com.alan.mvvm.base.BaseApplication
import com.alan.mvvm.base.coil.CoilUtils
import com.alan.mvvm.base.http.responsebean.CookerBean
import com.alan.mvvm.base.ktx.getResColor
import com.alan.mvvm.base.ktx.gone
import com.alan.mvvm.base.ktx.visible
import com.alan.mvvm.common.im.utils.VoicePlayerUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import leifu.shapelibrary.ShapeView


class ManagerAdapter : BaseQuickAdapter<CookerBean, BaseViewHolder>(R.layout.item_manager) {
    var voicePlayerUtil: VoicePlayerUtil
    lateinit var tvVoice: TextView
    var countDownTimer: CountDownTimer? = null

    init {
        voicePlayerUtil = VoicePlayerUtil.getInstance(BaseApplication.mContext)
    }

    override fun convert(holder: BaseViewHolder, item: CookerBean) {
        val tv_Voice = holder.getView<TextView>(R.id.tv_voice)
        CoilUtils.loadRound(holder.getView(R.id.iv_avatar), item.user.avatar!!, 3f)
        holder.setText(R.id.tv_name, item.user.userName)
        val tvAge = holder.getView<ShapeView>(R.id.tv_age)
        var address = ""
        if (!TextUtils.isEmpty(item.user.address) && item.user.address!!.split("-").size == 3) {
            address = item.user.address!!.split("-")[1]
        }
        val age = if (item.user.age > 0) {
            "${item.user.age}岁"
        } else {
            ""
        }
        if (TextUtils.isEmpty(age) && TextUtils.isEmpty(address)) {
            tvAge.setText("")
        } else {
            tvAge.setText("$age  ${address}")
        }
        if (item.user.gender == 1) {
            tvAge.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_home_boy_blue, 0, 0, 0)
            tvAge.setTextColor(R.color._515FFF.getResColor())
            tvAge.setShapeSolidColor(R.color._1A515FFF.getResColor()).setUseShape()
        } else {
            tvAge.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_home_girl_blue, 0, 0, 0)
            tvAge.setTextColor(R.color._FF517A.getResColor())
            tvAge.setShapeSolidColor(R.color._1AFF517A.getResColor()).setUseShape()
        }

        holder.setText(R.id.tv_state, item.title)
        if (item.user.onlineStatus!!) {
            holder.setGone(R.id.iv_online, false)
        } else {
            holder.setGone(R.id.iv_online, true)
        }

        if (item.user.greeting?.duration ?: 0 == 0) {
            tv_Voice.gone()
        } else {
            tv_Voice.visible()
            val duration: Int = (item.user.greeting?.duration ?: 0)
            tv_Voice.setText("${duration}s")
        }


        val tagList = item.tag
        if (tagList != null && !tagList.isEmpty()) {
            holder.setGone(R.id.tv_tag, false)
            val stringBuilder = StringBuilder()
            val list = arrayListOf<Int>()
            for (position in 0..tagList.size - 1) {
                if (position < tagList.size - 1) {
                    stringBuilder.append("${tagList.get(position)} ")
                    list.add(stringBuilder.length - 1)
                } else {
                    stringBuilder.append("${tagList.get(position)}")
                }
            }
            val spannableString = SpannableString(stringBuilder.toString());
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

        val likeList = item.likes
        if (likeList != null && !likeList.isEmpty()) {
            holder.setGone(R.id.tv_like, false)
            val stringBuilder = StringBuilder()
            val list = arrayListOf<Int>()
            for (position in 0..likeList.size - 1) {
                if (position < likeList.size - 1) {
                    stringBuilder.append("${likeList.get(position)} ")
                    list.add(stringBuilder.length - 1)
                } else {
                    stringBuilder.append("${likeList.get(position)}")
                }
            }
            val spannableString = SpannableString(stringBuilder.toString());

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


        tv_Voice.setOnClickListener {
            val audioPath = item.user.greeting?.audioPath
            val duration = item.user.greeting?.duration
            if (voicePlayerUtil.isPlaying) {
                //无论播放的语音项是这个还是其他，都先停止语音播放
                voicePlayerUtil.stop()
                // 停止语音播放动画。
                stopVoicePlayAnimation()

                // 如果正在播放的语音项是此项，则只需停止播放即可。
                val playingUrl: String = voicePlayerUtil.url
                if (TextUtils.equals(audioPath, playingUrl)) {
                    return@setOnClickListener
                }
            }
            if (!TextUtils.isEmpty(audioPath) && duration != 0) {
                tvVoice = tv_Voice
                playVoice(audioPath!!)
                // 启动语音播放动画
                startVoicePlayAnimation(duration!!)
            }
        }
    }

    fun startVoicePlayAnimation(duration: Int) {
        countDownTimer = object : CountDownTimer(duration * 1000L, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val second = millisUntilFinished / 1000
                tvVoice.setText("${second}s")
            }

            override fun onFinish() {
                tvVoice.setText("${duration}s")
            }
        }
        countDownTimer!!.start()
    }

    fun stopVoicePlayAnimation() {
        if (countDownTimer != null) {
            countDownTimer = null;
        }
    }

    private fun playVoice(url: String) {
        voicePlayerUtil.play(url, MediaPlayer.OnCompletionListener {
            stopVoicePlayAnimation()
        })
    }

}