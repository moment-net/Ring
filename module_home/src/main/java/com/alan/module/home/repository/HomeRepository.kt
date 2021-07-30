package com.alan.module.home.repository

import com.alan.mvvm.base.mvvm.m.BaseRepository
import com.alan.mvvm.common.http.apiservice.HomeApiService
import kotlinx.coroutines.delay
import javax.inject.Inject

/**
 * 作者：alan
 * 时间：2021/7/28
 * 备注：首页M层
 */
class HomeRepository @Inject constructor() : BaseRepository() {

    @Inject
    lateinit var mApi: HomeApiService

    /**
     * 模拟获取数据
     */
    suspend fun getData() = request<String> {
        mApi.requestLogin()
    }
}