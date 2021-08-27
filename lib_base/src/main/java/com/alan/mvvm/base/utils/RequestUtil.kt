package com.alan.mvvm.base.utils

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

/**
 * Json转换请求
 */
object RequestUtil {

    fun getPostBody(objects: Any?): RequestBody {
        val json: String = GsonUtil.jsonToString(objects)
        return json.toRequestBody("application/json;charset=UTF-8".toMediaTypeOrNull())
    }
}