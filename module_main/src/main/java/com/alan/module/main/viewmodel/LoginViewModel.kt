package com.alan.module.main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cn.jpush.android.api.JPushInterface
import com.alan.mvvm.base.BaseApplication
import com.alan.mvvm.base.http.callback.RequestCallback
import com.alan.mvvm.base.http.requestbean.DeviceRegisterBean
import com.alan.mvvm.base.http.requestbean.LoginRequestBean
import com.alan.mvvm.base.http.requestbean.LoginThirdRequestBean
import com.alan.mvvm.base.http.requestbean.PhoneCheckRequestBean
import com.alan.mvvm.base.http.responsebean.UserInfoBean
import com.alan.mvvm.base.mvvm.vm.BaseViewModel
import com.alan.mvvm.base.utils.DeviceUtil
import com.alan.mvvm.base.utils.RequestUtil
import com.alan.mvvm.base.utils.toast
import com.alan.mvvm.common.helper.SpHelper
import com.alan.mvvm.common.http.exception.BaseHttpException
import com.alan.mvvm.common.http.model.CommonRepository
import com.alan.mvvm.common.im.EMClientHelper
import com.alan.mvvm.common.report.DataPointUtil
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
class LoginViewModel @Inject constructor(private val mRepository: CommonRepository) :
    BaseViewModel() {

    val ldSuccess = MutableLiveData<Any>()
    val ldFailed = MutableLiveData<Int>()
    val ldIM = MutableLiveData<BaseHttpException>()

    fun requestCheckPhone(processId: String, token: String, authcode: String) {
        val requestBean = PhoneCheckRequestBean(processId, token, authcode)
        viewModelScope.launch {
            mRepository.requestCheckPhone(
                RequestUtil.getPostBody(requestBean), callback = RequestCallback(
                    onSuccess = {
                        DataPointUtil.reportRegister(SpHelper.getUserInfo()?.userId!!, 3)
                        ldSuccess.value = it.data!!
                    },
                    onFailed = {
                        toast(it.errorMessage)
                        ldFailed.value = 1
                    }
                ))
        }
    }


    /**
     * 登录
     */
    fun requestLogin(phone: String, installParam: String) {
        val requestBean = LoginRequestBean(phone, "123456", installParam, 1)
        viewModelScope.launch {
            mRepository.requestLogin(RequestUtil.getPostBody(requestBean),
                callback = RequestCallback(
                    onSuccess = {
                        ldSuccess.value = it.data!!
                    },
                    onFailed = {
                        toast(it.errorMessage)
                        ldFailed.value = 2
                    }
                ))
        }
    }


    /**
     * 微信登录
     */
    fun requestLoginWX(requestBean: LoginThirdRequestBean) {
        viewModelScope.launch {
            mRepository.requestLoginWX(RequestUtil.getPostBody(requestBean),
                callback = RequestCallback(
                    onSuccess = {
                        ldSuccess.value = it.data!!
                        DataPointUtil.reportRegister(SpHelper.getUserInfo()?.userId!!, 2)
                    },
                    onFailed = {
                        toast(it.errorMessage)
                        ldFailed.value = 3
                    }
                ))
        }
    }

    /**
     * 设备注册
     */
    fun requestDevicesRegister() {
        val requestBean = DeviceRegisterBean(
            "2",
            JPushInterface.getRegistrationID(BaseApplication.mContext),
            DeviceUtil.getBrand(),
            DeviceUtil.getImei(BaseApplication.mContext),
            DeviceUtil.getAndroidID(BaseApplication.mContext),
            DeviceUtil.getMacAddress()
        )

        viewModelScope.launch {
            mRepository.requestDevicesRegister(RequestUtil.getPostBody(requestBean),
                callback = RequestCallback(
                    onSuccess = {
                    },
                    onFailed = {
                    }
                ))
        }
    }

    /**
     * 登录环信
     */
    fun loginIM(userInfoBean: UserInfoBean) {
        EMClientHelper.loginEM(userInfoBean.userId, ldIM)
    }

}