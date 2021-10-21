package com.alan.module.main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cn.jpush.android.api.JPushInterface
import com.alan.mvvm.base.BaseApplication
import com.alan.mvvm.base.http.callback.RequestCallback
import com.alan.mvvm.base.http.requestbean.DeviceRegisterBean
import com.alan.mvvm.base.http.requestbean.LoginRequestBean
import com.alan.mvvm.base.http.requestbean.PhoneRequestBean
import com.alan.mvvm.base.http.responsebean.LoginBean
import com.alan.mvvm.base.http.responsebean.UserInfoBean
import com.alan.mvvm.base.mvvm.vm.BaseViewModel
import com.alan.mvvm.base.utils.DeviceUtil
import com.alan.mvvm.base.utils.RequestUtil
import com.alan.mvvm.base.utils.toast
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
class LoginCodeViewModel @Inject constructor(private val mRepository: CommonRepository) :
    BaseViewModel() {

    val ldCode = MutableLiveData<Boolean>()
    val ldBind = MutableLiveData<Boolean>()
    val ldLogin = MutableLiveData<LoginBean>()
    val ldFailed = MutableLiveData<Boolean>()
    val ldIM = MutableLiveData<BaseHttpException>()

    /**
     * 重新获取验证码
     */
    fun requestCode(phone: String) {
        val requestBean = PhoneRequestBean(phone)
        viewModelScope.launch {
            mRepository.requestCode(RequestUtil.getPostBody(requestBean),
                callback = RequestCallback(
                    onSuccess = {
                        ldCode.value = true
                    },
                    onFailed = {
                        toast(it.errorMessage)
                        ldCode.value = false
                    }
                ))
        }
    }

    /**
     * 登录
     */
    fun requestLogin(phone: String, code: String, installParam: String, isChecked: Int) {
        val requestBean = LoginRequestBean(phone, code, installParam, isChecked)
        viewModelScope.launch {
            mRepository.requestLogin(RequestUtil.getPostBody(requestBean),
                callback = RequestCallback(
                    onSuccess = {
                        DataPointUtil.reportCodeNext(true)
                        ldLogin.value = it.data!!
                    },
                    onFailed = {
                        DataPointUtil.reportCodeNext(false)
                        toast(it.errorMessage)
                        ldFailed.value = false
                    }
                ))
        }
    }


    /**
     * 绑定手机号
     */
    fun requestBindPhone(phone: String, code: String, isChecked: Int) {
        val requestBean = LoginRequestBean(phone, code, isChecked = isChecked)
        viewModelScope.launch {
            mRepository.requestBindPhone(RequestUtil.getPostBody(requestBean),
                callback = RequestCallback(
                    onSuccess = {
                        DataPointUtil.reportCodeNext(true)
                        ldBind.value = true
                    },
                    onFailed = {
                        DataPointUtil.reportCodeNext(false)
                        toast(it.errorMessage)
                        ldFailed.value = false
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
            mRepository.requestDevicesRegister(
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
     * 登录环信
     */
    fun loginIM(userInfoBean: UserInfoBean) {
        EMClientHelper.loginEM(userInfoBean.userId, ldIM)
    }
}