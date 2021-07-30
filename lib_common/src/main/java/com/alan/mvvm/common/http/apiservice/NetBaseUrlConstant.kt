package com.alan.mvvm.common.http.apiservice

/**
 * 作者：alan
 * 时间：2021/7/28
 * 备注：接口公共地址
 */
internal object NetBaseUrlConstant {

    val BASE_URL = "http://www.baidu.com"
    get() {
        if (field.isEmpty()){
            throw NotImplementedError("请求改你的 MAIN_URL 的值为自己的请求地址")
        }
       return  field
    }
}