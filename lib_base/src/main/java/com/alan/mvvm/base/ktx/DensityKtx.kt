package com.alan.mvvm.base.ktx

import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.fragment.app.Fragment

/**
 * 作者：alan
 * 时间：2021/7/28
 * 备注：尺寸单位换算相关扩展属性
 */

/**
 * dp 转 px
 */
fun Context.dp2px(dpValue: Float): Int {
    val scale = resources.displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}

/**
 * px 转 dp
 */
fun Context.px2dp(pxValue: Float): Int {
    val scale = resources.displayMetrics.density
    return (pxValue / scale + 0.5f).toInt()
}

/**
 * sp 转 px
 */
fun Context.sp2px(spValue: Float): Int {
    val scale = resources.displayMetrics.scaledDensity
    return (spValue * scale + 0.5f).toInt()
}

/**
 * px 转 sp
 */
fun Context.px2sp(pxValue: Float): Int {
    val scale = resources.displayMetrics.scaledDensity
    return (pxValue / scale + 0.5f).toInt()
}

/**
 * dp 转 px
 */
fun Fragment.dp2px(dpValue: Float): Int {
    val scale = resources.displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}

/**
 * px 转 dp
 */
fun Fragment.px2dp(pxValue: Float): Int {
    val scale = resources.displayMetrics.density
    return (pxValue / scale + 0.5f).toInt()
}

/**
 * sp 转 px
 */
fun Fragment.sp2px(spValue: Float): Int {
    val scale = resources.displayMetrics.scaledDensity
    return (spValue * scale + 0.5f).toInt()
}

/**
 * px 转 sp
 */
fun Fragment.px2sp(pxValue: Float): Int {
    val scale = resources.displayMetrics.scaledDensity
    return (pxValue / scale + 0.5f).toInt()
}

fun Context.getScreenWidth(): Int {
    val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        return wm.getCurrentWindowMetrics().bounds.width()
    } else {
        val localDisplayMetrics = DisplayMetrics()
        wm.defaultDisplay.getMetrics(localDisplayMetrics)
        return localDisplayMetrics.widthPixels
    }
}

fun Context.getScreenHeight(): Int {
    val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        return wm.getCurrentWindowMetrics().bounds.height()
    } else {
        val localDisplayMetrics = DisplayMetrics()
        wm.defaultDisplay.getMetrics(localDisplayMetrics)
        return localDisplayMetrics.heightPixels
    }
}