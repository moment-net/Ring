package com.alan.mvvm.common.report


import android.app.Application
import android.content.Context
import android.os.Build
import android.util.Log
import com.alan.mvvm.base.constant.VersionStatus
import com.alan.mvvm.base.utils.DeviceUtil
import com.alan.mvvm.common.BuildConfig
import com.amplitude.api.Amplitude
import com.amplitude.api.AmplitudeClient
import com.socks.library.KLog
import org.json.JSONException
import org.json.JSONObject


/**
 * 作者：alan
 * 时间：2021/10/20
 * 备注：数据上报工具类
 */
class AmplitudeUtil private constructor() {
    lateinit var amplitude: AmplitudeClient
    lateinit var context: Context

    companion object {
        //通过@JvmStatic注解，使得在Java中调用instance直接是像调用静态函数一样，
        //AmplitudeUtil.getInstance(),如果不加注解，在Java中必须这样调用: KLazilyDCLSingleton.Companion.getInstance().
        //使用lazy属性代理，并指定LazyThreadSafetyMode为SYNCHRONIZED模式保证线程安全
        @JvmStatic
        val instance: AmplitudeUtil by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            AmplitudeUtil()
        }
    }

    /**
     * 初始化
     */
    fun init(application: Application) {
        context = application
        val instanceName = if ((BuildConfig.VERSION_TYPE != VersionStatus.RELEASE)) {
            //测试
            "RING_DEBUG"
        } else {
            //正式
            "RING_RELEASE"
        }
        amplitude = Amplitude.getInstance(instanceName)
            .initialize(application, ReportConstant.AMPLITUDE_APPID)
            .enableForegroundTracking(application)
            .enableCoppaControl()
            .enableLogging(true)
            .setLogLevel(Log.VERBOSE)
            .setOptOut(false)
            .setServerUrl("https://api2.amplitude.com/")
    }

    /**
     * 设置用户ID
     */
    fun setUserId(userId: String?) {
        amplitude.setUserId(userId)
    }


    /**
     * 上报
     */
    fun logEvent(event: String?, vararg args: String?) {
        if (args.size == 0) return
        val jsonObject = JSONObject()
        try {
            var i = 0
            while (i < args.size - 1) {
                //偶数为key   奇数为value
                jsonObject.put(args[i]!!, args[i + 1])
                i += 2
            }
            jsonObject.put(ReportConstant.KEY_DEVICE_ID, DeviceUtil.getDeviceUuid(context))
            jsonObject.put(ReportConstant.KEY_DEVICE_MODEL, Build.MODEL)
            jsonObject.put(ReportConstant.KEY_SYSTEM_VERSION, Build.VERSION.SDK_INT)
            jsonObject.put(ReportConstant.KEY_APP_VERSION, DeviceUtil.getApkVersionName(context))
            jsonObject.put(ReportConstant.KEY_IMEI, DeviceUtil.getAndroidIMEI(context))
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        KLog.e("log", "上报数据：${event}===${jsonObject.toString()}")
        amplitude.logEvent(event, jsonObject)
    }
}