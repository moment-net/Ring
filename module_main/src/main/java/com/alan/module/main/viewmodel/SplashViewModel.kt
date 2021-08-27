package com.alan.module.main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.alan.mvvm.base.http.callback.RequestCallback
import com.alan.mvvm.base.mvvm.vm.BaseViewModel
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
class SplashViewModel @Inject constructor(private val mRepository: CommonRepository) :
    BaseViewModel() {

    val ldResult = MutableLiveData<Boolean>()

    /**
     * 重新登录
     */
    fun requestAutoLogin() {
        viewModelScope.launch {
            mRepository.requestAutoLogin(
                callback = RequestCallback(
                    onSuccess = {
                        ldResult.value = true
                        SpHelper.updateUserInfo(it.data)
                    },
                    onFailed = {
                        ldResult.value = false
                        SpHelper.clearUserInfo()
                    }
                ))
        }
    }


}