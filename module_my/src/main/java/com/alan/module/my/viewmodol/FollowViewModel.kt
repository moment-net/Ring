package com.alan.module.my.viewmodol

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.alan.mvvm.base.http.callback.RequestCallback
import com.alan.mvvm.base.http.requestbean.FollowRequestBean
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
class FollowViewModel @Inject constructor(private val mRepository: CommonRepository) :
    BaseViewModel() {

    val ldSuccess = MutableLiveData<Any>()

    fun requestChangeFollow(position: Int, userId: String, tag: Int) {
        var requestBean = FollowRequestBean(userId, tag, "0")
        viewModelScope.launch {
            mRepository.requestChangeFollow(
                RequestUtil.getPostBody(requestBean), callback = RequestCallback(
                    onSuccess = {
                        ldSuccess.value = position
                    },
                    onFailed = {
                        toast(it.errorMessage)
                    }
                ))
        }
    }


    fun requestFansList(curson: Int) {
        var map = hashMapOf<String, String>()
        map.put("cursor", "${curson}")
        map.put("count", "20")
        viewModelScope.launch {
            mRepository.requestFansList(
                map, callback = RequestCallback(
                    onSuccess = {
                        ldSuccess.value = it
                    },
                    onFailed = {
                        toast(it.errorMessage)
                    }
                ))
        }
    }


    fun requestFollowList(curson: Int) {
        var map = hashMapOf<String, String>()
        map.put("cursor", "${curson}")
        map.put("count", "20")
        viewModelScope.launch {
            mRepository.requestFollowList(
                map, callback = RequestCallback(
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