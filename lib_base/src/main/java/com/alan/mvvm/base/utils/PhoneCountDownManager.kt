package com.alan.mvvm.base.utils

/**
 * 作者：alan
 * 时间：2021/8/24
 * 备注：倒计时处理
 */
class PhoneCountDownManager {
    //记录倒计时，退出当前页再进入时继续倒计时
    var countDownTime: Long = 0
    val isNeedRegetCode: Boolean
        get() {
            val time = ((System.currentTimeMillis() - countDownTime) / 1000).toInt()
            return time > 59
        }

    companion object {
        private var countDownManager: PhoneCountDownManager? = null
        val instance: PhoneCountDownManager?
            get() {
                if (countDownManager == null) {
                    synchronized(PhoneCountDownManager::class.java) {
                        if (countDownManager == null) {
                            countDownManager = PhoneCountDownManager()
                        }
                    }
                }
                return countDownManager
            }
    }
}