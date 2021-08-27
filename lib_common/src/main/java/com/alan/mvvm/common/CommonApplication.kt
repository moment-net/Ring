package com.alan.mvvm.common

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.core.content.ContextCompat
import com.alan.module.im.EMClientHelper
import com.alan.mvvm.base.BaseApplication
import com.alan.mvvm.base.app.ApplicationLifecycle
import com.alan.mvvm.base.app.InitDepend
import com.alan.mvvm.base.coil.CoilUtils
import com.alan.mvvm.base.constant.VersionStatus
import com.alan.mvvm.base.utils.ProcessUtils
import com.alan.mvvm.base.utils.SpUtils
import com.alan.mvvm.base.utils.network.NetworkStateClient
import com.alibaba.android.arouter.launcher.ARouter
import com.google.auto.service.AutoService
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.socks.library.KLog
import com.tencent.bugly.crashreport.CrashReport
import com.tencent.smtt.export.external.TbsCoreSettings
import com.tencent.smtt.sdk.QbSdk
import com.tencent.smtt.sdk.QbSdk.PreInitCallback

/**
 * 作者：alan
 * 时间：2021/7/28
 * 备注：项目相关的Application
 */
@AutoService(ApplicationLifecycle::class)
class CommonApplication : ApplicationLifecycle {

    companion object {
        // 全局CommonApplication
        @SuppressLint("StaticFieldLeak")
        lateinit var mCommonApplication: CommonApplication

        init {
            SmartRefreshLayout.setDefaultRefreshInitializer { _, layout ->
                layout.apply {
                    setEnableOverScrollDrag(true)
                    setEnableScrollContentWhenLoaded(false)
                    setEnableAutoLoadMore(true)
                    setEnableOverScrollBounce(true)
                    setFooterHeight(60f)
                }
            }
            SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
                layout.apply {
                    setPrimaryColorsId(R.color.white, R.color._3A3A3A)
                }
                MaterialHeader(context).setColorSchemeColors(
                    ContextCompat.getColor(context, R.color._3A3A3A)
                )
            }
            SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
                ClassicsFooter(context).setFinishDuration(0)
            }
        }
    }

    /**
     * 同[Application.attachBaseContext]
     * @param context Context
     */
    override fun onAttachBaseContext(context: Context) {
        mCommonApplication = this
    }

    /**
     * 同[Application.onCreate]
     * @param application Application
     */
    override fun onCreate(application: Application) {}

    /**
     * 同[Application.onTerminate]
     * @param application Application
     */
    override fun onTerminate(application: Application) {}

    /**
     * 需要立即进行初始化的放在这里进行并行初始化
     * 需要必须在主线程初始化的放在[InitDepend.mainThreadDepends],反之放在[InitDepend.workerThreadDepends]
     * @return InitDepend 初始化方法集合
     */
    override fun initByFrontDesk(): InitDepend {
        val worker = mutableListOf<() -> String>()
        val main = mutableListOf<() -> String>()
        // 以下只需要在主进程当中初始化 按需要调整
        if (ProcessUtils.isMainProcess(BaseApplication.mContext)) {
            worker.add { initMMKV() }
            worker.add { initARouter() }
            worker.add { initKlog() }
            worker.add { initCoil() }
            main.add { initChatSdk() }
            main.add { initNetworkStateClient() }
        }
        worker.add { initTencentBugly() }
        return InitDepend(main, worker)
    }

    /**
     * 不需要立即初始化的放在这里进行后台初始化
     */
    override fun initByBackstage() {
        initX5WebViewCore()
    }

    /**
     * 初始化网络状态监听客户端
     * @return Unit
     */
    private fun initNetworkStateClient(): String {
        NetworkStateClient.register()
        return "NetworkStateClient -->> init complete"
    }

    /**
     * 腾讯TBS WebView X5 内核初始化
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
        QbSdk.initX5Environment(BaseApplication.mContext, object : PreInitCallback {

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
     * 腾讯 MMKV 初始化
     */
    private fun initMMKV(): String {
        val result = SpUtils.initMMKV(BaseApplication.mContext)
        return "MMKV -->> $result"
    }

    /**
     * 阿里路由 ARouter 初始化
     */
    private fun initARouter(): String {
        // 测试环境下打开ARouter的日志和调试模式 正式环境需要关闭
        if (BuildConfig.VERSION_TYPE != VersionStatus.RELEASE) {
            ARouter.openLog()     // 打印日志
            ARouter.openDebug()   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(BaseApplication.mApplication)
        return "ARouter -->> init complete"
    }

    /**
     * 初始化 腾讯Bugly
     * 测试环境应该与正式环境的日志收集渠道分隔开
     * 目前有两个渠道 测试版本/正式版本
     */
    private fun initTencentBugly(): String {
        // 第三个参数为SDK调试模式开关
        CrashReport.initCrashReport(
            BaseApplication.mContext,
            BaseApplication.mContext.getString(R.string.BUGLY_APP_ID),
            BuildConfig.VERSION_TYPE != VersionStatus.RELEASE
        )
        return "Bugly -->> init complete"
    }

    /**
     * 阿里路由 ARouter 初始化
     */
    private fun initKlog(): String {
        // 测试环境下打开Klog的日志和调试模式 正式环境需要关闭
        KLog.init(BuildConfig.VERSION_TYPE != VersionStatus.RELEASE)
        return "KLog -->> init complete"
    }

    /**
     * Coil 初始化
     */
    private fun initCoil(): String {
        // 初始化
        CoilUtils.initCoil()
        return "CoilUtils -->> init complete"
    }

    /**
     * 环信 初始化
     */
    private fun initChatSdk(): String {
        // 初始化
        EMClientHelper.init(BaseApplication.mContext)
        return "ChatSDK -->> init complete"
    }
}