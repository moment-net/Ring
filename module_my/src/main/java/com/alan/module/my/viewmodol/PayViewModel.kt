package com.alan.module.my.viewmodol

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.alan.mvvm.base.http.callback.RequestCallback
import com.alan.mvvm.base.http.requestbean.OrderIdRequestBean
import com.alan.mvvm.base.http.requestbean.OrderRequestBean
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
class PayViewModel @Inject constructor(private val mRepository: CommonRepository) :
    BaseViewModel() {

    val ldSuccess = MutableLiveData<Any>()


    /**
     * 购买钻石下单
     */
    fun requestOrder(goodsId: String) {
        var requestBean = OrderRequestBean(goodsId, 1);
        viewModelScope.launch {
            mRepository.requestOrder(
                RequestUtil.getPostBody(requestBean), callback = RequestCallback(
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
     * 购买钻石下单-微信支付
     */
    fun requestPayWX(orderId: String) {
        var requestBean = OrderIdRequestBean(orderId);
        viewModelScope.launch {
            mRepository.requestPayWX(
                RequestUtil.getPostBody(requestBean), callback = RequestCallback(
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
     * 购买钻石下单-支付宝支付
     */
    fun requestPayZFB(orderId: String) {
        var requestBean = OrderIdRequestBean(orderId);
        viewModelScope.launch {
            mRepository.requestPayZFB(
                RequestUtil.getPostBody(requestBean), callback = RequestCallback(
                    onSuccess = {
                        ldSuccess.value = it.data!!
                    },
                    onFailed = {
                        toast(it.errorMessage)
                    }
                ))
        }
    }

}