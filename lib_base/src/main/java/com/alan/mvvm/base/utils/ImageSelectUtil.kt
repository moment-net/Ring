package com.alan.mvvm.base.utils

import android.app.Activity
import com.huantansheng.easyphotos.EasyPhotos

object ImageSelectUtil {
    const val REQUESTCODE = 100

    /**
     * 选择图片
     */
    fun singlePic(activity: Activity) {
        EasyPhotos.createAlbum(activity, true, false, CoilEngine)
            .setFileProviderAuthority("${activity.application.packageName}.imageprovider")
            .setPuzzleMenu(false)
            .setCleanMenu(false)
            .start(REQUESTCODE);
    }


}