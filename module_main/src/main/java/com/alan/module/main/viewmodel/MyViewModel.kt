package com.alan.module.main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.alan.mvvm.base.http.callback.RequestCallback
import com.alan.mvvm.base.http.requestbean.BanRequestBean
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
class MyViewModel @Inject constructor(private val mRepository: CommonRepository) :
    BaseViewModel() {

    val ldSuccess = MutableLiveData<Any>()
    val ldCard = MutableLiveData<Any>()


    fun requestDiamond() {
        viewModelScope.launch {
            mRepository.requestDiamond(callback = RequestCallback(
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
     * 未读消息
     */
    fun requestUnRead() {
        viewModelScope.launch() {
            mRepository.requestUnRead(
                callback = RequestCallback(
                    onSuccess = {
                        ldSuccess.value = it.data!!
                    },
                    onFailed = {
                        ldSuccess.value = it
                    },
                )
            )
        }
    }


    /**
     * 获取列表
     */
    fun requestList(curson: Int, userId: String) {
        val map = hashMapOf<String, String>()
        map.put("cursor", "${curson}")
        map.put("count", "20")
        map.put("userId", userId)
        viewModelScope.launch() {
            mRepository.requestThinkHistory(
                map,
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
     * 获取列表
     */
    fun requestCardList(userId: String) {
        viewModelScope.launch() {
            mRepository.requestCardList(
                userId,
                callback = RequestCallback(
                    onSuccess = {
                        ldCard.value = it
                    },
                    onFailed = {
                        toast(it.errorMessage)
                    },
                )
            )
        }
    }


    /**
     * 赞、取消
     */
    fun requestZan(id: String, action: Int, position: Int) {
        val banRequestBean = BanRequestBean(id, "$action")
        viewModelScope.launch() {
            mRepository.requestZan(
                RequestUtil.getPostBody(banRequestBean),
                callback = RequestCallback(
                    onSuccess = {
                        ldSuccess.value = Pair<Int, Int>(action, position)
                    },
                    onFailed = {
                        toast(it.errorMessage)
                    },
                )
            )
        }
    }
}