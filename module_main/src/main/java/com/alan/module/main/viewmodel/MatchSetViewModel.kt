package com.alan.module.main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.alan.mvvm.base.http.callback.RequestCallback
import com.alan.mvvm.base.http.responsebean.AcceptBean
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
class MatchSetViewModel @Inject constructor(private val mRepository: CommonRepository) :
    BaseViewModel() {

    val ldSuccess = MutableLiveData<Any>()


    /**
     * 查看用户干饭状态
     */
    fun requestSetInfo() {
        viewModelScope.launch() {
            mRepository.requestSetInfo(
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

    /**
     * 修改匹配设置
     * //1接受 0不接受
     */
    fun requestMatchSet(state: String) {
        val acceptBean = AcceptBean(state)
        viewModelScope.launch() {
            mRepository.requestMatchSet(
                RequestUtil.getPostBody(acceptBean),
                callback = RequestCallback(
                    onSuccess = {
                        ldSuccess.value = true
                    },
                    onFailed = {
                        toast(it.errorMessage)
                    },
                )
            )
        }
    }


}