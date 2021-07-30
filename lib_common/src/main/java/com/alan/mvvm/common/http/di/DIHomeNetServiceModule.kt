package com.alan.mvvm.common.http.di

import com.alan.mvvm.common.http.apiservice.HomeApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * 作者：alan
 * 时间：2021/7/28
 * 备注：全局作用域的Home组件网络接口代理依赖注入模块
 */
@Module
@InstallIn(SingletonComponent::class)
class DIHomeNetServiceModule {

    /**
     * Home模块的[HomeApiService]依赖提供方法
     *
     * @param retrofit Retrofit
     * @return HomeApiService
     */
    @Singleton
    @Provides
    fun provideHomeApiService(retrofit: Retrofit): HomeApiService {
        return retrofit.create(HomeApiService::class.java)
    }
}