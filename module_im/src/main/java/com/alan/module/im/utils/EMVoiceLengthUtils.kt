package com.alan.module.im.utils

import android.content.Context
import com.bigkoo.convenientbanner.utils.ScreenUtil
import com.hyphenate.util.DensityUtil

/**
 * 作者：alan
 * 时间：2021/9/8
 * 备注：用于控制语音视图的长度
 */
object EMVoiceLengthUtils {
    /**
     * 获取语音的长度
     * @param context
     * @param voiceLength
     * @return
     */
    fun getVoiceLength(context: Context?, voiceLength: Int): Int {
        // 先获取屏幕的宽度，取其一半作为最大长度
        // 语音超过20s后长度一致，小于20s的按照时长控制长度
        val maxLength =
            (ScreenUtil.getScreenWidth(context) / 2 - DensityUtil.dip2px(context, 10f)).toFloat()
        val paddingLeft: Float
        paddingLeft = if (voiceLength <= 20) {
            voiceLength / 20f * maxLength + DensityUtil.dip2px(context, 10f)
        } else {
            maxLength + DensityUtil.dip2px(context, 10f)
        }
        return paddingLeft.toInt()
    }
}