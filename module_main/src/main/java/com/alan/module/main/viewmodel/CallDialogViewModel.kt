package com.alan.module.main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.alan.mvvm.base.http.callback.RequestCallback
import com.alan.mvvm.base.http.requestbean.CallRequestBean
import com.alan.mvvm.base.mvvm.vm.BaseViewModel
import com.alan.mvvm.base.utils.RequestUtil
import com.alan.mvvm.base.utils.toast
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
class CallDialogViewModel @Inject constructor(private val mRepository: CommonRepository) :
    BaseViewModel() {

    val ldSuccess = MutableLiveData<Any>()


    /**
     * 开始聊天
     */
    fun requestChatStart(sessionId: String) {
        val requestBean = CallRequestBean("", sessionId)
        viewModelScope.launch {
            mRepository.requestChatStart(
                RequestUtil.getPostBody(requestBean),
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
     * 挂断聊天
     */
    fun requestChatHangup(sessionId: String) {
        val requestBean = CallRequestBean("", sessionId)
        viewModelScope.launch {
            mRepository.requestChatHangup(
                RequestUtil.getPostBody(requestBean),
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