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
class MessageViewModel @Inject constructor(private val mRepository: CommonRepository) :
    BaseViewModel() {

    val ldData = MutableLiveData<Any>()


    /**
     * 获取列表
     */
    fun requestList(curson: Int) {
        val map = hashMapOf<String, String>()
        map.put("cursor", "${curson}")
        map.put("count", "20")
        map.put("type", "2");
        viewModelScope.launch() {
            mRepository.requestMessage(
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
     * 未读消息
     */
    fun requestUnRead() {
        viewModelScope.launch() {
            mRepository.requestUnRead(
                callback = RequestCallback(
                    onSuccess = {
                        ldData.value = it.data!!
                    },
                    onFailed = {
                        ldData.value = it
                    },
                )
            )
        }
    }

    fun requestChangeFollow(userId: String, tag: Int) {
        val requestBean = FollowRequestBean(userId, tag, "0")
        viewModelScope.launch {
            mRepository.requestChangeFollow(
                RequestUtil.getPostBody(requestBean), callback = RequestCallback(
                    onSuccess = {
                        ldData.value = tag
                    },
                    onFailed = {
                        toast(it.errorMessage)
                    }
                ))
        }
    }
}