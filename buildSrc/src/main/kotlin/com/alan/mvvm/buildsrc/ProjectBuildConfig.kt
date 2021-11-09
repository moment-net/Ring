package com.alan.mvvm.buildsrc

/**
 * 作者：alan
 * 时间：2021/7/28
 * 备注：项目相关参数配置
 */
object ProjectBuildConfig {
    const val compileSdkVersion = 30
    const val buildToolsVersion = "30.0.2"
    const val applicationId = "com.moment.ring"
    const val minSdkVersion = 21
    const val targetSdkVersion = 30
    const val versionCode = 5
    const val versionName = "1.0.4"
    const val isAppMode = false

    /**
     * 弃用
     * 项目当前的版本状态
     * 该状态直接反映当前App是测试版 还是正式版 或者预览版
     * 正式版:RELEASE、预览版(α)-内部测试版:ALPHA、测试版(β)-公开测试版:BETA
     * buildConfigField "String", "VERSION_TYPE", "\"${ProjectBuildConfig.Version.DEBUG}\""
     * buildConfigField "String", "VERSION_TYPE", "\"${ProjectBuildConfig.Version.RELEASE}\""
     */
    object Version {
        const val RELEASE = "VERSION_STATUS_RELEASE"

        const val DEBUG = "VERSION_STATUS_DEBUG"
    }
}