package com.alan.module.main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cn.jpush.android.api.JPushInterface
import com.alan.mvvm.base.BaseApplication
import com.alan.mvvm.base.http.callback.RequestCallback
import com.alan.mvvm.base.http.requestbean.DeviceRegisterBean
import com.alan.mvvm.base.http.responsebean.UserInfoBean
import com.alan.mvvm.base.mvvm.vm.BaseViewModel
import com.alan.mvvm.base.utils.DeviceUtil
import com.alan.mvvm.base.utils.RequestUtil
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
class SplashViewModel @Inject constructor(private val mRepository: CommonRepository) :
    BaseViewModel() {

    val ldResult = MutableLiveData<Boolean>()
    val ldIM = MutableLiveData<BaseHttpException>()

    /**
     * 重新登录
     */
    fun requestAutoLogin() {
        viewModelScope.launch {
            mRepository.requestAutoLogin(
                callback = RequestCallback(
                    onSuccess = {
                        ldResult.value = true
                        SpHelper.updateUserInfo(it.data)
                        loginIM(it.data.user!!)
                        requestDevicesRegister()
                    },
                    onFailed = {
                        ldResult.value = false
                        SpHelper.clearUserInfo()
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
            DeviceUtil.getImei(BaseApplication.mContext)
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