package com.alan.mvvm.base.ktx

import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow

/**
 * 作者：alan
 * 时间：2021/7/28
 * 备注：PopupWindow相关扩展
 */

/**
 * 测量view宽高
 */
fun PopupWindow.makeDropDownMeasureSpec(measureSpec: Int): Int {
    val mode =
        if (measureSpec == ViewGroup.LayoutParams.WRAP_CONTENT) View.MeasureSpec.UNSPECIFIED else View.MeasureSpec.EXACTLY
    return View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(measureSpec), mode)
}