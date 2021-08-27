package com.alan.mvvm.common.ui

import android.os.Build
import android.view.View
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.alan.mvvm.base.mvvm.v.BaseFrameActivity
import com.alan.mvvm.base.mvvm.vm.BaseViewModel
import com.alan.mvvm.base.utils.ActivityStackManager
import com.alan.mvvm.base.utils.AndroidBugFixUtils
import com.alan.mvvm.base.utils.StateLayoutEnum
import com.alan.mvvm.common.R
import com.alan.mvvm.common.dialog.DialogHelper
import com.jaeger.library.StatusBarUtil
import com.lxj.xpopup.core.BasePopupView
import com.socks.library.KLog

/**
 * 作者：alan
 * 时间：2021/7/28
 * 备注：Activity基类
 */
abstract class BaseActivity<VB : ViewBinding, VM : BaseViewModel> : BaseFrameActivity<VB, VM>() {
    lateinit var loadingDialog: BasePopupView

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

        mViewModel.ld_state.observe(this) {
            when {
                it == StateLayoutEnum.LOADING -> {
                    showDialog()
                }
                it == StateLayoutEnum.HIDE -> {
                    dismissDialog()
                }
                it == StateLayoutEnum.ERROR -> {
                }
                it == StateLayoutEnum.NO_DATA -> {
                }
            }
        }
    }

    fun showDialog() {
        loadingDialog = DialogHelper.showLoadingDialog(this)
    }

    fun dismissDialog() {
        if (loadingDialog != null) {
            loadingDialog.dismiss()
        }
    }


    override fun onResume() {
        super.onResume()
        KLog.d("ActivityLifecycle", "ActivityStack: ${ActivityStackManager.activityStack}")
    }

    override fun onDestroy() {
        super.onDestroy()
        // 解决某些特定机型会触发的Android本身的Bug
        AndroidBugFixUtils().fixSoftInputLeaks(this)
    }
}