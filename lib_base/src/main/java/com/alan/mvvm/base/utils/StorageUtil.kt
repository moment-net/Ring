package com.alan.mvvm.base.utils

import android.content.Context
import android.os.Environment
import java.io.File


/**
 * 存储路径管理类
 * - 获取可用的Data路径
 * - 获取可用的Cache路径
 * - 获取项目的SD卡路径
 * - 获取Cache大小
 * - 删除Cache文件
 * <p>
 * - 基础知识
 * <p>
 * 卸载会被删除，
 * getCacheDir()方法用于获取/data/data/包名/cache目录
 * getExternalCacheDir()方法可以获取到 SDCard/Android/data/包名/cache/目录，存放临时缓存数据
 * getFilesDir()方法用于获取/data/data/包名/files目录
 * getExternalFilesDir()方法可以获取到 SDCard/Android/data/包名/files/目录，存放长时间保存的数据
 * 卸载不会被删除
 * getExternalStorageDirectory   SDCard根目录
 * getExternalStoragePublicDirectory  SDCard下google创建的一些指定目录
 */
object StorageUtil {

    /**
     * 外部缓存是否可以使用
     *
     * @return
     */
    private fun isExternalStorageWritable(): Boolean {
        val state = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED == state
    }

    /**
     * 获取缓存路径（外部缓存可以使用则使用外部缓存，不可用使用内部缓存）
     *
     * @return
     */
    fun getAbleCacheDir(context: Context): File? {
        return if (isExternalStorageWritable()) {
            context.externalCacheDir
        } else {
            // 只有此应用才能访问。拍照的时候有问题，因为拍照的应用写入不了该文件
            context.cacheDir
        }
    }

    /**
     * 获取数据路径（外部缓存可以使用则使用外部缓存，不可用使用内部缓存）
     *
     * @return
     */
    fun getAbleDataDir(context: Context): File? {
        return if (isExternalStorageWritable()) {
            context.getExternalFilesDir(null)
        } else {
            // 只有此应用才能访问。拍照的时候有问题，因为拍照的应用写入不了该文件
            context.filesDir
        }
    }

    /**
     * 获取内置指内置的SD卡下一个项目相关的目录
     *
     * @return
     */
    fun getExternalFileDir(): File? {
        val appDir = File(Environment.getExternalStorageDirectory(), "ring")
        if (!appDir.exists()) {
            appDir.mkdirs()
        }
        return appDir
    }

    /**
     * @return
     * @throws Exception 获取缓存路径文件大小
     */
    fun getTotalCacheSize(context: Context): String? {
        return try {
            var cacheSize: Long = FileUtil.getFolderSize(context.cacheDir)
            if (isExternalStorageWritable()) {
                cacheSize += FileUtil.getFolderSize(context.externalCacheDir!!)
            }
            FileUtil.getFormatSize(cacheSize.toDouble())
        } catch (e: Exception) {
            "0 KB"
        }
    }

    /**
     * 删除缓存
     */
    fun clearAllCache(context: Context) {
        FileUtil.deleteDir(context.cacheDir)
        if (isExternalStorageWritable()) {
            FileUtil.deleteDir(context.externalCacheDir!!)
        }
    }
}