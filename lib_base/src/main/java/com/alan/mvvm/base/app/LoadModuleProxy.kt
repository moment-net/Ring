package com.alan.mvvm.base.app

import android.app.Application
import android.content.Context
import android.util.Log
import java.util.*

/**
 * 作者：alan
 * 时间：2021/7/28
 * 备注：加载组件代理类
 * 组件初始化的工作将由该代理类代理实现
 */
class LoadModuleProxy : ApplicationLifecycle {

    private var mLoader: ServiceLoader<ApplicationLifecycle> =
        ServiceLoader.load(ApplicationLifecycle::class.java)

    /**
     * 同[Application.attachBaseContext]
     * @param context Context
     */
    override fun onAttachBaseContext(context: Context) {
        mLoader.forEach {
            Log.d("ApplicationInit", it.toString())
            it.onAttachBaseContext(context)
        }
    }

    /**
     * 同[Application.onCreate]
     * @param application Application
     */
    override fun onCreate(application: Application) {
        mLoader.forEach { it.onCreate(application) }
    }

    /**
     * 同[Application.onTerminate]
     * @param application Application
     */
    override fun onTerminate(application: Application) {
        mLoader.forEach { it.onTerminate(application) }
    }

    /**
     * 需要立即进行初始化的放在这里进行并行初始化
     * 需要必须在主线程初始化的放在[InitDepend.mainThreadDepends],反之放在[InitDepend.workerThreadDepends]
     * @return InitDepend 初始化方法集合
     */
    override fun initByFrontDesk(): InitDepend {
        val mainThreadDepends: MutableList<() -> String> = mutableListOf()
        val workerThreadDepends: MutableList<() -> String> = mutableListOf()
        mLoader.forEach {
            mainThreadDepends.addAll(it.initByFrontDesk().mainThreadDepends)
            workerThreadDepends.addAll(it.initByFrontDesk().workerThreadDepends)
        }
        return InitDepend(mainThreadDepends, workerThreadDepends)
    }

    /**
     * 不需要立即初始化的放在这里进行后台初始化
     */
    override fun initByBackstage() {
        mLoader.forEach { it.initByBackstage() }
    }
}