package com.alan.module.chat.viewmodol

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.alan.mvvm.base.http.callback.RequestCallback
import com.alan.mvvm.base.http.requestbean.CallRequestBean
import com.alan.mvvm.base.http.requestbean.FollowRequestBean
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
class ChatDetailViewModel @Inject constructor(private val mRepository: CommonRepository) :
    BaseViewModel() {

    val ldSuccess = MutableLiveData<Any>()


    /**
     * 用户是否在饭友匹配中
     */
    fun requestCheckMatch(userId: String) {
        viewModelScope.launch {
            mRepository.requestCheckMatch(
                userId, callback = RequestCallback(
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
     * 关注
     */
    fun requestChangeFollow(userId: String, tag: Int) {
        var requestBean = FollowRequestBean(userId, tag, "0")
        viewModelScope.launch {
            mRepository.requestChangeFollow(
                RequestUtil.getPostBody(requestBean), callback = RequestCallback(
                    onSuccess = {
                        requestCheckMatch(userId)
                        requestUserInfo(userId)
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
            mRepository.requestUserInfo(
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
     * 开始聊天
     */
    fun requestChatStart(userId: String) {
        val requestBean = CallRequestBean(userId, "")
        viewModelScope.launch {
            mRepository.requestChatStart(
                RequestUtil.getPostBody(requestBean),
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
     * 聊天回复
     */
    fun requestReply(userId: String) {
        val requestBean = ToUserIdRequestBean(userId)
        viewModelScope.launch() {
            mRepository.requestReply(
                RequestUtil.getPostBody(requestBean),
                callback = RequestCallback(
                    onSuccess = {

                    },
                    onFailed = {
                        toast(it.errorMessage)
                    },
                )
            )
        }
    }
}