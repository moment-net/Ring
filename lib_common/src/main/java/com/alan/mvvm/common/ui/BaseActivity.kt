package com.alan.mvvm.common.ui

import android.os.Build
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.alan.mvvm.base.mvvm.v.BaseFrameActivity
import com.alan.mvvm.base.mvvm.vm.BaseViewModel
import com.alan.mvvm.base.utils.ActivityStackManager
import com.alan.mvvm.base.utils.AndroidBugFixUtils
import com.alan.mvvm.common.R
import com.jaeger.library.StatusBarUtil

/**
 * 作者：alan
 * 时间：2021/7/28
 * 备注：Activity基类
 */
abstract class BaseActivity<VB : ViewBinding, VM : BaseViewModel> : BaseFrameActivity<VB, VM>() {

    /**
     * 设置状态栏
     * 子类需要自定义时重写该方法即可
     * @return Unit
     */
    override fun setStatusBar() {
        val themeColor = ContextCompat.getColor(this, R.color.transparent)
        StatusBarUtil.setColor(this, themeColor, 0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("ActivityLifecycle", "ActivityStack: ${ActivityStackManager.activityStack}")
    }

    override fun onDestroy() {
        super.onDestroy()
        // 解决某些特定机型会触发的Android本身的Bug
        AndroidBugFixUtils().fixSoftInputLeaks(this)
    }
}