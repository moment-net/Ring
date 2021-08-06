package com.alan.mvvm.common.dialog

import android.content.Context
import com.alan.mvvm.common.R
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.interfaces.OnCancelListener
import com.lxj.xpopup.interfaces.OnConfirmListener

/**
 * 作者：alan
 * 时间：2021/8/2
 * 备注：
 */
object DialogHelper {

    fun showBaseDialog(
        context: Context,
        title: String,
        content: String,
        func: () -> Unit
    ): BasePopupView {
        return XPopup.Builder(context).asConfirm(title, content, OnConfirmListener(func)).show()
    }

    /**
     * 版本下载
     */
    fun showVersionDialog(
        context: Context,
        title: String,
        content: String,
        func: () -> Unit
    ): BasePopupView {
        return XPopup.Builder(context)
            .asConfirm(title, content, "取消", "下载", OnConfirmListener(func), null, false).show()
    }

    /**
     * 自定义取消确认弹框
     */
    fun showMultipleDialog(
        context: Context,
        content: String,
        confirmBt: String,
        cancelBt: String,
        funConfirm: () -> Unit,
        funCancel: () -> Unit
    ): BasePopupView {
        return XPopup.Builder(context)
            .asConfirm(
                "",
                content,
                cancelBt,
                confirmBt,
                OnConfirmListener(funConfirm),
                OnCancelListener(funCancel),
                false,
                R.layout.layout_normal_multiple
            ).show()
    }

    /**
     * 加载框
     */
    fun showLoadingDialog(context: Context, title: String? = "加载中"): BasePopupView {
        return XPopup.Builder(context).asLoading(title, R.layout.layout_dialog).show()
    }

    /**
     * 显示无取消按钮弹框
     */
    fun showNoCancelDialog(
        context: Context,
        title: String,
        content: String,
        func: () -> Unit
    ): BasePopupView {
        return XPopup.Builder(context)
            .asConfirm(title, content, "取消", "确定", OnConfirmListener(func), null, true).show()
    }


}