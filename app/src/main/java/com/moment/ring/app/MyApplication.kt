package com.moment.ring.app

import com.alan.mvvm.base.BaseApplication
import dagger.hilt.android.HiltAndroidApp
import org.greenrobot.eventbus.EventBus

/**
 * 作者：alan
 * 时间：2021/7/28
 * 备注：App壳
 */
@HiltAndroidApp
class MyApplication : BaseApplication() {

    override fun onCreate() {
        // 开启EventBusAPT,优化反射效率 当组件作为App运行时需要将添加的Index注释掉 因为找不到对应的类了
        EventBus
            .builder()
//            .addIndex(module_homeEventIndex())
            .installDefaultEventBus()
        super.onCreate()
    }
}