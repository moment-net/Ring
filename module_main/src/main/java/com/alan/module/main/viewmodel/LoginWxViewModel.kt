package com.alan.module.main.viewmodel

import androidx.lifecycle.viewModelScope
import com.alan.mvvm.base.http.callback.RequestCallback
import com.alan.mvvm.base.http.requestbean.EditRequestBean
import com.alan.mvvm.base.mvvm.vm.BaseViewModel
import com.alan.mvvm.base.utils.RequestUtil
import com.alan.mvvm.common.http.model.CommonRepository
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
class LoginWxViewModel @Inject constructor(private val mRepository: CommonRepository) :
    BaseViewModel() {


    /**
     * 获取个人信息
     */
    fun requestEditUserInfo(
        userName: String,
        desc: String,
        url: String,
        gender: Int,
        hometown: String,
        address: String
    ) {
        val requestBean = EditRequestBean(
            userName, desc, url, gender, hometown = hometown, address = address
        )

        viewModelScope.launch {
            mRepository.requestEditUserInfo(
                RequestUtil.getPostBody(requestBean),
                callback = RequestCallback(
                    onSuccess = {
                    },
                    onFailed = {
                    }
                ))
        }
    }


    /**
     * 更改个人信息
     */
    fun requestUserInfo(userId: String) {
        viewModelScope.launch {
            mRepository.requestUserInfo(
                userId,
                callback = RequestCallback(
                    onSuccess = {
                    },
                    onFailed = {
                    }
                ))
        }
    }


    /**
     * 上传图片
     */
    fun requestDevicesRegister(url: String) {
        val file = File(url)
        viewModelScope.launch {
            mRepository.requestUploadPic(
                RequestUtil.getPostPart(file),
                callback = RequestCallback(
                    onSuccess = {
                    },
                    onFailed = {
                    }
                ))
        }
    }


}