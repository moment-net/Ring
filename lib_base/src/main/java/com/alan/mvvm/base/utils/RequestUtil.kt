package com.alan.mvvm.base.utils

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

/**
 * Json转换请求
 */
object RequestUtil {

    /**
     * 将对象转为请求体
     */
    fun getPostBody(objects: Any?): RequestBody {
        val json: String = GsonUtil.jsonToString(objects)
        return json.toRequestBody("application/json;charset=UTF-8".toMediaTypeOrNull())
    }


    /**
     * 将文件转换请求Part
     */
    fun getPostPart(file: File): MultipartBody.Part {
        val fileBody: RequestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val part = MultipartBody.Part.createFormData("image", file.getName(), fileBody)
        return part
    }
}