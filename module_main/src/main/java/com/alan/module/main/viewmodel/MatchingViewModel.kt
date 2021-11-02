package com.alan.module.main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.alan.mvvm.base.http.callback.RequestCallback
import com.alan.mvvm.base.http.requestbean.TagRequestBean
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
class MatchingViewModel @Inject constructor(private val mRepository: CommonRepository) :
    BaseViewModel() {

    val ldData = MutableLiveData<Any>()


    /**
     * 匹配信息
     */
    fun requestFastMatch(tag: String) {
        val requestBean = TagRequestBean(tag)
        viewModelScope.launch() {
            mRepository.requestFastMatch(
                RequestUtil.getPostBody(requestBean),
                callback = RequestCallback(
                    onSuccess = {
                        ldData.value = it.data!!
                    },
                    onFailed = {
                        ldData.value = false
                        toast(it.errorMessage)
                    },
                )
            )
        }
    }
}