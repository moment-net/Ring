package com.alan.mvvm.buildsrc

/**
 * 作者：alan
 * 时间：2021/7/28
 * 备注：项目依赖版本统一管理
 */
object DependencyConfig {

    /**
     * 依赖版本号
     */
    object Version {

        // AndroidX--------------------------------------------------------------
        const val AppCompat = "1.2.0"
        const val CoreKtx = "1.3.1"
        const val ConstraintLayout = "2.0.1"                // 约束布局
        const val TestExtJunit = "1.1.2"
        const val TestEspresso = "3.3.0"
        const val ActivityKtx = "1.1.0"
        const val FragmentKtx = "1.2.5"
        const val MultiDex = "2.0.1"

        // Android---------------------------------------------------------------
        const val Junit = "4.13"
        const val Material = "1.2.0"                        // 材料设计UI套件

        // Kotlin----------------------------------------------------------------
        const val Kotlin = "1.5.10"
        const val Coroutines = "1.5.0"                      // 协程

        // JetPack---------------------------------------------------------------
        const val Lifecycle = "2.3.1"                       // Lifecycle相关（ViewModel & LiveData & Lifecycle）
        const val Hilt = "2.35.1"                           // DI框架-Hilt
        const val HiltAndroidx = "1.0.0"
        const val Room = "2.2.5"                            // 数据库框架-Hilt

        // GitHub----------------------------------------------------------------
        const val OkHttp = "4.9.0"                          // OkHttp
        const val OkHttpInterceptorLogging = "4.9.1"        // OkHttp 请求Log拦截器
        const val Retrofit = "2.9.0"                        // Retrofit
        const val RetrofitConverterGson = "2.9.0"           // Retrofit Gson 转换器
        const val Gson = "2.8.7"                            // Gson
        const val MMKV = "1.2.9"                            // 腾讯 MMKV 替代SP
        const val AutoSize = "1.2.1"                        // 屏幕适配
        const val ARoute = "1.5.1"                          // 阿里路由
        const val ARouteCompiler = "1.5.1"                  // 阿里路由 APT
        const val RecyclerViewAdapter = "3.0.4"             // RecyclerViewAdapter
        const val SmartRefresh = "2.0.1"                    // SmartRefresh
        const val StatusBar = "1.5.1"                       // 状态栏
        const val EventBus = "3.2.0"                        // 事件总线
        const val PermissionX = "1.4.0"                     // 权限申请
        const val LeakCanary = "2.7"                        // 检测内存泄漏
        const val AutoService = "1.0"                       // 自动生成SPI暴露服务文件
        const val Coil = "1.4.0"                            // Kotlin图片加载框架
        const val GlideVersion = "4.12.0"                   // Glide图片加载框架
        const val KlogVersion = "1.6.0"                     // Klog日志框架
        const val ShapeViewVersion = "1.0.1"                // ShapeView框架
        const val BannerVersion = "2.1.5"                   // Banner框架
        const val XPopupVersion = "2.6.8"                   // XPopupDialog框架
        const val SubImageViewVersion = "3.10.0"            // SubImageViewVersion框架
        const val PickerViewVersion = "4.1.9"               // PickerView框架
        const val LottieVersion = "3.5.0"                   // Lottie框架
        const val CameraVersion = "1.1.9"                   // Camera框架
        const val EasyFloatVersion = "2.0.3"                // 悬浮窗权限框架
        const val FlexboxVersion = "3.0.0"                  // 流式排列框架
        const val PictureselectorVersion = "v2.7.3-rc09"    // 图片选择框架
        const val NewGuideVersion = "v2.4.0"                // 新手引导框架
        const val TransfereeVersion = "1.6.1"               // 图片预览框架
        const val ImageViewerVersion = "3.0.1"              // 图片预览框架

        // 第三方SDK--------------------------------------------------------------
        const val WeChat = "6.6.4"                          // 腾讯开放平台微信
        const val TencentBugly = "3.3.9"                    // 腾讯Bugly 异常上报
        const val TencentBuglyNative = "3.8.0"              // Bugly native异常上报
        const val TencentTBSX5 = "43939"                    // 腾讯X5WebView
        const val IMVersion = "3.8.5"                       // 环信版本
        const val AgoraVersion = "3.3.0"                    // 声网版本
        const val OneLoginVersion = "2.5.1"                 // 极验框架
        const val JPushVersion = "4.1.5"                    // 极光推送版本
        const val JCoreVersion = "2.9.0"                    // 极光推送版本
        const val HWHMSVersion = "5.3.0.304"                // 华为推送版本
        const val HWPushVersion = "4.1.5"                   // 华为推送版本
        const val XiaomiPushVersion = "4.1.5"               // 小米推送版本
        const val OppoPushVersion = "4.1.5"                 // oppo推送版本
        const val VivoPushVersion = "4.1.5"                 // vivo推送版本
        const val AmplitudeVersion = "2.32.1"               // amplitude数据上报版本
        const val AliyunVersion = "2.9.5"                   // AliyunVersion版本
    }

