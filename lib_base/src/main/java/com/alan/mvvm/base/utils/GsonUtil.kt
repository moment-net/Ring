package com.alan.mvvm.base.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import java.util.*

object GsonUtil {
    private var gson: Gson? = null

    //判断gson对象是否存在了,不存在则创建对象
    init {
        if (gson == null) {
            //gson = new Gson();
            //当使用GsonBuilder方式时属性为空的时候输出来的json字符串是有键值key的,显示形式是"key":null，而直接new出来的就没有"key":null的
            gson = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create()
        }
    }

    /**
     * 将对象转成json格式
     *
     * @param object
     * @return String
     */
    fun jsonToString(objects: Any?): String {
        var gsonString: String = ""
        if (gson != null) {
            gsonString = gson!!.toJson(objects)
        }
        return gsonString
    }

    /**
     * 将json转成特定的cls的对象
     *
     * @param gsonString
     * @param cls
     * @return
     */
    fun <T> jsonToBean(gsonString: String?, cls: Class<T>?): T? {
        var t: T? = null
        if (gson != null) {
            //传入json对象和对象类型,将json转成对象
            t = gson!!.fromJson(gsonString, cls)
        }
        return t
    }

    /**
     * json字符串转成list
     *
     * @param gsonString
     * @param cls
     * @return
     */
//    fun <T> jsonToList(gsonString: String?, cls: Class<T>?): List<T>? {
//        var list: List<T>? = null
//        if (gson != null) {
//            //根据泛型返回解析指定的类型,TypeToken<List<T>>{}.getType()获取返回类型
//            list = gson!!.fromJson(gsonString, object : TypeToken<List<T>?>() {}.type)
//        }
//        return list
//    }

    /**
     * json字符串转成list
     *
     * @param cls
     * @return
     */
    fun <T> jsonToList(json: String?, cls: Class<T>?): List<T> {
        val mList = ArrayList<T>()
        val array = JsonParser.parseString(json).asJsonArray
        for (elem in array) {
            mList.add(gson!!.fromJson(elem, cls))
        }
        return mList
    }

    /**
     * json字符串转成list中有map的
     *
     * @param gsonString
     * @return
     */
    fun <T> jsonToListMaps(gsonString: String?): List<Map<String, T>>? {
        var list: List<Map<String, T>>? = null
        if (gson != null) {
            list = gson!!.fromJson(
                gsonString,
                object : TypeToken<List<Map<String?, T>?>?>() {}.type
            )
        }
        return list
    }

    /**
     * json字符串转成map的
     *
     * @param gsonString
     * @return
     */
    fun <T> jsonToMaps(gsonString: String?): Map<String, T>? {
        var map: Map<String, T>? = null
        if (gson != null) {
            map = gson!!.fromJson(gsonString, object : TypeToken<Map<String?, T>?>() {}.type)
        }
        return map
    }

    /**
     * json字符串转成map的
     *
     * @param gsonString
     * @return
     */
    fun jsonToHashMap(gsonString: String?): Map<String?, String?>? {
        var map: Map<String?, String?>? = null
        if (gson != null) {
            map = gson!!.fromJson(gsonString, object : TypeToken<Map<String?, String?>?>() {}.type)
        }
        return map
    }


}