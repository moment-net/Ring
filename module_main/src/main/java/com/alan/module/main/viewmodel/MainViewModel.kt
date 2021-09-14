package com.alan.module.main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.alan.mvvm.base.mvvm.vm.BaseViewModel
import com.alan.mvvm.common.helper.SpHelper
import com.alan.mvvm.common.http.exception.BaseHttpException
import com.alan.mvvm.common.http.model.CommonRepository
import com.alan.mvvm.common.im.EMClientHelper
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

    val data = MutableLiveData<String>()
    val ldIM = MutableLiveData<BaseHttpException>()

    /**
     * 模拟获取数据
     */
    fun getData() {
        viewModelScope.launch() {
//            mRepository.getData(callback = RequestCallback(
//                onStart = {},
//                onSuccess = {},
//                onFailed = {},
//                onFinally = {}
//            ))
        }
    }

    /**
     * 登录环信
     */
    fun loginIM() {
        EMClientHelper.loginEM(SpHelper.getUserInfo()!!.userId, ldIM)
    }
}