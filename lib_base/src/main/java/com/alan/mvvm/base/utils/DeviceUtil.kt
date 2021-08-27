package com.alan.mvvm.base.utils

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.TextUtils
import androidx.core.app.ActivityCompat
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.net.NetworkInterface
import java.util.*

object DeviceUtil {


    fun getApkVersionCode(context: Context): Int {
        var version = 0
        try {
            val pm = context.packageManager
            val packageInfo = pm.getPackageInfo(context.packageName, 0)
            version = packageInfo.versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        } finally {
            return version
        }
    }

    fun getApkVersionName(context: Context): String? {
        var versionName = ""
        try {
            val pm = context.packageManager
            val packageInfo = pm.getPackageInfo(context.packageName, 0)
            versionName = packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        } finally {
            return versionName
        }
    }

    /**
     * 获取设备型号
     *
     * 如MI2SC
     *
     * @return 设备型号
     */
    fun getModel(): String? {
        var model = Build.MODEL
        model = model?.trim { it <= ' ' }?.replace("\\s*".toRegex(), "") ?: ""
        return model
    }


    /**
     * 获取PackageName
     *
     * @param context context
     * @return 当前应用的版本名称
     */
    fun getPackageName(context: Context): String? {
        return try {
            val packageManager = context.packageManager
            val packageInfo = packageManager.getPackageInfo(
                context.packageName, 0
            )
            packageInfo.packageName
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 获取渠道名
     *
     * @param context
     * @return
     */
    fun getChannel(context: Context): String? {
        try {
            val pm = context.packageManager
            val appInfo = pm.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
            return appInfo.metaData.getString("JPUSH_CHANNEL")
        } catch (ignored: PackageManager.NameNotFoundException) {
        }
        return ""
    }


    /**
     * 获取设备id
     */
    @SuppressLint("MissingPermission")
    fun getDeviceUuid(context: Context): String? {
        var deviceId: String? = null
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            deviceId = try {
                val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.READ_PHONE_STATE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return ""
                }
                tm.deviceId
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                return ""
            }
        } else {
            try {
                deviceId =
                    Settings.System.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
                if (TextUtils.isEmpty(deviceId)) {
                    return ""
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                if (TextUtils.isEmpty(deviceId)) {
                    return ""
                }
            }
        }
        return deviceId
    }


    /**
     * 获取Android IMEI号
     *
     * @param context
     * @return
     */
    fun getAndroidIMEI(context: Context): String? {
        var deviceId = getImei(context)
        //android 10以上已经获取不了imei了 用 android id代替
        if (TextUtils.isEmpty(deviceId)) {
            deviceId = Settings.System.getString(
                context.contentResolver, Settings.Secure.ANDROID_ID
            )
        }
        return deviceId
    }

    /**
     * 获取Imei 主义需要获取 Manifest.permission.READ_PHONE_STATE 权限
     *
     * @param context
     * @return
     */
    fun getImei(context: Context): String? {
        var imei = ""
        try {
            val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            imei = tm.deviceId
        } catch (e: java.lang.Exception) {
        }
        return imei
    }

    /**
     * 获取厂商品牌
     */
    fun getBrand(): String? {
        return Build.BRAND
    }


    /**
     * 获取IMEI
     *
     * @param ctx
     * @return
     */
    fun getIMEI(ctx: Context): String? {
        var imei: String? = ""
        imei = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {  //4.0以下 直接获取
            getImeiOrMeid(ctx)
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) { //5。0，6。0系统
            val imeiMaps = getImeiAndMeid(ctx)
            getTransform(imeiMaps)
        } else {
            val imeiMaps = getIMEIforO(ctx)
            getTransform(imeiMaps)
        }
        return imei
    }


    /**
     * 将IMEI拼接起来
     *
     * @param imeiMaps
     * @return
     */
    private fun getTransform(imeiMaps: Map<*, *>?): String? {
        var imei: String? = ""
        if (imeiMaps != null) {
            imei = imeiMaps["imei1"] as String?
            //            String imei1 = (String) imeiMaps.get("imei1");
//            if (TextUtils.isEmpty(imei1)) {
//                return imei;
//            }
//            String imei2 = (String) imeiMaps.get("imei2");
//            if (imei2 != null) {
//                if (imei1.trim().length() == 15 && imei2.trim().length() == 15) {
//                    //如果两个位数都是15。说明都是有效IMEI。根据从大到小排列
//                    long i1 = Long.parseLong(imei1.trim());
//                    long i2 = Long.parseLong(imei2.trim());
//                    if (i1 > i2) {
//                        imei = imei2 + ";" + imei1;
//                    } else {
//                        imei = imei1 + ";" + imei2;
//                    }
//
//                } else {  //
//                    if (imei1.trim().length() == 15) {
//                        //如果只有imei1是有效的
//                        imei = imei1;
//                    } else if (imei2.trim().length() == 15) {
//                        //如果只有imei2是有效的
//                        imei = imei2;
//                    } else {
//                        //如果都无效那么都为meid。只取一个就可以
//                        imei = imei1;
//                    }
//                }
//            } else {
//                imei = imei1;
//            }
        }
        return imei
    }


    /**
     * 系统4.0的时候
     * 获取手机IMEI 或者MEID
     *
     * @return 手机IMEI
     */
    @SuppressLint("MissingPermission")
    fun getImeiOrMeid(context: Context): String? {
        return if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ""
        } else try {
            val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            tm.deviceId
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            ""
        }
    }


