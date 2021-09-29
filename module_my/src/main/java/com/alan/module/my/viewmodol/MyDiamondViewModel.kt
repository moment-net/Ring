package com.alan.module.my.viewmodol

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
class MyDiamondViewModel @Inject constructor(private val mRepository: CommonRepository) :
    BaseViewModel() {

    val ldSuccess = MutableLiveData<Any>()


    fun requestDiamond() {
        viewModelScope.launch {
            mRepository.requestDiamond(callback = RequestCallback(
                onSuccess = {
                    ldSuccess.value = it.data!!
                },
                onFailed = {

                }
            ))
        }
    }


    fun requestGoodsList() {
        viewModelScope.launch {
            mRepository.requestGoodsList(
                "android", callback = RequestCallback(
                    onSuccess = {
                        ldSuccess.value = it
                    },
                    onFailed = {
                        toast(it.errorMessage)
                    }
                ))
        }
    }

}