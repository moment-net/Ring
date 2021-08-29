package com.alan.module.main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.alan.mvvm.base.http.callback.RequestCallback
import com.alan.mvvm.base.http.requestbean.EditRequestBean
import com.alan.mvvm.base.http.responsebean.TargetBean
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
class SelectTargetViewModel @Inject constructor(private val mRepository: CommonRepository) :
    BaseViewModel() {

    val ldSuccess = MutableLiveData<Any>()


    fun requestList() {
        viewModelScope.launch {
            mRepository.requestTargetList(
                callback = RequestCallback(
                    onSuccess = {
                        ldSuccess.value = it.data!!
                    },
                    onFailed = {

                    },
                )
            )
        }
    }


    fun requestSaveTarget(list: ArrayList<String>) {
        var requestBean = TargetBean(list)
        viewModelScope.launch {
            mRepository.requestSaveTarget(
                RequestUtil.getPostBody(requestBean),
                callback = RequestCallback(
                    onSuccess = {
                        ldSuccess.value = 1
                        toast(it.msg)
                    },
                    onFailed = {
                        toast(it.errorMessage)
                    },
                )
            )
        }
    }

}