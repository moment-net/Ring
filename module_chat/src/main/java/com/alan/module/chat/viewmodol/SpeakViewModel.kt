package com.alan.module.chat.viewmodol

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.alan.mvvm.base.http.callback.RequestCallback
import com.alan.mvvm.base.mvvm.vm.BaseViewModel
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
class SpeakViewModel @Inject constructor(private val mRepository: CommonRepository) :
    BaseViewModel() {

    val ldSuccess = MutableLiveData<Any>()


    fun requestChatBg(userId: String) {
        viewModelScope.launch {
            mRepository.requestChatBg(
                userId,
                callback = RequestCallback(
                    onSuccess = {
                        ldSuccess.value = it.data!!
                    },
                    onFailed = {
                        toast(it.errorMessage)
                    },
                )
            )
        }
    }


}