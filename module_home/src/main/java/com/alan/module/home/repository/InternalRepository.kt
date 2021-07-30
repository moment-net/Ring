package com.alan.module.home.repository

import com.alan.mvvm.base.mvvm.m.BaseRepository
import kotlinx.coroutines.delay
import javax.inject.Inject


class InternalRepository @Inject constructor() : BaseRepository() {

    suspend fun getData() = request<String> {
        delay(1000)
        emit("数据加载成功")
    }
}