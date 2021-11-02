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
     * 注销账号
     */
    suspend fun requestLogoff(
        callback: RequestCallback<String>
    ) = request(mApi, callback) { mApi.requestLogoff() }

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
     * 获取用户信息(包含干饭信息)
     */
    suspend fun requestUserInfoDetail(
        userId: String,
        callback: RequestCallback<UserInfoBean>
    ) = request(mApi, callback) { mApi.requestUserInfoDetail(userId) }


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
     * 获取target所有列表
     */
    suspend fun requestTargetList(
        callback: RequestCallback<TargetBean>
    ) = request(mApi, callback) { mApi.requestTargetList() }

    /**
     * 编辑用户target
     */
    suspend fun requestSaveTarget(
        requestBody: RequestBody,
        callback: RequestCallback<TargetInfoBean>
    ) = request(mApi, callback) { mApi.requestSaveTarget(requestBody) }

    /**
     * 查看用户target
     */
    suspend fun requestTarget(
        userId: String,
        callback: RequestCallback<TargetInfoBean>
    ) = request(mApi, callback) { mApi.requestTarget(userId) }


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
     * 未读消息
     */
    suspend fun requestUnRead(
        callback: RequestCallback<UnreadBean>
    ) = request(mApi, callback) { mApi.requestUnRead() }

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

    /**
     * 查看用户干饭状态
     */
    suspend fun requestMealStatus(
        callback: RequestCallback<MealStateBean>
    ) = request(mApi, callback) { mApi.requestMealStatus() }


    /**
     * 修改干饭状态
     */
    suspend fun requestEditMeal(
        orderStatus: String,
        callback: RequestCallback<String>
    ) = request(mApi, callback) { mApi.requestEditMeal(orderStatus) }


    /**
     * 开启/关闭匹配
     */
    suspend fun requestMealStop(
        status: String,
        callback: RequestCallback<String>
    ) = request(mApi, callback) { mApi.requestMealStop(status) }

    /**
     * 匹配设置
     */
    suspend fun requestMatchSet(
        requestBody: RequestBody,
        callback: RequestCallback<String>
    ) = request(mApi, callback) { mApi.requestMatchSet(requestBody) }

    /**
     * 查看用户匹配设置
     */
    suspend fun requestSetInfo(
        callback: RequestCallback<AcceptBean>
    ) = request(mApi, callback) { mApi.requestSetInfo() }

    /**
     * 干饭匹配时间段
     */
    suspend fun requestMatchTime(
        callback: RequestCallback<ArrayList<MatchTimeBean>>
    ) = request(mApi, callback) { mApi.requestMatchTime() }


    /**
     * 发布正在状态
     */
    suspend fun requestPushNow(
        requestBody: RequestBody,
        callback: RequestCallback<String>
    ) = request(mApi, callback) { mApi.requestPushNow(requestBody) }

    /**
     * 首页“正在”列表
     */
    suspend fun requestNowList(
        map: MutableMap<String, String>,
        callback: RequestCallback<ArrayList<NowBean>>
    ) = request(mApi, callback) { mApi.requestNowList(map) }

    /**
     * 正在的状态列表
     */
    suspend fun requestNowTagList(
        callback: RequestCallback<ArrayList<NowTagBean>>
    ) = request(mApi, callback) { mApi.requestNowTagList() }

    /**
     * 屏蔽正在
     */
    suspend fun requestBanNow(
        requestBody: RequestBody,
        callback: RequestCallback<String>
    ) = request(mApi, callback) { mApi.requestBanNow(requestBody) }

    /**
     * 回复一起(被发起人的聊天消息)
     */
    suspend fun requestReply(
        requestBody: RequestBody,
        callback: RequestCallback<String>
    ) = request(mApi, callback) { mApi.requestReply(requestBody) }

    /**
     * 检测一起是否被已回复
     */
    suspend fun requestIsReply(
        requestBody: RequestBody,
        callback: RequestCallback<ReplyBean>
    ) = request(mApi, callback) { mApi.requestIsReply(requestBody) }

    /**
     * 获取匹配用户
     */
    suspend fun requestNowMatch(
        callback: RequestCallback<UserInfoBean>
    ) = request(mApi, callback) { mApi.requestNowMatch() }

    /**
     * 发布想法
     */
    suspend fun requestPushThink(
        requestBody: RequestBody,
        callback: RequestCallback<String>
    ) = request(mApi, callback) { mApi.requestPushThink(requestBody) }

    /**
     * 首页“想法”列表
     */
    suspend fun requestThinkList(
        map: MutableMap<String, String>,
        callback: RequestCallback<ArrayList<ThinkBean>>
    ) = request(mApi, callback) { mApi.requestThinkList(map) }

    /**
     * 屏蔽想法
     */
    suspend fun requestBanThink(
        requestBody: RequestBody,
        callback: RequestCallback<String>
    ) = request(mApi, callback) { mApi.requestBanThink(requestBody) }

    /**
     * 点赞/取消点赞
     */
    suspend fun requestZan(
        requestBody: RequestBody,
        callback: RequestCallback<String>
    ) = request(mApi, callback) { mApi.requestZan(requestBody) }

    /**
     * 根据用户id查询所有想法
     */
    suspend fun requestThinkHistory(
        map: MutableMap<String, String>,
        callback: RequestCallback<ArrayList<ThinkBean>>
    ) = request(mApi, callback) { mApi.requestThinkHistory(map) }

    /**
     * 匹配信息（配置设置和匹配次数）
     */
    suspend fun requestMatchInfo(
        callback: RequestCallback<MatchInfoBean>
    ) = request(mApi, callback) { mApi.requestMatchInfo() }

    /**
     * 匹配设置
     */
    suspend fun requestMatchFilter(
        requestBody: RequestBody,
        callback: RequestCallback<String>
    ) = request(mApi, callback) { mApi.requestMatchFilter(requestBody) }

    /**
     * 快速比配
     */
    suspend fun requestFastMatch(
        requestBody: RequestBody,
        callback: RequestCallback<UserInfoBean>
    ) = request(mApi, callback) { mApi.requestFastMatch(requestBody) }

    /**
     * 开始匹配（加入）
     */
    suspend fun requestMatchJoin(
        callback: RequestCallback<String>
    ) = request(mApi, callback) { mApi.requestMatchJoin() }

    /**
     * 开始匹配（停止）
     */
    suspend fun requestMatchStop(
        callback: RequestCallback<String>
    ) = request(mApi, callback) { mApi.requestMatchStop() }


    /**
     * 所有类型的社交卡片的字段列表
     * type :match=首页匹配 card=社交卡片 默认社交卡片
     */
    suspend fun requestCardAllList(
        type: String,
        callback: RequestCallback<ArrayList<CardTagBean>>
    ) = request(mApi, callback) { mApi.requestCardAllList(type) }

    /**
     * 添加社交卡片
     */
    suspend fun requestAddCard(
        requestBody: RequestBody,
        callback: RequestCallback<String>
    ) = request(mApi, callback) { mApi.requestAddCard(requestBody) }

    /**
     * 编辑社交卡片
     */
    suspend fun requestEditCard(
        cardId: String,
        requestBody: RequestBody,
        callback: RequestCallback<String>
    ) = request(mApi, callback) { mApi.requestEditCard(cardId, requestBody) }

    /**
     * 删除社交卡片
     */
    suspend fun requestDeleteCard(
        cardId: String,
        callback: RequestCallback<String>
    ) = request(mApi, callback) { mApi.requestDeleteCard(cardId) }

    /**
     * 查询用户的社交卡片列表
     */
    suspend fun requestCardList(
        userId: String,
        callback: RequestCallback<ArrayList<CardInfoBean>>
    ) = request(mApi, callback) { mApi.requestCardList(userId) }

    /**
     * 根据卡片名称查询用户的社交卡片列表
     */
    suspend fun requestCardDetail(
        map: MutableMap<String, String>,
        callback: RequestCallback<CardDetailBean>
    ) = request(mApi, callback) { mApi.requestCardDetail(map) }

    /**
     * banner列表
     */
    suspend fun requestBanner(
        callback: RequestCallback<ArrayList<BannerBean>>
    ) = request(mApi, callback) { mApi.requestBanner() }

}

