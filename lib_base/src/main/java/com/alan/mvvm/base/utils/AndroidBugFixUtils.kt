package com.alan.mvvm.base.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.alan.mvvm.base.BaseApplication
import java.lang.reflect.Field

/**
 * 作者：alan
 * 时间：2021/7/28
 * 备注：解决 Android 自身的 Bug
 */
class AndroidBugFixUtils {

    /**
     * 解决 InputMethodManager 造成的内存泄露
     *
     * 使用方式：
     * ```
     * override fun onDestroy() {
     *     AndroidBugFixUtils().fixSoftInputLeaks(this)
     *     super.onDestroy()
     * }
     * ```
     */
    fun fixSoftInputLeaks(activity: Activity) {
        val imm =
            BaseApplication.mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val leakViews = arrayOf("mLastSrvView", "mCurRootView", "mServedView", "mNextServedView")
        for (leakView in leakViews) {
            try {
                val leakViewField: Field =
                    InputMethodManager::class.java.getDeclaredField(leakView) ?: continue
                if (!leakViewField.isAccessible) leakViewField.isAccessible = true
                val view: Any? = leakViewField.get(imm)
                if (view !is View) continue
                if (view.rootView == activity.window.decorView.rootView) {
                    leakViewField.set(imm, null)
                }
            } catch (t: Throwable) {
            }
        }
    }
}