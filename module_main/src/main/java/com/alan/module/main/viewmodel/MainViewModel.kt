package com.alan.module.main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.alan.mvvm.base.http.callback.RequestCallback
import com.alan.mvvm.base.http.requestbean.CallRequestBean
import com.alan.mvvm.base.mvvm.vm.BaseViewModel
import com.alan.mvvm.base.utils.RequestUtil
import com.alan.mvvm.base.utils.toast
import com.alan.mvvm.common.helper.SpHelper
import com.alan.mvvm.common.http.exception.BaseHttpException
import com.alan.mvvm.common.http.model.CommonRepository
import com.alan.mvvm.common.im.EMClientHelper
import com.hyphenate.chat.EMConversation
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
class MainViewModel @Inject constructor(private val mRepository: CommonRepository) :
    BaseViewModel() {

    val ldCard = MutableLiveData<Any>()
    val ldIM = MutableLiveData<BaseHttpException>()

    /**
     * 开始聊天
     */
    fun requestChatStart(sessionId: String) {
        val requestBean = CallRequestBean(sessionId = sessionId)
        viewModelScope.launch {
            mRepository.requestChatStart(
                RequestUtil.getPostBody(requestBean),
                callback = RequestCallback(
                    onSuccess = {
                    },
                    onFailed = {
                    }
                ))
        }
    }

    /**
     * 挂断聊天
     */
    fun requestChatHangup(sessionId: String) {
        val requestBean = CallRequestBean(sessionId = sessionId)
        viewModelScope.launch {
            mRepository.requestChatHangup(
                RequestUtil.getPostBody(requestBean),
                callback = RequestCallback(
                    onSuccess = {
                    },
                    onFailed = {
                    }
                ))
        }
    }

    /**
     * 获取卡片列表
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
     * 登录环信
     */
    fun loginIM() {
        EMClientHelper.loginEM(SpHelper.getUserInfo()!!.userId, ldIM)
    }

    /**
     * 获取会话列表
     */
    fun requestConversations(): ArrayList<EMConversation> {
        return EMClientHelper.getConversationList();
    }
}