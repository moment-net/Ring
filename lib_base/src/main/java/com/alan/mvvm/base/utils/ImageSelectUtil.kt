package com.alan.mvvm.base.utils

import android.app.Activity
import com.huantansheng.easyphotos.EasyPhotos

object ImageSelectUtil {
    const val REQUESTCODE = 100
    const val REQUESTVEDIOCODE = 101

    /**
     * 选择图片
     * 内部已做权限处理
     */
    fun singlePic(activity: Activity) {
        EasyPhotos.createAlbum(activity, true, false, CoilEngine)
            .setFileProviderAuthority("${activity.application.packageName}.imageprovider")
            .setPuzzleMenu(false)
            .setCleanMenu(false)
            .start(REQUESTCODE);
    }

    /**
     * 选择视频
     * 内部已做权限处理
     */
    fun singleVedio(activity: Activity) {
        EasyPhotos.createAlbum(activity, true, false, CoilEngine)
            .setFileProviderAuthority("${activity.application.packageName}.imageprovider")
            .complexSelector(true, 1, 1)
            .setVideo(true)
            .setPuzzleMenu(false)
            .setCleanMenu(false)
            .start(REQUESTVEDIOCODE);
    }

}