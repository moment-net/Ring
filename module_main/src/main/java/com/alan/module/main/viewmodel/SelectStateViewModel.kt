package com.alan.module.main.viewmodel

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
class SelectStateViewModel @Inject constructor(private val mRepository: CommonRepository) :
    BaseViewModel() {

    val ldSuccess = MutableLiveData<Any>()


    /**
     * 查看用户干饭状态
     */
    fun requestMealStatus() {
        viewModelScope.launch() {
            mRepository.requestMealStatus(
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
     * 查看用户干饭状态
     */
    fun requestMatchTime() {
        viewModelScope.launch() {
            mRepository.requestMatchTime(
                callback = RequestCallback(
                    onSuccess = {
                        ldSuccess.value = it
                    },
                    onFailed = {
                        toast(it.errorMessage)
                    },
                )
            )
        }
    }

    /**
     * 修改干饭状态
     */
    fun requestEditMeal(orderState: String) {
        viewModelScope.launch() {
            mRepository.requestEditMeal(
                orderState,
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

    /**
     * 开启/关闭匹配
     * 1:开启 0：关闭
     */
    fun requestMealStop(status: String) {
        viewModelScope.launch() {
            mRepository.requestMealStop(
                status,
                callback = RequestCallback(
                    onSuccess = {
                        ldSuccess.value = status
                    },
                    onFailed = {
                        toast(it.errorMessage)
                    },
                )
            )
        }
    }

}