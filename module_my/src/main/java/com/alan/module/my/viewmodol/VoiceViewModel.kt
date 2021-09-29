package com.alan.module.my.viewmodol

import android.media.MediaPlayer
import android.media.MediaRecorder
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.alan.mvvm.base.http.callback.RequestCallback
import com.alan.mvvm.base.http.requestbean.EditRequestBean
import com.alan.mvvm.base.mvvm.vm.BaseViewModel
import com.alan.mvvm.base.utils.RequestUtil
import com.alan.mvvm.base.utils.toast
import com.alan.mvvm.common.constant.Constants
import com.alan.mvvm.common.helper.SpHelper
import com.alan.mvvm.common.http.model.CommonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import javax.inject.Inject

/**
 * 作者：alan
 * 时间：2021/7/28
 * 备注：首页的VM层
 * @property mRepository CommonRepository 仓库层 通过Hilt注入
 */
@HiltViewModel
class VoiceViewModel @Inject constructor(private val mRepository: CommonRepository) :
    BaseViewModel() {

    var mediaRecorder: MediaRecorder? = null
    val ldSuccess = MutableLiveData<Any>()


    /**
     * 开始录制
     */
    fun startRecorder() {
        try {
            mediaRecorder = MediaRecorder()
            //输出目录
            mediaRecorder!!.setOutputFile(Constants.PATH_GREET_SELF)
            //设置采集源
            mediaRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
            //设置输出格式 3GPP media file format
            mediaRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            //编解码的格式 AMR (Narrowband) audio codec
            mediaRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            mediaRecorder!!.prepare()
            mediaRecorder!!.start()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * 停止录制
     */
    fun stopRecorder() {
        if (mediaRecorder != null) {
            mediaRecorder!!.stop()
            mediaRecorder!!.release()
            mediaRecorder = null
        }
    }


    /**
     * 上传声音
     */
    fun requestUploadAudio() {
        val file = File(Constants.PATH_GREET_SELF)
        if (!file.exists()) {
            return
        }
        var duration = 0
        var mediaPlayer: MediaPlayer? = null
        try {
            mediaPlayer = MediaPlayer()
            mediaPlayer.setDataSource(Constants.PATH_GREET_SELF)
            mediaPlayer.prepare()
            duration = mediaPlayer.duration / 1000 //时长
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (mediaPlayer != null) {
                mediaPlayer.release()
                mediaPlayer = null
            }
        }
        viewModelScope.launch {
            mRepository.requestUploadAudio(
                RequestUtil.getPostStringPart("type", "greeting"),
                RequestUtil.getPostStringPart("duration", "${duration}"),
                RequestUtil.getPostPart(RequestUtil.PART_TYPE_AUDIO, file),
                callback = RequestCallback(
                    onSuccess = {
                        toast("语音签名上传成功")
                        requestEditUserInfo(it.data.fileName, duration)
                    },
                    onFailed = {
                    }
                ))
        }
    }


    /**
     * 更改个人信息
     */
    fun requestEditUserInfo(
        audioUrl: String,
        duration: Int
    ) {
        val requestBean = EditRequestBean(
            audioGreeting = audioUrl, audioDuration = duration
        )

        viewModelScope.launch {
            mRepository.requestEditUserInfo(
                RequestUtil.getPostBody(requestBean),
                callback = RequestCallback(
                    onSuccess = {
                        requestUserInfo(SpHelper.getUserInfo()?.userId!!)
                    },
                    onFailed = {
                        toast(it.errorMessage)
                    }
                ))
        }
    }


    /**
     * 获取个人信息
     */
    fun requestUserInfo(userId: String) {
        viewModelScope.launch {
            mRepository.requestUserInfo(
                userId,
                callback = RequestCallback(
                    onSuccess = {
                        SpHelper.updateUserInfo(it.data)
                        ldSuccess.value = it.data!!
                    },
                    onFailed = {
                    }
                ))
        }
    }

    override fun onCleared() {
        super.onCleared()
        if (mediaRecorder != null) {
            mediaRecorder!!.stop()
            mediaRecorder = null
        }
    }
}