    /**
     * 5.0统一使用这个获取IMEI IMEI2 MEID
     *
     * @param context
     * @return
     */
    @SuppressLint("MissingPermission")
    @TargetApi(Build.VERSION_CODES.M)
    fun getImeiAndMeid(context: Context): Map<*, *> {
        val map: MutableMap<String, String> = HashMap()
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            //如果没有获取权限，则返回空数据
            return map
        }
        val mTelephonyManager =
            context.getSystemService(Activity.TELEPHONY_SERVICE) as TelephonyManager
        var clazz: Class<*>? = null
        var method: Method? = null //(int slotId)
        try {
            clazz = Class.forName("android.os.SystemProperties")
            method = clazz.getMethod("get", String::class.java, String::class.java)
            val gsm = method.invoke(null, "ril.gsm.imei", "") as String
            val meid = method.invoke(null, "ril.cdma.meid", "") as String
            map["meid"] = meid
            if (!TextUtils.isEmpty(gsm)) {
                //the value of gsm like:xxxxxx,xxxxxx
                val imeiArray = gsm.split(",").toTypedArray()
                if (imeiArray != null && imeiArray.size > 0) {
                    map["imei1"] = imeiArray[0]
                    if (imeiArray.size > 1) {
                        map["imei2"] = imeiArray[1]
                    } else {
                        map["imei2"] = mTelephonyManager.getDeviceId(1)
                    }
                } else {
                    map["imei1"] = mTelephonyManager.getDeviceId(0)
                    map["imei2"] = mTelephonyManager.getDeviceId(1)
                }
            } else {
                map["imei1"] = mTelephonyManager.getDeviceId(0)
                map["imei2"] = mTelephonyManager.getDeviceId(1)
            }
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
        return map
    }

    /**
     * 6.0统一使用这个获取IMEI IMEI2 MEID
     *
     * @param context
     * @return
     */
    @SuppressLint("MissingPermission")
    @TargetApi(Build.VERSION_CODES.O)
    fun getIMEIforO(context: Context): Map<*, *> {
        val map: MutableMap<String, String> = HashMap()
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            //如果没有获取权限，则返回空数据
            return map
        }
        try {
            val imei1 = tm.getImei(0)
            val imei2 = tm.getImei(1)
            if (TextUtils.isEmpty(imei1) && TextUtils.isEmpty(imei2)) {
                map["imei1"] = tm.meid //如果CDMA制式手机返回MEID
            } else {
                map["imei1"] = imei1
                map["imei2"] = imei2
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return map
    }


    //获取当前设备ID
    fun getAndroidID(context: Context): String? {
        var androidId: String? = null
        try {
            androidId =
                Settings.System.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
            if (TextUtils.isEmpty(androidId)) {
                return ""
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            if (TextUtils.isEmpty(androidId)) {
                return ""
            }
        }
        return androidId
    }


    /**
     * 获取mac地址
     */
    fun getMacAddress(): String? {
        try {
            val all: List<NetworkInterface> =
                Collections.list(NetworkInterface.getNetworkInterfaces())
            for (nif in all) {
                if (!nif.name.equals("wlan0", ignoreCase = true)) continue
                val macBytes = nif.hardwareAddress ?: return ""
                val res1 = StringBuilder()
                for (b in macBytes) {
                    res1.append(String.format("%02X:", b))
                }
                if (res1.length > 0) {
                    res1.deleteCharAt(res1.length - 1)
                }
                return res1.toString()
            }
        } catch (ex: java.lang.Exception) {
        }
        return "02:00:00:00:00:00"
    }
}