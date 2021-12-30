package com.alan.mvvm.base.utils

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.text.TextUtils
import android.util.Log
import com.alan.mvvm.base.utils.record.PcmToWav
import com.alan.mvvm.base.utils.record.RecordStreamListener
import com.alan.mvvm.base.utils.record.RecordSuccessListener
import com.socks.library.KLog
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


/**
 * 作者：alan
 * 时间：2021/12/27
 * 备注：实现录音wav格式
 */
class AudioRecorder private constructor() {
    // 缓冲区字节大小
    private var bufferSizeInBytes = 0

    //录音对象
    private var audioRecord: AudioRecord? = null

    //录音状态
    private var status: AudioRecorder.Status = AudioRecorder.Status.STATUS_NO_READY

    //文件名
    private var fileName: String? = null

    //录制成功监听
    private var successListener: RecordSuccessListener? = null

    //录音文件
    private val filesName: MutableList<String?> = ArrayList()

    /**
     * 类级的内部类，也就是静态类的成员式内部类，该内部类的实例与外部类的实例
     * 没有绑定关系，而且只有被调用时才会装载，从而实现了延迟加载
     */
    private object AudioRecorderHolder {
        /**
         * 静态初始化器，由JVM来保证线程安全
         */
        val instance = AudioRecorder()
    }

    companion object {
        //音频输入-麦克风
        private const val AUDIO_INPUT = MediaRecorder.AudioSource.MIC

        //采用频率
        //44100是目前的标准，但是某些设备仍然支持22050，16000，11025
        //采样频率一般共分为22.05KHz、44.1KHz、48KHz三个等级
        private const val AUDIO_SAMPLE_RATE = 16000

        //声道 单声道
        private const val AUDIO_CHANNEL = AudioFormat.CHANNEL_IN_MONO

        //编码
        private const val AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT
        val instance: AudioRecorder
            get() = AudioRecorderHolder.instance
    }

    /**
     * 创建录音对象
     */
    fun createAudio(
        fileName: String?,
        audioSource: Int,
        sampleRateInHz: Int,
        channelConfig: Int,
        audioFormat: Int
    ) {
        // 获得缓冲区字节大小
        bufferSizeInBytes = AudioRecord.getMinBufferSize(
            sampleRateInHz,
            channelConfig, channelConfig
        )
        audioRecord =
            AudioRecord(audioSource, sampleRateInHz, channelConfig, audioFormat, bufferSizeInBytes)
        this.fileName = fileName
    }

    /**
     * 创建默认的录音对象
     *
     * @param fileName 文件名
     */
    fun createDefaultAudio(fileName: String?, successListener: RecordSuccessListener?) {
        // 获得缓冲区字节大小
        bufferSizeInBytes = AudioRecord.getMinBufferSize(
            AudioRecorder.Companion.AUDIO_SAMPLE_RATE,
            AudioRecorder.Companion.AUDIO_CHANNEL,
            AudioRecorder.Companion.AUDIO_ENCODING
        )
        audioRecord = AudioRecord(
            AudioRecorder.Companion.AUDIO_INPUT,
            AudioRecorder.Companion.AUDIO_SAMPLE_RATE,
            AudioRecorder.Companion.AUDIO_CHANNEL,
            AudioRecorder.Companion.AUDIO_ENCODING,
            bufferSizeInBytes
        )
        this.fileName = fileName
        this.successListener = successListener
        status = AudioRecorder.Status.STATUS_READY
    }

    /**
     * 开始录音
     *
     * @param listener 音频流的监听
     */
    fun startRecord(listener: RecordStreamListener?) {
        check(!(status == AudioRecorder.Status.STATUS_NO_READY || TextUtils.isEmpty(fileName))) { "录音尚未初始化,请检查是否禁止了录音权限~" }
        check(status != AudioRecorder.Status.STATUS_START) { "正在录音" }
        Log.d("AudioRecorder", "===startRecord===" + audioRecord!!.state)
        audioRecord!!.startRecording()
        Thread { writeDataTOFile(listener) }.start()
    }

    /**
     * 暂停录音
     */
    fun pauseRecord() {
        Log.d("AudioRecorder", "===pauseRecord===")
        status = if (status != AudioRecorder.Status.STATUS_START) {
            throw IllegalStateException("没有在录音")
        } else {
            audioRecord!!.stop()
            AudioRecorder.Status.STATUS_PAUSE
        }
    }

    /**
     * 停止录音
     */
    fun stopRecord() {
        Log.d("AudioRecorder", "===stopRecord===")
        check(!(status == AudioRecorder.Status.STATUS_NO_READY || status == AudioRecorder.Status.STATUS_READY)) { "录音尚未开始" }
        audioRecord!!.stop()
        status = AudioRecorder.Status.STATUS_STOP
        release()
    }

