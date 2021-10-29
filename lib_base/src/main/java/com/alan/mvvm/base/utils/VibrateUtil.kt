package com.alan.mvvm.base.utils

import android.annotation.SuppressLint
import android.app.Service
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import com.alan.mvvm.base.BaseApplication

/**
 * 作者：alan
 * 时间：2020/12/22
 * 备注：震动工具
 */
object VibrateUtil {
    /**
     * 震动1s
     */
    @SuppressLint("MissingPermission")
    fun starVibrate() {
        val vib: Vibrator =
            BaseApplication.mApplication.getSystemService(Service.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vib.vibrate(VibrationEffect.createOneShot(2000, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vib.vibrate(2000)
        }
    }

    /**
     * @param pattern  震动频率
     * @param isRepeat 是否循环执行震动
     */
    @SuppressLint("MissingPermission")
    fun starVibrate(pattern: LongArray?, isRepeat: Boolean) {
        val vib: Vibrator =
            BaseApplication.mApplication.getSystemService(Service.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vib.vibrate(VibrationEffect.createWaveform(pattern, if (isRepeat) 1 else -1))
        } else {
            vib.vibrate(pattern, if (isRepeat) 1 else -1)
        }
    }

    @SuppressLint("MissingPermission")
    fun stopVibrate() {
        val vib: Vibrator =
            BaseApplication.mApplication.getSystemService(Service.VIBRATOR_SERVICE) as Vibrator
        vib.cancel()
    }
}