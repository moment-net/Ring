package com.alan.mvvm.base.utils

import android.content.Context
import android.graphics.Bitmap
import java.io.*

object BitmapUtil {

    fun save(context: Context?, bitmap: Bitmap?): String? {
        if (bitmap == null) {
            return ""
        }
        val file: File
        var out: FileOutputStream? = null
        val imagePath: File = File(StorageUtil.getExternalFileDir(), "img")
        if (!imagePath.exists()) {
            imagePath.mkdirs()
        }
        file = File(imagePath, System.currentTimeMillis().toString() + ".jpg")
        try {
            out = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            return file.path
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } finally {
            if (out != null) {
                try {
                    out.flush()
                    out.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return ""
    }

    fun bmpToByteArray(bmp: Bitmap, needRecycle: Boolean): ByteArray? {
        val output = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output)
        if (needRecycle) {
            bmp.recycle()
        }
        val result = output.toByteArray()
        try {
            output.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }
}