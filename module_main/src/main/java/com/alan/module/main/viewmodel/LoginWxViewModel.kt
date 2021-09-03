package com.alan.module.main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.alan.mvvm.base.http.callback.RequestCallback
import com.alan.mvvm.base.http.requestbean.EditRequestBean
import com.alan.mvvm.base.http.requestbean.LoginThirdRequestBean
import com.alan.mvvm.base.mvvm.vm.BaseViewModel
import com.alan.mvvm.base.utils.RequestUtil
import com.alan.mvvm.base.utils.toast
import com.alan.mvvm.common.helper.SpHelper
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

    var ldSuccess = MutableLiveData<Any>()
    var ldFailer = MutableLiveData<Any>()


    /**
     * 更改个人信息
     */
    fun requestEditUserInfo(
        userName: String,
        desc: String,
        url: String,
        gender: Int,
        birthday: String,
        hometown: String,
        address: String
    ) {
        val requestBean = EditRequestBean(
            userName, desc, url, gender, birthday, hometown = hometown, address = address
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


    /**
     * 上传图片
     */
    fun requestUploadPic(url: String) {
        val file = File(url)
        viewModelScope.launch {
            mRepository.requestUploadPic(
                RequestUtil.getPostPart(RequestUtil.PART_TYPE_IMAGE, file),
                callback = RequestCallback(
                    onSuccess = {
                        ldSuccess.value = it.data!!
                    },
                    onFailed = {
                        toast(it.errorMessage)
                    }
                ))
        }
    }


    /**
     * 微信绑定
     */
    fun requestBindWX(requestBean: LoginThirdRequestBean) {
        viewModelScope.launch {
            mRepository.requestBindThird(RequestUtil.getPostBody(requestBean),
                callback = RequestCallback(
                    onSuccess = {
                        ldSuccess.value = it.data!!
                    },
                    onFailed = {
                        toast(it.errorMessage)
                    }
                ))
        }
    }

}