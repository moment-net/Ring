package com.alan.module.main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.alan.mvvm.base.http.callback.RequestCallback
import com.alan.mvvm.base.mvvm.vm.BaseViewModel
import com.alan.mvvm.base.utils.AudioRecorder
import com.alan.mvvm.base.utils.RequestUtil
import com.alan.mvvm.base.utils.record.RecordSuccessListener
import com.alan.mvvm.base.utils.toast
import com.alan.mvvm.common.constant.Constants
import com.alan.mvvm.common.http.model.CommonRepository
import com.socks.library.KLog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

/**
 * 作者：alan
 * 时间：2021/7/28
 * 备注：首页的VM层
 * @property mRepository CommonRepository 仓库层 通过Hilt注入
 */
@HiltViewModel
class SoundTestViewModel @Inject constructor(private val mRepository: CommonRepository) :
    BaseViewModel() {

    var audioRecorder: AudioRecorder = AudioRecorder.instance
    val ldData = MutableLiveData<Any>()


    /**
     * 开始录制
     */
    fun startRecorder() {
//        try {
//            mediaRecorder = MediaRecorder()
//            //输出目录
//            mediaRecorder!!.setOutputFile(Constants.PATH_SOUND_TEST)
//            //设置采集源
//            mediaRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
//            //设置输出格式 3GPP media file format
//            mediaRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
//            //编解码的格式 AMR (Narrowband) audio codec
//            mediaRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
//            mediaRecorder!!.prepare()
//            mediaRecorder!!.start()
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
        try {
            if (audioRecorder.getStatus() === AudioRecorder.Status.STATUS_NO_READY) {
                //初始化录音
                val fileName = Constants.PATH_SOUND_TEST
                audioRecorder.createDefaultAudio(fileName, object : RecordSuccessListener {

                    override fun recordSuccess(path: String?) {
                        KLog.e("xujm", "录制回掉$path")
                        requestUploadAudio(path!!)
                    }
                })
                audioRecorder.startRecord(null)
            }
        } catch (e: IllegalStateException) {
            toast(e.message!!)
        }
    }

    /**
     * 停止录制
     */
    fun stopRecorder() {
        //停止录音
        audioRecorder.stopRecord()
    }


    /**
     * 获取配置
     */
    fun requestConfigByName() {
        viewModelScope.launch {
            mRepository.requestConfigByName(
                "tts_guide",
                callback = RequestCallback(
                    onSuccess = {
                        ldData.value = it.data!!
                    },
                    onFailed = {
                    }
                ))
        }
    }

    /**
     * 上传声音
     */
    fun requestUploadAudio(path: String) {
        val file = File(path)
        if (!file.exists()) {
            return
        }
        viewModelScope.launch {
            mRepository.requestUploadAudio(
                RequestUtil.getPostPart(RequestUtil.PART_TYPE_VOICE, file),
                callback = RequestCallback(
                    onSuccess = {
                        ldData.value = it.data!!
                    },
                    onFailed = {
                        toast(it.errorMessage)
                        ldData.value = false
                    }
                ))
        }
    }


}