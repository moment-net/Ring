package com.alan.mvvm.common.event

/**
 * 作者：alan
 * 时间：2021/9/30
 * 备注：语音通话时间到了或取消了，隐藏通话弹框
 */
data class CallDismissEvent(
    var type: String = ""
)
