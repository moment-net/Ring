package com.alan.module.home.viewmodol

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.alan.mvvm.base.http.callback.RequestCallback
import com.alan.mvvm.base.http.requestbean.BanRequestBean
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
class ManagerInfoViewModel @Inject constructor(private val mRepository: CommonRepository) :
    BaseViewModel() {

    val ldSuccess = MutableLiveData<Any>()


    fun requestChangeFollow(userId: String, tag: Int) {
        val requestBean = FollowRequestBean(userId, tag, "0")
        viewModelScope.launch {
            mRepository.requestChangeFollow(
                RequestUtil.getPostBody(requestBean), callback = RequestCallback(
                    onSuccess = {
                        ldSuccess.value = tag
                    },
                    onFailed = {
                        toast(it.errorMessage)
                    }
                ))
        }
    }


    /**
     * 获取个人信息
     */
    fun requestUserInfo(userId: String) {
        viewModelScope.launch {
            mRepository.requestUserInfoDetail(
                userId,
                callback = RequestCallback(
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