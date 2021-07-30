package com.alan.mvvm.common.http.apiservice

import com.alan.mvvm.common.http.model.BaseResponse
import retrofit2.http.GET

/**
 * 作者：alan
 * 时间：2021/7/28
 * 备注：Home模块的接口
 */
interface HomeApiService{
    /**
     * 登录接口
     * 新版本的Retrofit支持直接声明成挂起函数，并且函数直接返回网络返回数据
     */
    @GET("user/login")
    suspend fun requestLogin(): BaseResponse<String>


}