package com.alan.mvvm.common.service

import android.content.Context
import com.alan.mvvm.base.utils.GsonUtil
import com.alan.mvvm.common.constant.RouteUrl
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.facade.service.SerializationService
import java.lang.reflect.Type

@Route(path = RouteUrl.JsonModule.ACTIVITY_JSON_JSON)
class JsonServiceImpl : SerializationService {
    override fun init(context: Context) {}
    override fun <T> json2Object(text: String, clazz: Class<T>): T {
        return GsonUtil.jsonToBean(text, clazz)!!
    }

    override fun object2Json(instance: Any): String {
        return GsonUtil.jsonToString(instance)
    }

    override fun <T : Any?> parseObject(input: String?, clazz: Type?): T {
        return GsonUtil.jsonToBean(input, clazz)!!
    }
}