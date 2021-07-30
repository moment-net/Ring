package com.alan.mvvm.base.ktx

import android.text.InputFilter
import android.widget.EditText

/**
 * 作者：alan
 * 时间：2021/7/28
 * 备注：EditText相关扩展方法
 */

/**
 * 过滤掉空格和回车
 */
fun EditText.filterBlankAndCarriageReturn() {
    this.filters =
        arrayOf(InputFilter { source, _, _, _, _, _ -> if (source == " " || source == "\n") "" else null })
}