    /**
     * AndroidX相关依赖
     */
    object AndroidX {
        const val AndroidJUnitRunner = "androidx.test.runner.AndroidJUnitRunner"
        const val AppCompat = "androidx.appcompat:appcompat:${Version.AppCompat}"
        const val CoreKtx = "androidx.core:core-ktx:${Version.CoreKtx}"
        const val ConstraintLayout = "androidx.constraintlayout:constraintlayout:${Version.ConstraintLayout}"
        const val TestExtJunit = "androidx.test.ext:junit:${Version.TestExtJunit}"
        const val TestEspresso = "androidx.test.espresso:espresso-core:${Version.TestEspresso}"
        const val ActivityKtx = "androidx.activity:activity-ktx:${Version.ActivityKtx}"
        const val FragmentKtx = "androidx.fragment:fragment-ktx:${Version.FragmentKtx}"
        const val MultiDex = "androidx.multidex:multidex:${Version.MultiDex}"
    }

    /**
     * Android相关依赖
     */
    object Android {
        const val Junit = "junit:junit:${Version.Junit}"
        const val Material = "com.google.android.material:material:${Version.Material}"
    }

    /**
     * JetPack相关依赖
     */
    object JetPack {
        const val ViewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Version.Lifecycle}"
        const val ViewModelSavedState =
            "androidx.lifecycle:lifecycle-viewmodel-savedstate:${Version.Lifecycle}"
        const val LiveData = "androidx.lifecycle:lifecycle-livedata-ktx:${Version.Lifecycle}"
        const val Lifecycle = "androidx.lifecycle:lifecycle-runtime-ktx:${Version.Lifecycle}"
        const val LifecycleCompilerAPT =
            "androidx.lifecycle:lifecycle-compiler:${Version.Lifecycle}"
        const val HiltCore = "com.google.dagger:hilt-android:${Version.Hilt}"
        const val HiltApt = "com.google.dagger:hilt-compiler:${Version.Hilt}"
        const val HiltAndroidx = "androidx.hilt:hilt-compiler:${Version.HiltAndroidx}"
        const val Room = "androidx.room:room-runtime:${Version.Room}"
        const val RoomApt = "androidx.room:room-compiler:${Version.Room}"
    }

    /**
     * Kotlin相关依赖
     */
    object Kotlin {
        const val Kotlin = "org.jetbrains.kotlin:kotlin-stdlib:${Version.Kotlin}"
        const val CoroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Version.Coroutines}"
        const val CoroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Version.Coroutines}"
    }

    /**
     * GitHub及其他相关依赖
     */
    object GitHub {
        const val OkHttp = "com.squareup.okhttp3:okhttp:${Version.OkHttp}"
        const val OkHttpInterceptorLogging =
            "com.squareup.okhttp3:logging-interceptor:${Version.OkHttpInterceptorLogging}"
        const val Retrofit = "com.squareup.retrofit2:retrofit:${Version.Retrofit}"
        const val RetrofitConverterGson =
            "com.squareup.retrofit2:converter-gson:${Version.RetrofitConverterGson}"
        const val Gson = "com.google.code.gson:gson:${Version.Gson}"
        const val MMKV = "com.tencent:mmkv-static:${Version.MMKV}"
        const val AutoSize = "me.jessyan:autosize:${Version.AutoSize}"
        const val ARoute = "com.alibaba:arouter-api:${Version.ARoute}"
        const val ARouteCompiler = "com.alibaba:arouter-compiler:${Version.ARouteCompiler}"
        const val RecyclerViewAdapter =
            "com.github.CymChad:BaseRecyclerViewAdapterHelper:${Version.RecyclerViewAdapter}"
        const val Smartrefresh = "com.scwang.smart:refresh-layout-kernel:${Version.SmartRefresh}"
        const val Refreshheader = "com.scwang.smart:refresh-header-material:${Version.SmartRefresh}"
        const val Refreshfooter = "com.scwang.smart:refresh-footer-classics:${Version.SmartRefresh}"
        const val StatusBar = "com.jaeger.statusbarutil:library:${Version.StatusBar}"
        const val EventBus = "org.greenrobot:eventbus:${Version.EventBus}"
        const val EventBusAPT = "org.greenrobot:eventbus-annotation-processor:${Version.EventBus}"
        const val PermissionX = "com.permissionx.guolindev:permissionx:${Version.PermissionX}"
        const val LeakCanary = "com.squareup.leakcanary:leakcanary-android:${Version.LeakCanary}"
        const val AutoService = "com.google.auto.service:auto-service:${Version.AutoService}"
        const val AutoServiceAnnotations =
            "com.google.auto.service:auto-service-annotations:${Version.AutoService}"
        const val Coil = "io.coil-kt:coil:${Version.Coil}"
        const val CoilGIF = "io.coil-kt:coil-gif:${Version.Coil}"
        const val CoilSVG = "io.coil-kt:coil-svg:${Version.Coil}"
        const val CoilVideo = "io.coil-kt:coil-video:${Version.Coil}"
        const val Glide = "com.github.bumptech.glide:glide:${Version.GlideVersion}"
        const val KLog = "com.github.zhaokaiqiang.klog:library:${Version.KlogVersion}"
        const val ShapeView = "com.github.leifu1107:ShapeView:${Version.ShapeViewVersion}"
        const val Banner = "com.bigkoo:convenientbanner:${Version.BannerVersion}"
        const val XPopup = "com.github.li-xiaojun:XPopup:${Version.XPopupVersion}"
        const val SubImageView =
            "com.davemorrissey.labs:subsampling-scale-image-view-androidx:${Version.SubImageViewVersion}"
        const val PickerView = "com.contrarywind:Android-PickerView:${Version.PickerViewVersion}"
        const val Lottie = "com.airbnb.android:lottie:${Version.LottieVersion}"
        const val Camera = "cjt.library.wheel:camera:${Version.CameraVersion}"
        const val EasyFloat = "com.github.princekin-f:EasyFloat:${Version.EasyFloatVersion}"
        const val Flexbox = "com.google.android.flexbox:flexbox:${Version.FlexboxVersion}"
        const val Pictureselector =
            "io.github.lucksiege:pictureselector:${Version.PictureselectorVersion}"
        const val NewGuide = "com.github.huburt-Hu:NewbieGuide:${Version.NewGuideVersion}"
        const val Transferee =
            "com.github.Hitomis.transferee:Transferee:${Version.TransfereeVersion}"
        const val TransfereeGlide =
            "com.github.Hitomis.transferee:GlideImageLoader:${Version.TransfereeVersion}"
        const val ImageViewer = "indi.liyi.view:image-viewer:${Version.ImageViewerVersion}"
    }

    /**
     * SDK相关依赖
     */
    object SDK {
        const val WeChat = "com.tencent.mm.opensdk:wechat-sdk-android-without-mta:${Version.WeChat}"
        const val TencentBugly = "com.tencent.bugly:crashreport:${Version.TencentBugly}"
        const val TencentBuglyNative =
            "com.tencent.bugly:nativecrashreport:${Version.TencentBuglyNative}"
        const val TencentTBSX5 = "com.tencent.tbs.tbssdk:sdk:${Version.TencentTBSX5}"
        const val OneLogin = "com.geetest.android:onelogin:${Version.OneLoginVersion}"
        const val IMChat = "io.hyphenate:hyphenate-chat:${Version.IMVersion}"
//        const val IMCall = "io.hyphenate:ease-call-kit:${Version.IMVersion}"
        const val Agora = "io.agora.rtc:full-sdk:${Version.AgoraVersion}"
        const val JPush = "cn.jiguang.sdk:jpush:${Version.JPushVersion}"
        const val JCore = "cn.jiguang.sdk:jcore:${Version.JCoreVersion}"
        const val HWHms = "com.huawei.hms:push:${Version.HWHMSVersion}"
        const val HWplugin = "cn.jiguang.sdk.plugin:huawei:${Version.HWPushVersion}"
        const val Xiaomiplugin = "cn.jiguang.sdk.plugin:xiaomi:${Version.XiaomiPushVersion}"
        const val Oppoplugin = "cn.jiguang.sdk.plugin:oppo:${Version.OppoPushVersion}"
        const val Vivoplugin = "cn.jiguang.sdk.plugin:vivo:${Version.VivoPushVersion}"
        const val Amplitude = "com.amplitude:android-sdk:${Version.AmplitudeVersion}"
        const val Aliyun = "com.aliyun.dpa:oss-android-sdk:${Version.AliyunVersion}"

    }
}