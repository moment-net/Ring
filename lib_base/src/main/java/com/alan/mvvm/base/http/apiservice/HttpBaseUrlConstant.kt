package com.alan.mvvm.base.http.apiservice

import com.alan.mvvm.base.BuildConfig

/**
 * 作者：alan
 * 时间：2021/7/28
 * 备注：接口公共地址
 */
object HttpBaseUrlConstant {
    //测试环境
    val BASE_URL: String
        get() {
            if (BuildConfig.DEBUG) {
                //测试环境
                return "http://59.110.143.77:9060/api/"
            } else {
                //正式环境
                return "http://freedomspeakapp.moment-network.com/api/"
            }
        }


    val BASE_H5URL = "https://h5test-carchat.moment-network.com/"

}