package com.alan.mvvm.common.http.model

import com.alan.mvvm.base.mvvm.m.BaseRepository
import com.alan.mvvm.common.http.apiservice.HomeApiService
import javax.inject.Inject

/**
 * 作者：alan
 * 时间：2021/7/28
 * 备注：项目相关M层实现
 */
class CommonRepository @Inject constructor() : BaseRepository() {

    @Inject
    lateinit var mApi: HomeApiService

    /**
     * 模拟获取数据
     */
    suspend fun getData() = request<String> {
        mApi.requestLogin().apply {
            emit("")
        }
    }
}