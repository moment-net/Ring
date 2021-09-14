package com.alan.mvvm.common.im.utils

import android.content.Context
import android.media.MediaRecorder
import android.os.Handler
import android.os.Message
import android.os.SystemClock
import com.hyphenate.EMError
import com.hyphenate.util.EMLog
import java.io.File
import java.io.IOException
import java.util.*

class VoiceRecorderUtil(val handler: Handler) {
    var recorder: MediaRecorder? = null
    var isRecording = false
    var startTime: Long = 0
    var file: File? = null
    var voiceFilePath: String? = null


    fun startRecording(appContext: Context?): String? {
        file = null
        try {
            if (recorder != null) {
                recorder!!.release()
                recorder = null
            }
            recorder = MediaRecorder()
            recorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
            recorder!!.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB)
            recorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            recorder!!.setAudioChannels(1) // MONO
            recorder!!.setAudioSamplingRate(8000) // 8000Hz
            recorder!!.setAudioEncodingBitRate(64) // seems if change this to
            voiceFilePath = com.alan.mvvm.common.constant.IMConstant.PATH_VOICE;
            file = File(voiceFilePath!!)
            recorder!!.setOutputFile(file!!.absolutePath)
            recorder!!.prepare()
            isRecording = true
            recorder!!.start()
        } catch (e: IOException) {
            EMLog.e("voice", "prepare() failed")
        }
        Thread {
            try {
                while (isRecording) {
                    val msg = Message()
                    msg.what = recorder!!.maxAmplitude * 13 / 0x7FFF
                    handler.sendMessage(msg)
                    SystemClock.sleep(100)
                }
            } catch (e: Exception) {
                EMLog.e("voice", e.toString())
            }
        }.start()
        startTime = Date().time
        EMLog.d("voice", "start voice recording to file:" + file!!.absolutePath)
        return if (file == null) null else file!!.getAbsolutePath()
    }


    fun discardRecording() {
        if (recorder != null) {
            try {
                recorder!!.stop()
                recorder!!.release()
                recorder = null
                if (file != null && file!!.exists() && !file!!.isDirectory) {
                    file!!.delete()
                }
            } catch (e: IllegalStateException) {
            } catch (e: RuntimeException) {
            }
            isRecording = false
        }
    }

    fun stopRecoding(): Int {
        if (recorder != null) {
            isRecording = false
            recorder!!.stop()
            recorder!!.release()
            recorder = null
            if (file == null || !file!!.exists() || !file!!.isFile) {
                return EMError.FILE_INVALID
            }
            if (file!!.length() == 0L) {
                file!!.delete()
                return EMError.FILE_INVALID
            }
            val seconds = (Date().time - startTime).toInt() / 1000
            EMLog.d(
                "voice",
                "voice recording finished. seconds:" + seconds + " file length:" + file!!.length()
            )
            return seconds
        }
        return 0
    }


    @Throws(Throwable::class)
    fun finalize() {
        if (recorder != null) {
            recorder!!.release()
        }
    }

}