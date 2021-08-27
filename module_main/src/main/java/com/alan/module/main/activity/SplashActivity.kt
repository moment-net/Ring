package com.alan.module.main.activity

import android.Manifest
import android.text.TextUtils
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.alan.module.main.databinding.ActivitySplashBinding
import com.alan.module.main.viewmodel.SplashViewModel
import com.alan.mvvm.base.utils.jumpARoute
import com.alan.mvvm.common.constant.Constants
import com.alan.mvvm.common.constant.RouteUrl
import com.alan.mvvm.common.helper.SpHelper
import com.alan.mvvm.common.ui.BaseActivity
import com.geetest.onelogin.OneLoginHelper
import com.permissionx.guolindev.PermissionX
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * 作者：alan
 * 时间：2021/7/30
 * 备注：启动页
 */

@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding, SplashViewModel>() {
    val REQUESTED_PERMISSIONS = mutableListOf<String>(
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.CAMERA
    )

    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<SplashViewModel>()

    /**
     * 初始化View
     */
    override fun ActivitySplashBinding.initView() {
        //home键退出，点击app再进时重启bug修改
        if (!isTaskRoot) {
            finish()
            return
        }
    }

    /**
     * 订阅数据
     */
    override fun initObserve() {
        mViewModel.ldResult.observe(this) {
            if (it) {
                intentHome()
            } else {
                finish()
                jumpARoute(RouteUrl.MainModule.ACTIVITY_MAIN_LOGIN)
            }
        }
    }

    /**
     * 获取数据
     */
    override fun initRequestData() {
        requestPermisssion()

        initOneLogin()
    }

    /**
     * 极验初始化
     */
    fun initOneLogin() {
        //极验初始化
        OneLoginHelper
            .with() //开启 SDK 日志打印功能
            .setLogEnable(true) //第一个参数为当前 Application 或 Activity 的 Context
            //第二个参数为所需要配置的 APPID, 注意与服务端保持一致
            .init(this, Constants.ONELOGIN_APP_ID)
            .register("", 5000)
    }

    fun requestPermisssion() {
        PermissionX.init(this).permissions(REQUESTED_PERMISSIONS)
            .request { allGranted, grantedList, deniedList ->
                //不给权限可以进
                if (!TextUtils.isEmpty(SpHelper.getToken())) {
                    mViewModel.requestAutoLogin()
                } else {
                    intentHome()
                }
            }
    }


    fun intentHome() {
        lifecycleScope.launch(Dispatchers.IO) {
            delay(3000)
            withContext(Dispatchers.Main) {
                if (SpHelper.isLogin()!!) {
                    jumpARoute(RouteUrl.MainModule.ACTIVITY_MAIN_MAIN);
                } else {
                    jumpARoute(RouteUrl.MainModule.ACTIVITY_MAIN_LOGIN);
                }
            }
        }
    }


}