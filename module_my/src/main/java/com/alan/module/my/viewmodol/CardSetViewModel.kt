package com.alan.module.my.viewmodol

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.alan.mvvm.base.http.callback.RequestCallback
import com.alan.mvvm.base.http.requestbean.CardTagRequestBean
import com.alan.mvvm.base.http.requestbean.Tag
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
class CardSetViewModel @Inject constructor(private val mRepository: CommonRepository) :
    BaseViewModel() {

    val ldData = MutableLiveData<Any>()

    /**
     * 查询详情
     */
    fun requestCardDetail(userId: String, name: String) {
        val map = hashMapOf<String, String>()
        map.put("userId", userId)
        map.put("keyword", name)
        viewModelScope.launch() {
            mRepository.requestCardDetail(
                map,
                callback = RequestCallback(
                    onSuccess = {
                        ldData.value = it.data!!
                    },
                    onFailed = {
                        toast(it.errorMessage)
                    },
                )
            )
        }
    }

    /**
     * 增加
     */
    fun requestAddCard(cardName: String, tags: ArrayList<Tag>) {
        val requestBean = CardTagRequestBean(cardName, tags)
        viewModelScope.launch() {
            mRepository.requestAddCard(
                RequestUtil.getPostBody(requestBean),
                callback = RequestCallback(
                    onSuccess = {
                        ldData.value = 1
                    },
                    onFailed = {
                        toast(it.errorMessage)
                    },
                )
            )
        }
    }

    /**
     * 编辑
     */
    fun requestEditCard(cardId: String, cardName: String, tags: ArrayList<Tag>) {
        val requestBean = CardTagRequestBean(cardName, tags)
        viewModelScope.launch() {
            mRepository.requestEditCard(
                cardId,
                RequestUtil.getPostBody(requestBean),
                callback = RequestCallback(
                    onSuccess = {
                        ldData.value = 2
                    },
                    onFailed = {
                        toast(it.errorMessage)
                    },
                )
            )
        }
    }

    /**
     * 删除
     */
    fun requestDeleteCard(cardId: String) {
        viewModelScope.launch() {
            mRepository.requestDeleteCard(
                cardId,
                callback = RequestCallback(
                    onSuccess = {
                        ldData.value = 3
                    },
                    onFailed = {
                        toast(it.errorMessage)
                    },
                )
            )
        }
    }
}