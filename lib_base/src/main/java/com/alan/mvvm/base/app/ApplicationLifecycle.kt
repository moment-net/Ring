package com.alan.mvvm.base.app

import android.app.Application
import android.content.Context

/**
 * 作者：alan
 * 时间：2021/7/28
 * 备注：Application 生命周期 用于初始化各个组件
 */
interface ApplicationLifecycle {

    /**
     * 同[Application.attachBaseContext]
     * @param context Context
     */
    fun onAttachBaseContext(context: Context)

    /**
     * 同[Application.onCreate]
     * @param application Application
     */
    fun onCreate(application: Application)

    /**
     * 同[Application.onTerminate]
     * @param application Application
     */
    fun onTerminate(application: Application)

    /**
     * 需要立即进行初始化的放在这里进行并行初始化
     * 需要必须在主线程初始化的放在[InitDepend.mainThreadDepends],反之放在[InitDepend.workerThreadDepends]
     * @return InitDepend 初始化方法集合
     */
    fun initByFrontDesk(): InitDepend

    /**
     * 不需要立即初始化的放在这里进行后台初始化
     */
    fun initByBackstage()
}