    /**
     * 释放资源
     */
    fun release() {
        Log.d("AudioRecorder", "===release===")
        //假如有暂停录音
        try {
            if (filesName.size > 0) {
                val filePaths: MutableList<String> = ArrayList()
                for (fileName in filesName) {
                    filePaths.add(FileUtil.getPcmFileAbsolutePath(fileName!!)!!)
                }
                //清除
                filesName.clear()
                //将多个pcm文件转化为wav文件
                mergePCMFilesToWAVFile(filePaths)
            } else {
                //这里由于只要录音过filesName.size都会大于0,没录音时fileName为null
                //会报空指针 NullPointerException
                // 将单个pcm文件转化为wav文件
                //Log.d("AudioRecorder", "=====makePCMFileToWAVFile======");
                //makePCMFileToWAVFile();
            }
        } catch (e: IllegalStateException) {
            throw IllegalStateException(e.message)
        }
        if (audioRecord != null) {
            audioRecord!!.release()
            audioRecord = null
        }
        status = AudioRecorder.Status.STATUS_NO_READY
    }

    /**
     * 取消录音
     */
    fun canel() {
        filesName.clear()
        fileName = null
        if (audioRecord != null) {
            audioRecord!!.release()
            audioRecord = null
        }
        status = AudioRecorder.Status.STATUS_NO_READY
    }

    /**
     * 将音频信息写入文件
     *
     * @param listener 音频流的监听
     */
    private fun writeDataTOFile(listener: RecordStreamListener?) {
        // new一个byte数组用来存一些字节数据，大小为缓冲区大小
        val audiodata = ByteArray(bufferSizeInBytes)
        var fos: FileOutputStream? = null
        var readsize = 0
        try {
            var currentFileName = fileName
            if (status == AudioRecorder.Status.STATUS_PAUSE) {
                //假如是暂停录音 将文件名后面加个数字,防止重名文件内容被覆盖
                currentFileName += filesName.size
            }
            filesName.add(currentFileName)
            val file: File = File(FileUtil.getPcmFileAbsolutePath(currentFileName!!))
            if (file.exists()) {
                file.delete()
            }
            fos = FileOutputStream(file) // 建立一个可存取字节的文件
        } catch (e: IllegalStateException) {
            Log.e("AudioRecorder", e.message!!)
            throw IllegalStateException(e.message)
        } catch (e: FileNotFoundException) {
            Log.e("AudioRecorder", e.message!!)
        }
        //将录音状态设置成正在录音状态
        status = AudioRecorder.Status.STATUS_START
        while (status == AudioRecorder.Status.STATUS_START) {
            readsize = audioRecord!!.read(audiodata, 0, bufferSizeInBytes)
            if (AudioRecord.ERROR_INVALID_OPERATION != readsize && fos != null) {
                try {
                    fos.write(audiodata)
                    if (listener != null) {
                        //用于拓展业务
                        listener.recordOfByte(audiodata, 0, audiodata.size)
                    }
                } catch (e: IOException) {
                    Log.e("AudioRecorder", e.message!!)
                }
            }
        }
        try {
            fos?.close()
        } catch (e: IOException) {
            Log.e("AudioRecorder", e.message!!)
        }
    }

    /**
     * 将pcm合并成wav
     *
     * @param filePaths
     */
    private fun mergePCMFilesToWAVFile(filePaths: List<String>) {
        Thread {
            if (PcmToWav.mergePCMFilesToWAVFile(
                    filePaths,
                    FileUtil.getWavFileAbsolutePath(fileName)
                )
            ) {
                KLog.e("xujm", "录制成功${successListener}")
                //操作成功
                if (successListener != null) {
                    successListener?.recordSuccess(FileUtil.getWavFileAbsolutePath(fileName))
                }
            } else {
                KLog.e("xujm", "录制失败")
                //操作失败
                Log.e("AudioRecorder", "mergePCMFilesToWAVFile fail")
                throw IllegalStateException("mergePCMFilesToWAVFile fail")
            }
            fileName = null
        }.start()
    }

    /**
     * 将单个pcm文件转化为wav文件
     */
    private fun makePCMFileToWAVFile() {
        Thread {
            if (PcmToWav.makePCMFileToWAVFile(
                    FileUtil.getPcmFileAbsolutePath(fileName!!),
                    FileUtil.getWavFileAbsolutePath(fileName), true
                )
            ) {
                //操作成功
            } else {
                //操作失败
                Log.e("AudioRecorder", "makePCMFileToWAVFile fail")
                throw IllegalStateException("makePCMFileToWAVFile fail")
            }
            fileName = null
        }.start()
    }

    /**
     * 获取录音对象的状态
     *
     * @return
     */
    fun getStatus(): AudioRecorder.Status {
        return status
    }

    /**
     * 获取本次录音文件的个数
     *
     * @return
     */
    val pcmFilesCount: Int
        get() = filesName.size

    /**
     * 录音对象的状态
     */
    enum class Status {
        //未开始
        STATUS_NO_READY,  //预备
        STATUS_READY,  //录音
        STATUS_START,  //暂停
        STATUS_PAUSE,  //停止
        STATUS_STOP
    }


}