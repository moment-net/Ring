package com.alan.mvvm.common.http.model

import com.alan.mvvm.base.http.apiservice.HomeApiService
import com.alan.mvvm.base.http.callback.RequestCallback
import com.alan.mvvm.base.http.responsebean.FileBean
import com.alan.mvvm.base.http.responsebean.LoginBean
import com.alan.mvvm.base.http.responsebean.PhoneBean
import com.alan.mvvm.base.http.responsebean.ThridLoginBean
import com.alan.mvvm.base.mvvm.m.BaseRepository
import com.tencent.bugly.crashreport.biz.UserInfoBean
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

/**
 * 作者：alan
 * 时间：2021/7/28
 * 备注：项目相关M层实现
 */
class CommonRepository @Inject constructor() : BaseRepository() {

    @Inject
    lateinit var mApi: HomeApiService


    suspend fun requestCode(
        requestBody: RequestBody,
        callback: RequestCallback<String>
    ) = request(mApi, callback) { mApi.requestCode(requestBody) }


    suspend fun requestLogin(
        requestBody: RequestBody,
        callback: RequestCallback<LoginBean>
    ) = request(mApi, callback) { mApi.requestLogin(requestBody) }


    suspend fun requestLoginWX(
        requestBody: RequestBody,
        callback: RequestCallback<ThridLoginBean>
    ) = request(mApi, callback) { mApi.requestLoginWX(requestBody) }


    suspend fun requestAutoLogin(
        callback: RequestCallback<LoginBean>
    ) = request(mApi, callback) { mApi.requestAutoLogin() }


    suspend fun requestBindPhone(
        requestBody: RequestBody,
        callback: RequestCallback<LoginBean>
    ) = request(mApi, callback) { mApi.requestBindPhone(requestBody) }


    suspend fun requestCheckPhone(
        requestBody: RequestBody,
        callback: RequestCallback<PhoneBean>
    ) = request(mApi, callback) { mApi.requestCheckPhone(requestBody) }


    suspend fun requestBindThird(
        requestBody: RequestBody,
        callback: RequestCallback<UserInfoBean>
    ) = request(mApi, callback) { mApi.requestBindThird(requestBody) }


    suspend fun requestUserInfo(
        userId: String,
        callback: RequestCallback<UserInfoBean>
    ) = request(mApi, callback) { mApi.requestUserInfo(userId) }


    suspend fun requestEditUserInfo(
        requestBody: RequestBody,
        callback: RequestCallback<String>
    ) = request(mApi, callback) { mApi.requestEditUserInfo(requestBody) }


    suspend fun requestDevicesRegister(
        requestBody: RequestBody,
        callback: RequestCallback<String>
    ) = request(mApi, callback) { mApi.requestDevicesRegister(requestBody) }


    suspend fun requestUploadPic(
        part: MultipartBody.Part,
        callback: RequestCallback<FileBean>
    ) = request(mApi, callback) { mApi.requestUploadPic(part) }


}