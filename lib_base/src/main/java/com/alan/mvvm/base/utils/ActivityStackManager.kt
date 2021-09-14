package com.alan.mvvm.base.utils

import android.app.Activity
import java.util.*

/**
 * 作者：alan
 * 时间：2021/7/28
 * 备注：Activity 栈管理类
 */
object ActivityStackManager {

    // 管理栈所有Activity
    val activityStack by lazy { Stack<Activity>() }

    //处于前台的Activity
    val frontStack by lazy { Stack<Activity>() }

    /**
     * 添加 Activity 到管理栈
     * @param activity Activity
     */
    fun addActivityToStack(activity: Activity) {
        activityStack.push(activity)
    }

    /**
     * 弹出栈内指定Activity 不finish
     * @param activity Activity
     */
    fun popActivityToStack(activity: Activity) {
        if (!activityStack.empty()) {
            activityStack.forEach {
                if (it == activity) {
                    activityStack.remove(activity)
                    return
                }
            }
        }
    }

    /**
     * 添加 Activity 到管理栈
     * @param activity Activity
     */
    fun addResumedToStack(activity: Activity) {
        frontStack.push(activity)
    }

    /**
     * 弹出栈内指定Activity 不finish
     * @param activity Activity
     */
    fun popPausedToStack(activity: Activity) {
        if (!frontStack.empty()) {
            frontStack.forEach {
                if (it == activity) {
                    frontStack.remove(activity)
                    return
                }
            }
        }
    }

    /**
     * 是否是前台
     */
    fun isFront(): Boolean {
        return frontStack.size > 0
    }

    /**
     * 返回到上一个 Activity 并结束当前 Activity
     */
    fun backToPreviousActivity() {
        if (!activityStack.empty()) {
            val activity = activityStack.pop()
            if (!activity.isFinishing) activity.finish()
        }
    }

    /**
     * 根据类名 判断是否是当前的 Activity
     * @param cls Class<*> 类名
     * @return Boolean
     */
    fun isCurrentActivity(cls: Class<*>): Boolean {
        val currentActivity = getCurrentActivity()
        return if (currentActivity != null) currentActivity.javaClass == cls else false
    }

    /**
     * 获取当前的 Activity
     */
    fun getCurrentActivity(): Activity? =
        if (!activityStack.empty()) activityStack.lastElement() else null

    /**
     * 结束一个栈内指定类名的 Activity
     * @param cls Class<*>
     */
    fun finishActivity(cls: Class<*>) {
        activityStack.forEach {
            if (it.javaClass == cls) {
                if (!it.isFinishing) it.finish()
                return
            }
        }
    }

    /**
     * 弹出其他 Activity
     */
    fun popOtherActivity() {
        val activityList = activityStack.toList()
        getCurrentActivity()?.run {
            activityList.forEach { activity ->
                if (this != activity) {
                    activityStack.remove(activity)
                    activity.finish()
                }
            }
        }
    }

    /**
     * 返回到指定 Activity
     */
    fun backToSpecifyActivity(activityClass: Class<*>) {
        val activityList = activityStack.toList()
        // 获取栈最上面的Activity
        val lastElement = activityStack.lastElement()
        activityList.forEach {
            // 如果栈内存在该Activity就进行下一步操作
            if (it.javaClass == activityClass) {
                // 判断最上面的Activity是不是指定的Activity 不是就finish
                if (lastElement.javaClass == activityClass) {
                    return
                } else {
                    activityStack.remove(lastElement)
                    lastElement.finish()
                }
            }
        }
    }

    /**
     * 结束指定的Activity
     */
    fun finishActivity(activity: Activity?) {
        if (activity != null) {
            if (!activity.isFinishing) {
                activity.finish()
            }
        }
    }

    /**
     * 结束所有Activity
     */
    fun finishAllActivity() {
        for (activity in activityStack!!) {
            finishActivity(activity)
        }
    }

    /**
     * 退出应用程序
     */
    fun exitApp() {
        try {
            finishAllActivity()
            // 杀死该应用进程
            android.os.Process.killProcess(android.os.Process.myPid())
//            调用 System.exit(n) 实际上等效于调用：
//            Runtime.getRuntime().exit(n)
//            finish()是Activity的类方法，仅仅针对Activity，当调用finish()时，只是将活动推向后台，并没有立即释放内存，活动的资源并没有被清理；当调用System.exit(0)时，退出当前Activity并释放资源（内存），但是该方法不可以结束整个App如有多个Activty或者有其他组件service等不会结束。
//            其实android的机制决定了用户无法完全退出应用，当你的application最长时间没有被用过的时候，android自身会决定将application关闭了。
            //System.exit(0);
        } catch (e: Exception) {
            activityStack!!.clear()
            e.printStackTrace()
        }
    }
}