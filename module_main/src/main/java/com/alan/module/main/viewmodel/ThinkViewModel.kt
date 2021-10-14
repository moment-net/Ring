package com.alan.module.main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.alan.mvvm.base.http.callback.RequestCallback
import com.alan.mvvm.base.mvvm.vm.BaseViewModel
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
class ThinkViewModel @Inject constructor(private val mRepository: CommonRepository) :
    BaseViewModel() {

    val ldData = MutableLiveData<Any>()
    val ldState = MutableLiveData<Any>()

    /**
     * 获取首页列表
     */
    fun requestList(curson: Int) {
        var map = hashMapOf<String, String>()
        map.put("cursor", "${curson}")
        map.put("count", "20")
        viewModelScope.launch() {
            mRepository.requestMealList(
                map,
                callback = RequestCallback(
                    onSuccess = {
                        ldData.value = it
                    },
                    onFailed = {
                        ldData.value = it
                    },
                )
            )
        }
    }

    /**
     * 查看用户干饭状态
     */
    fun requestMealStatus() {
        viewModelScope.launch() {
            mRepository.requestMealStatus(
                callback = RequestCallback(
                    onSuccess = {
                        ldState.value = it
                    },
                    onFailed = {
                        ldState.value = it
                    },
                )
            )
        }
    }
}