package com.alan.mvvm.base.utils

import android.media.MediaPlayer
import java.io.IOException

object MediaPlayUtil {

    var mediaPlayer: MediaPlayer

    init {
        mediaPlayer = MediaPlayer()
    }


    fun play(path: String?) {
        try {
            mediaPlayer.reset() //如果正在播放，则重置为初始状态
            mediaPlayer.setDataSource(path)
            mediaPlayer.prepare() //缓冲
            mediaPlayer.start() //开始或恢复播放
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    fun release() {
        mediaPlayer.stop()
        mediaPlayer.setOnCompletionListener(null)
    }
}