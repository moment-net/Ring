package com.alan.module.main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.alan.mvvm.base.http.callback.RequestCallback
import com.alan.mvvm.base.http.requestbean.BanRequestBean
import com.alan.mvvm.base.http.requestbean.ToUserIdRequestBean
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
class NowViewModel @Inject constructor(private val mRepository: CommonRepository) :
    BaseViewModel() {

    val ldData = MutableLiveData<Any>()

    /**
     * 获取首页列表
     */
    fun requestList(curson: Int) {
        val map = hashMapOf<String, String>()
        map.put("cursor", "${curson}")
        map.put("count", "20")
        viewModelScope.launch() {
            mRepository.requestNowList(
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
     * 屏蔽
     */
    fun requestBanNow(id: String, position: Int) {
        val banRequestBean = BanRequestBean(id, "-1")
        viewModelScope.launch() {
            mRepository.requestBanNow(
                RequestUtil.getPostBody(banRequestBean),
                callback = RequestCallback(
                    onSuccess = {
                        ldData.value = position
                    },
                    onFailed = {
                        toast(it.errorMessage)
                    },
                )
            )
        }
    }

    /**
     * 聊天检测（是否被已回复）
     */
    fun requestIsReply(userId: String, userName: String) {
        val requestBean = ToUserIdRequestBean(userId)
        viewModelScope.launch() {
            mRepository.requestIsReply(
                RequestUtil.getPostBody(requestBean),
                callback = RequestCallback(
                    onSuccess = {
                        ldData.value = Pair<String, String>(userId, userName)
                    },
                    onFailed = {
                        toast(it.errorMessage)
                    },
                )
            )
        }
    }
}