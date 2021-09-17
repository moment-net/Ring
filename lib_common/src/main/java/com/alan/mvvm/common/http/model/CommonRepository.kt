package com.alan.mvvm.common.http.model

import com.alan.mvvm.base.http.apiservice.HomeApiService
import com.alan.mvvm.base.http.callback.RequestCallback
import com.alan.mvvm.base.http.requestbean.AccountBean
import com.alan.mvvm.base.http.responsebean.*
import com.alan.mvvm.base.mvvm.m.BaseRepository
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

    /**
     * 获取验证码
     */
    suspend fun requestCode(
        requestBody: RequestBody,
        callback: RequestCallback<String>
    ) = request(mApi, callback) { mApi.requestCode(requestBody) }

    /**
     * 用户注册接口(已注册使用手机号登陆)
     */
    suspend fun requestLogin(
        requestBody: RequestBody,
        callback: RequestCallback<LoginBean>
    ) = request(mApi, callback) { mApi.requestLogin(requestBody) }

    /**
     * 三方登录(微信)
     */
    suspend fun requestLoginWX(
        requestBody: RequestBody,
        callback: RequestCallback<LoginBean>
    ) = request(mApi, callback) { mApi.requestLoginWX(requestBody) }

    /**
     * 自动登陆
     */
    suspend fun requestAutoLogin(
        callback: RequestCallback<LoginBean>
    ) = request(mApi, callback) { mApi.requestAutoLogin() }

    /**
     * 绑定手机号
     */
    suspend fun requestBindPhone(
        requestBody: RequestBody,
        callback: RequestCallback<String>
    ) = request(mApi, callback) { mApi.requestBindPhone(requestBody) }

    /**
     * 一键获取手机号
     */
    suspend fun requestCheckPhone(
        requestBody: RequestBody,
        callback: RequestCallback<PhoneBean>
    ) = request(mApi, callback) { mApi.requestCheckPhone(requestBody) }

    /**
     * 绑定三方账号
     */
    suspend fun requestBindThird(
        requestBody: RequestBody,
        callback: RequestCallback<String>
    ) = request(mApi, callback) { mApi.requestBindThird(requestBody) }

    /**
     * 获取用户信息
     */
    suspend fun requestUserInfo(
        userId: String,
        callback: RequestCallback<UserInfoBean>
    ) = request(mApi, callback) { mApi.requestUserInfo(userId) }

    /**
     * 编辑用户信息
     */
    suspend fun requestEditUserInfo(
        requestBody: RequestBody,
        callback: RequestCallback<UserInfoBean>
    ) = request(mApi, callback) { mApi.requestEditUserInfo(requestBody) }

    /**
     * 极光推送设备注册
     */
    suspend fun requestDevicesRegister(
        requestBody: RequestBody,
        callback: RequestCallback<String>
    ) = request(mApi, callback) { mApi.requestDevicesRegister(requestBody) }

    /**
     * 上传图片
     */
    suspend fun requestUploadPic(
        part: MultipartBody.Part,
        callback: RequestCallback<FileBean>
    ) = request(mApi, callback) { mApi.requestUploadPic(part) }

    /**
     * 获取target信息
     */
    suspend fun requestTargetList(
        callback: RequestCallback<TargetBean>
    ) = request(mApi, callback) { mApi.requestTargetList() }

    /**
     * 用户选择target
     */
    suspend fun requestSaveTarget(
        requestBody: RequestBody,
        callback: RequestCallback<TargetInfoBean>
    ) = request(mApi, callback) { mApi.requestSaveTarget(requestBody) }

    /**
     * 首页饭友推荐
     */
    suspend fun requestMealList(
        map: MutableMap<String, String>,
        callback: RequestCallback<ArrayList<CookerBean>>
    ) = request(mApi, callback) { mApi.requestMealList(map) }

    /**
     * 消息列表
     */
    suspend fun requestMessage(
        map: MutableMap<String, String>,
        callback: RequestCallback<ArrayList<MessageBean>>
    ) = request(mApi, callback) { mApi.requestMessage(map) }

    /**
     * 消息列表
     */
    suspend fun requestSystemMessage(
        map: MutableMap<String, String>,
        callback: RequestCallback<ArrayList<SystemMessageBean>>
    ) = request(mApi, callback) { mApi.requestSystemMessage(map) }

    /**
     * 关注/取关
     */
    suspend fun requestChangeFollow(
        requestBody: RequestBody,
        callback: RequestCallback<UserInfoBean>
    ) = request(mApi, callback) { mApi.requestChangeFollow(requestBody) }

    /**
     * 我关注的列表
     */
    suspend fun requestFollowList(
        map: MutableMap<String, String>,
        callback: RequestCallback<ArrayList<UserInfoBean>>
    ) = request(mApi, callback) { mApi.requestFollowList(map) }

    /**
     * 关注我的列表
     */
    suspend fun requestFansList(
        map: MutableMap<String, String>,
        callback: RequestCallback<ArrayList<UserInfoBean>>
    ) = request(mApi, callback) { mApi.requestFansList(map) }

    /**
     * 账户余额(钻石)
     */
    suspend fun requestDiamond(
        callback: RequestCallback<DiamondBean>
    ) = request(mApi, callback) { mApi.requestDiamond() }

    /**
     * 申请提现
     */
    suspend fun requestApply(
        callback: RequestCallback<ApplyRequestBean>
    ) = request(mApi, callback) { mApi.requestApply() }

    /**
     * 绑定用户微信名
     */
    suspend fun requestAddWXAccount(
        requestBody: RequestBody,
        callback: RequestCallback<String>
    ) = request(mApi, callback) { mApi.requestAddWXAccount(requestBody) }

    /**
     * 提现记录
     */
    suspend fun requestWithdrawList(
        map: MutableMap<String, String>,
        callback: RequestCallback<ArrayList<WithdrawBean>>
    ) = request(mApi, callback) { mApi.requestWithdrawList(map) }

    /**
     * 单条提现记录详情
     */
    suspend fun requestWithdrawDetail(
        tradeId: String,
        callback: RequestCallback<ArrayList<WithdrawBean>>
    ) = request(mApi, callback) { mApi.requestWithdrawDetail(tradeId) }

    /**
     * 查询微信绑定信息
     */
    suspend fun requestWxAccount(
        callback: RequestCallback<WXAccountBindBean>
    ) = request(mApi, callback) { mApi.requestWxAccount() }

    /**
     * 账户直说币余额相关
     */
    suspend fun requestAccount(
        callback: RequestCallback<AccountBean>
    ) = request(mApi, callback) { mApi.requestAccount() }


    /**
     * 收益历史/我收到的
     */
    suspend fun requestReceived(
        map: MutableMap<String, String>,
        callback: RequestCallback<ArrayList<ReceivedBean>>
    ) = request(mApi, callback) { mApi.requestReceived(map) }


    /**
     * 充值商品列表
     */
    suspend fun requestConsume(
        map: MutableMap<String, String>,
        callback: RequestCallback<ArrayList<ConsumeBean>>
    ) = request(mApi, callback) { mApi.requestConsume(map) }


    /**
     * 礼物列表
     */
    suspend fun requestGiftList(
        callback: RequestCallback<ArrayList<GiftBean>>
    ) = request(mApi, callback) { mApi.requestGiftList() }


    /**
     * 打赏礼物
     */
    suspend fun requestGive(
        requestBody: RequestBody,
        callback: RequestCallback<String>
    ) = request(mApi, callback) { mApi.requestGive(requestBody) }


    /**
     * 账户余额(钻石)
     */
    suspend fun requestGoodsList(
        type: String,
        callback: RequestCallback<ArrayList<GoodBean>>
    ) = request(mApi, callback) { mApi.requestGoodsList(type) }


    /**
     * 购买钻石
     */
    suspend fun requestOrder(
        requestBody: RequestBody,
        callback: RequestCallback<OrderBean>
    ) = request(mApi, callback) { mApi.requestOrder(requestBody) }


    /**
     * 微信支付
     */
    suspend fun requestPayWX(
        requestBody: RequestBody,
        callback: RequestCallback<PrepayBean>
    ) = request(mApi, callback) { mApi.requestPayWX(requestBody) }


    /**
     * 支付宝支付
     */
    suspend fun requestPayZFB(
        requestBody: RequestBody,
        callback: RequestCallback<String>
    ) = request(mApi, callback) { mApi.requestPayZFB(requestBody) }


    /**
     * 订单查询
     */
    suspend fun requestOrderInfo(
        orderId: String,
        callback: RequestCallback<OrderBean>
    ) = request(mApi, callback) { mApi.requestOrderInfo(orderId) }

    /**
     * 上传音频信息
     */
    suspend fun requestUploadAudio(
        type: MultipartBody.Part,
        duration: MultipartBody.Part,
        audio: MultipartBody.Part,
        callback: RequestCallback<FileBean>
    ) = request(mApi, callback) { mApi.requestUploadAudio(type, audio, duration) }


    /**
     * 进入语聊房
     */
    suspend fun requestRtcToken(
        requestBody: RequestBody,
        callback: RequestCallback<RtcTokenBean>
    ) = request(mApi, callback) { mApi.requestRtcToken(requestBody) }


    /**
     * 发起聊天
     */
    suspend fun requestChatStart(
        requestBody: RequestBody,
        callback: RequestCallback<CallBean>
    ) = request(mApi, callback) { mApi.requestChatStart(requestBody) }


    /**
     * 挂断聊天
     */
    suspend fun requestChatHangup(
        requestBody: RequestBody,
        callback: RequestCallback<CallEndBean>
    ) = request(mApi, callback) { mApi.requestChatHangup(requestBody) }


    /**
     * 用户是否在饭友匹配中
     */
    suspend fun requestCheckMatch(
        userId: String,
        callback: RequestCallback<MatchStatusBean>
    ) = request(mApi, callback) { mApi.requestCheckMatch(userId) }


}

