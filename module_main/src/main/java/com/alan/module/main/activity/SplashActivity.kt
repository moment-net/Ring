package com.alan.module.main.activity

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.alan.module.main.databinding.ActivitySplashBinding
import com.alan.module.main.dialog.PrivacyFragmentDialog
import com.alan.module.main.viewmodel.SplashViewModel
import com.alan.mvvm.base.BaseApplication
import com.alan.mvvm.base.utils.ActivityStackManager
import com.alan.mvvm.base.utils.EventBusRegister
import com.alan.mvvm.base.utils.jumpARoute
import com.alan.mvvm.common.constant.Constants
import com.alan.mvvm.common.constant.RouteUrl
import com.alan.mvvm.common.event.TokenEvent
import com.alan.mvvm.common.helper.SpHelper
import com.alan.mvvm.common.report.AmplitudeUtil
import com.alan.mvvm.common.ui.BaseActivity
import com.geetest.onelogin.OneLoginHelper
import com.permissionx.guolindev.PermissionX
import com.socks.library.KLog
import com.tencent.smtt.export.external.TbsCoreSettings
import com.tencent.smtt.sdk.QbSdk
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * 作者：alan
 * 时间：2021/7/30
 * 备注：启动页
 */
@EventBusRegister
@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding, SplashViewModel>() {
    val REQUESTED_PERMISSIONS = mutableListOf<String>(
        Manifest.permission.READ_PHONE_STATE,
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
        if (!isTaskRoot()) {
            finish()
            return
        }


        initOneLogin()
        initAmplitudeSdk()
        initX5WebViewCore()

        if (SpHelper.isAgree()) {
            requestPermisssion()
        } else {
            showPrivacyDialog()
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
     * 初始化
     */
    override fun initRequestData() {

    }

    fun showPrivacyDialog() {
        val dialog = PrivacyFragmentDialog.newInstance()
        dialog.show(supportFragmentManager)
        dialog.listener = object : PrivacyFragmentDialog.OnClickAgreeListener {
            override fun onClick() {
                SpHelper.setAgree(true)
                requestPermisssion()
            }
        }
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
                finish()
            }
        }
    }


    //Token失效
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun handleToken(event: TokenEvent) {
        SpHelper.clearUserInfo()
        ActivityStackManager.finishAllActivity()
        val bundle = Bundle()
        jumpARoute(RouteUrl.MainModule.ACTIVITY_MAIN_LOGIN, bundle, Intent.FLAG_ACTIVITY_NEW_TASK)
    }


    //--------------------初始化相关--------------------------------------------------------------------------
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


    /**
     * 腾讯TBS WebView X5 内核初始化
     * 会获取设备ID
     */
    private fun initX5WebViewCore() {
        // dex2oat优化方案
        val map = HashMap<String, Any>()
        map[TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER] = true
        map[TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE] = true
        QbSdk.initTbsSettings(map)

        // 允许使用非wifi网络进行下载
        QbSdk.setDownloadWithoutWifi(true)

        // 初始化
        QbSdk.initX5Environment(BaseApplication.mContext, object : QbSdk.PreInitCallback {

            override fun onCoreInitFinished() {
                KLog.d("ApplicationInit", " TBS X5 init finished")
            }

            override fun onViewInitFinished(p0: Boolean) {
                // 初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核
                KLog.d("ApplicationInit", " TBS X5 init is $p0")
            }
        })
    }

    /**
     * 数据上报 初始化
     * 会获取设备ID
     */
    private fun initAmplitudeSdk(): String {
        // 初始化
        AmplitudeUtil.instance.init(BaseApplication.mApplication)
        return "AmplitudeSDK -->> init complete"
    }
}