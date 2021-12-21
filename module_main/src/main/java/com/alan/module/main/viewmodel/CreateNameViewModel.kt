package com.alan.module.main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.alan.mvvm.base.http.callback.RequestCallback
import com.alan.mvvm.base.http.requestbean.EditRequestBean
import com.alan.mvvm.base.mvvm.vm.BaseViewModel
import com.alan.mvvm.base.utils.RequestUtil
import com.alan.mvvm.base.utils.toast
import com.alan.mvvm.common.helper.SpHelper
import com.alan.mvvm.common.http.model.CommonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 作者：alan
 * 时间：2021/7/28
 * 备注：首页的VM层
 * @property mRepository CommonRepository 仓库层 通过Hilt注入
 */
@HiltViewModel
class CreateNameViewModel @Inject constructor(private val mRepository: CommonRepository) :
    BaseViewModel() {

    val ldSuccess = MutableLiveData<Any>()


    /**
     * 更改个人信息
     */
    fun requestEditUserInfo(
        userName: String,
        birthday: String,
    ) {
        val requestBean = EditRequestBean(
            userName, "", "", 0, birthday, "", ""
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

}