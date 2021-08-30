package com.alan.mvvm.base.http.apiservice

import com.alan.mvvm.base.http.baseresp.BaseResponse
import com.alan.mvvm.base.http.responsebean.*
import com.tencent.bugly.crashreport.biz.UserInfoBean
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

/**
 * 作者：alan
 * 时间：2021/7/28
 * 备注：Home模块的接口
 */
interface HomeApiService {

    /**
     * 获取验证码
     *
     * @return
     */
    @POST("user/common/sendVerifyCode")
    suspend fun requestCode(@Body requestBody: RequestBody): BaseResponse<String>

    /**
     * 用户注册接口(已注册使用手机号登陆)
     *
     * @return
     */
    @POST("user/login")
    suspend fun requestLogin(@Body requestBody: RequestBody): BaseResponse<LoginBean>

    /**
     * 三方登录(微信)
     *
     * @return
     */
    @POST("user/login/third")
    suspend fun requestLoginWX(@Body requestBody: RequestBody): BaseResponse<ThridLoginBean>


    /**
     * 自动登陆
     *
     * @return
     */
    @POST("user/auto")
    suspend fun requestAutoLogin(): BaseResponse<LoginBean>

    /**
     * 绑定手机号
     *
     * @return
     */
    @POST("user/bindPhone")
    suspend fun requestBindPhone(@Body requestBody: RequestBody): BaseResponse<LoginBean>


    /**
     * 一键获取手机号
     *
     * @return
     */
    @POST("user/login/checkphone")
    suspend fun requestCheckPhone(@Body requestBody: RequestBody): BaseResponse<PhoneBean>

    /**
     * 绑定三方账号
     *
     * @return
     */
    @POST("user/bindThird")
    suspend fun requestBindThird(@Body requestBody: RequestBody): BaseResponse<UserInfoBean>

    /**
     * 获取用户信息
     *
     * @return
     */
    @GET("user/info/{userId}")
    suspend fun requestUserInfo(@Path("userId") userId: String): BaseResponse<UserInfoBean>


    /**
     * 编辑用户信息
     *
     * @return
     */
    @POST("user/edit")
    suspend fun requestEditUserInfo(@Body requestBody: RequestBody): BaseResponse<String>

    /**
     * 极光推送设备注册
     */
    @POST("device/register")
    suspend fun requestDevicesRegister(@Body requestBody: RequestBody): BaseResponse<String>


    /**
     * 上传图片
     *
     * @return
     */
    @Multipart
    @POST("user/common/uploadImage")
    suspend fun requestUploadPic(@Part img: MultipartBody.Part): BaseResponse<FileBean>


    /**
     * 获取target信息
     *
     * @return
     */
    @GET("user/taglist")
    suspend fun requestTargetList(): BaseResponse<TargetBean>


    /**
     * 用户选择target
     */
    @POST("user/editmealtag")
    suspend fun requestSaveTarget(@Body requestBody: RequestBody): BaseResponse<TargetInfoBean>

    /**
     * 用户查看target
     */
    @POST("user/mealtag")
    suspend fun requestTarget(@Query("userId") userId: String): BaseResponse<TargetInfoBean>

//
//
//    /**
//     * 消息列表
//     *
//     * @return
//     */
//    @GET("user/notice/list")
//    suspend fun requestMessage(@QueryMap map: MutableMap<String, String>): BaseResponse<ArrayList<MessageBean>>
//
//    /**
//     * 消息列表
//     *
//     * @return
//     */
//    @GET("user/notice/list")
//    suspend fun requestSystemMessage(@QueryMap map: MutableMap<String, String>): BaseResponse<ArrayList<SystemMessageBean>>
//
//    /**
//     * 未读回复数
//     *
//     * @return
//     */
//    @GET("msgboard/reply/unreadCount")
//    suspend fun requestMsgboardUnread(): BaseResponse<String>
//
//
//    /**
//     * 关注/取关
//     *
//     * @return
//     */
//    
//    @POST("user/relation/follow")
//    suspend fun requestChangeFollow(@Body requestBody:RequestBody): BaseResponse<ChangeFollowBean>
//
//    /**
//     * 我关注的列表
//     *
//     * @return
//     */
//    @GET("user/relation/followlist")
//    suspend fun requestFollowList(@QueryMap map: MutableMap<String, String>): BaseResponse<ArrayList<UserInfoBean>>
//
//
//    /**
//     * 关注我的列表
//     *
//     * @return
//     */
//    @GET("user/relation/fanslist")
//    suspend fun requestFansList(@QueryMap map: MutableMap<String, String>): BaseResponse<ArrayList<UserInfoBean>>
//
//
//    /**
//     * 创建语聊房
//     *
//     * @return
//     */
//    
//    @POST("channel/create")
//    suspend fun requestCreateChannel(@Body requestBody:RequestBody): BaseResponse<ChannelBean>
//
//    /**
//     * 标签列表
//     *
//     * @return
//     */
//    @GET("user/common/tag")
//    suspend fun requestTagList(): BaseResponse<LabelListBean>
//
//
//    /**
//     * 退出语聊房
//     *
//     * @return
//     */
//    
//    @POST("channel/out")
//    suspend fun requestOutChannel(@Body requestBody:RequestBody): BaseResponse<ChannelBean>
//
//    /**
//     * 转让语聊房
//     *
//     * @return
//     */
//    
//    @PUT("channel/transfer")
//    suspend fun requestTransferChannel(@Body requestBody:RequestBody): BaseResponse
//
//    /**
//     * 屏蔽成员列表
//     *
//     * @return
//     */
//    @GET("channel/getBlockList")
//    suspend fun requestBlockList(@QueryMap map: MutableMap<String, String>): BaseResponse<ArrayList<UserInfoBean>>
//
//    /**
//     * 屏蔽用户
//     *
//     * @return
//     */
//    
//    @POST("channel/lockUser")
//    suspend fun requestLock(@Body requestBody:RequestBody): BaseResponse
//
//
//    /**
//     * 解除屏蔽用户
//     *
//     * @return
//     */
//    
//    @POST("channel/unlockUser")
//    suspend fun requestUnLock(@Body requestBody:RequestBody): BaseResponse
//
//
//    /**
//     * 编辑语聊房
//     *
//     * @return
//     */
//    
//    @PUT("channel/{id}")
//    suspend fun requestEditChannel(
//        @Path("id") id: String,
//        @Body requestBody:RequestBody
//    ): BaseResponse<EditChannelBean>
//
//    /**
//     * 进入语聊房
//     *
//     * @return
//     */
//    
//    @POST("channel/enter")
//    suspend fun requestEnterChannel(@Body requestBody:RequestBody): BaseResponse<ChannelBean>
//
//    /**
//     * 群成员列表
//     *
//     * @return
//     */
//    @GET("channel/member")
//    suspend fun requestChannelMember(@QueryMap map: MutableMap<String, String>): BaseResponse<LinkedList<MemberBean>>
//
//    /**
//     * 在线成员列表
//     *
//     * @return
//     */
//    @GET("channel/onlinemembers/{id}")
//    suspend fun requestChannelOnline(@Path("id") id: String): BaseResponse<OnlineMemberBean>
//
//
//    /**
//     * 曾经加入的群
//     *
//     * @return
//     */
//    @GET("channel/getHistoryList")
//    suspend fun requestChannelHistory(@QueryMap map: MutableMap<String, String>): BaseResponse<ArrayList<ChannelBean>>
//
//
//    /**
//     * 删除曾经加入的群
//     *
//     * @return
//     */
//    
//    @HTTP(method = "DELETE", path = "channel/deleteHistory", hasBody = true)
//    suspend fun requestDeleteChannel(@Body requestBody:RequestBody): BaseResponse
//
//    /**
//     * 好友正在玩
//     *
//     * @return
//     */
//    @GET("channel/getMixList")
//    suspend fun requestChannelFriend(@QueryMap map: MutableMap<String, String>): BaseResponse<ArrayList<ChannelBean>>
//
//    /**
//     * 在线用户
//     *
//     * @return
//     */
//    @GET("user/onlinefriends")
//    suspend fun requestChannelOnline(@QueryMap map: MutableMap<String, String>): BaseResponse<ArrayList<UserInfoBean>>
//
//    /**
//     * 邀请好友
//     *
//     * @return
//     */
//    @GET("user/relation/invitelist")
//    suspend fun requestInviteList(@QueryMap map: MutableMap<String, String>): BaseResponse<ArrayList<UserInfoBean>>
//
//
//    /**
//     * 搜索
//     *
//     * @return
//     */
//    @GET("user/searchuser")
//    suspend fun requestSearchUser(@QueryMap map: MutableMap<String, String>): BaseResponse<ArrayList<UserInfoBean>>
//
//    /**
//     * app内邀请
//     *
//     * @return
//     */
//    
//    @POST("user/relation/invite")
//    suspend fun requestInviteApp(@Body requestBody:RequestBody): BaseResponse<InviteCodeBean>
//
//    /**
//     * 微信邀请好友
//     *
//     * @return
//     */
//    
//    @POST("user/relation/inviteByThird")
//    suspend fun requestInviteWX(@Body requestBody:RequestBody): BaseResponse<WXInviteBean>
//
//
//    /**
//     * 未读消息
//     *
//     * @return
//     */
//    @GET("user/common/unread")
//    suspend fun requestUnRead(): BaseResponse<UnreadBean>
//
//
//    /**
//     * 获取app版本号
//     *
//     * @return
//     */
//    @GET("user/common/appVersion")
//    suspend fun getAppVersion(@QueryMap map: MutableMap<String, String>): BaseResponse<HeartUpdateBean>
//
//    /**
//     * 上报开麦时长
//     */
//    
//    @POST("channel/mic/stat")
//    suspend fun submitSpeakTime(@Body requestBody:RequestBody): BaseResponse
//
//    /**
//     * 开麦上报
//     */
//    
//    @POST("channel/mic/switch")
//    suspend fun submitOpenMicro(@Body requestBody:RequestBody): BaseResponse
//
//    /**
//     * 附近的频道
//     */
//    @GET("channel/getListByRelation")
//    suspend fun requestNearChannel(@QueryMap map: MutableMap<String, String>): BaseResponse<ArrayList<ChannelBean>>
//
//
//    /**
//     * 查询新手引导音频
//     */
//    @GET("user/common/tutorial/{version}")
//    suspend fun requestVoiceList(@Path("version") version: String): BaseResponse<ArrayList<VoiceBean>>
//
//
//    /**
//     * 查询新手引导用户
//     */
//    @GET("user/common/tutorial/users/{version}")
//    suspend fun requestVoiceUserList(@Path("version") version: String): BaseResponse<ArrayList<MemberBean>>
//
//    /**
//     * 附近频道
//     */
//    @GET("channel/getNearList")
//    suspend fun requestNearList(
//        @Query("cursor") cursor: String,
//        @Query("count") count: Int,
//        @Query("cityCode") cityCode: String
//    ): BaseResponse<ArrayList<ChannelBean>>
//
//    /**
//     * 推荐频道
//     */
//    @GET("channel/getRecommendList")
//    suspend fun requestRecommendList(
//        @Query("cursor") cursor: String,
//        @Query("count") count: Int,
//        @Query("cityCode") cityCode: String
//    ): BaseResponse<ArrayList<ChannelBean>>
//
//    /**
//     * 获取首次进入房间信息
//     */
//    @GET("channel/getRecommend")
//    suspend fun requestRecommend(): BaseResponse<ChannelBean>
//
//    /**
//     * 获取城市列表
//     */
//    @GET("user/common/cities")
//    suspend fun getCityList(): BaseResponse<ArrayList<ProvinceBean>>
//
//    /**
//     * 意见反馈
//     */
//    
//    @POST("user/common/feedback")
//    suspend fun requestFeedback(@Body requestBody:RequestBody): BaseResponse
//
//    /**
//     * 耳机状态上报
//     */
//    
//    @POST("stats/headset")
//    suspend fun requestHeadSet(@Body requestBody:RequestBody): BaseResponse
//
//    /**
//     * 生成邀请群聊暗号
//     */
//    @GET("share/invitecode")
//    suspend fun requestInviteCode(@Query("roomId") roomId: String): BaseResponse<String>
//
//    /**
//     * 查验邀请暗号是否正确
//     */
//    
//    @POST("share/checkcode")
//    suspend fun requestCheckCode(@Body requestBody:RequestBody): BaseResponse<CheckCodeBean>
//
//    /**
//     * 生成邀请好友暗号
//     */
//    @GET("share/inviteusercode")
//    suspend fun requestInviteFriend(): BaseResponse<String>
//
//    /**
//     * 发布留言板信息
//     */
//    
//    @POST("msgboard/create")
//    suspend fun requestPublish(@Body requestBody:RequestBody): BaseResponse
//
//    /**
//     * 用户当前周期已发布数量
//     */
//    @GET("msgboard/checkenable")
//    suspend fun requestCheckPublish(): BaseResponse<CheckPublishBean>
//
//    /**
//     * 留言板回复
//     */
//    @Multipart
//    @POST("msgboard/reply")
//    suspend fun requestReply(
//        @Part video: Part,
//        @Part msgId: Part,
//        @Part duration: Part,
//        @Part msg: Part
//    ): BaseResponse<StatusBean>
//
//    /**
//     * 留言板列表
//     */
//    @GET("msgboard/list")
//    suspend fun requestPublishList(@QueryMap map: MutableMap<String, String>): BaseResponse<ArrayList<PublishListBean>>
//
//    /**
//     * 查看已发布的留言板信息
//     */
//    @GET("msgboard/msgs")
//    suspend fun requestPublishedList(@QueryMap map: MutableMap<String, String>): BaseResponse<ArrayList<PublishedBean>>
//
//    /**
//     * 标记不想听
//     */
//    
//    @POST("msgboard/mark")
//    suspend fun requestMarkDelete(@Body requestBody:RequestBody): BaseResponse
//
//    /**
//     * 检测回复是否匹配
//     */
//    
//    @POST("msgboard/checkmatch")
//    suspend fun requestCheckMatch(@Body requestBody:RequestBody): BaseResponse
//
//
//    /**
//     * 留言板回复已读
//     */
//    
//    @POST("msgboard/reply/read")
//    suspend fun requestReplyRead(@Body requestBody:RequestBody): BaseResponse<StatusBean>
//
//    /**
//     * tab2 随机头像信息
//     */
//    @GET("user/avatars")
//    suspend fun requestAvatars(@QueryMap map: MutableMap<String, String>): BaseResponse<ArrayList<UserInfoBean>>
//
//    /**
//     * 是否接受打招呼
//     */
//    
//    @POST("private/accept")
//    suspend fun isAcceptSpeakHello(@Body requestBody:RequestBody): BaseResponse<RoomIdBean>
//
//
//    /**
//     * 检查是否能打招呼
//     */
//    @GET("private/greetingcheck")
//    suspend fun requestCheckGreet(@Query("targetUserId") targetUserId: String): BaseResponse
//
//    /**
//     * 打招呼
//     */
//    @Multipart
//    @POST("private/greeting")
//    suspend fun requestGreet(
//        @Part audio: Part,
//        @Part targetUserId: Part,
//        @Part duration: Part,
//        @Part content: Part
//    ): BaseResponse<GreetBean>
//
//    /**
//     * 进入私聊
//     */
//    
//    @POST("private/enter")
//    suspend fun requestEnterSingleChat(@Body requestBody:RequestBody): BaseResponse<ChannelBean>
//
//    /**
//     * 退出私聊
//     */
//    
//    @POST("private/out")
//    suspend fun requestOutSingleChat(@Body requestBody:RequestBody): BaseResponse
//
//    /**
//     * 在线成员列表
//     */
//    @GET("private/onlinedetails/{id}")
//    suspend fun requestSingleOnline(@Path("id") id: String): BaseResponse<ArrayList<MemberBean>>
//
//    /**
//     * 文字内容违规检查
//     */
//    
//    @POST("channel/textcheck")
//    suspend fun requestCheckText(@Body requestBody:RequestBody): BaseResponse<CheckTextBean>
//
//    /**
//     * 根据房间id查询房间详细信息
//     */
//    @GET("private/roomdetails/{id}")
//    suspend fun requestSingleChatInfo(@Path("id") id: String): BaseResponse<ChannelBean>
//
//    /**
//     * 招呼语音引导
//     */
//    @GET("tips/greeting")
//    suspend fun requestGreetTip(): BaseResponse<LinkedList<GreetTipBean>>
//
//    /**
//     * 匹配开关切换
//     */
//    
//    @POST("private/matchSwitch")
//    suspend fun requestMatchSwitch(@Body requestBody:RequestBody): BaseResponse
//
//    /**
//     * 上传音频信息
//     */
//    @Multipart
//    @POST("user/common/uploadAudio")
//    suspend fun requestUploadAudio(
//        @Part type: Part,
//        @Part audio: Part,
//        @Part duration: Part
//    ): BaseResponse<GreetBean>
//
//    /**
//     * 获取用户开关状态
//     */
//    @GET("user/switchStatus")
//    suspend fun requestSwitchStatus(@Query("name") name: String): BaseResponse<MatchStatusBean>
//
//
//    /**
//     * 查看盒子的状态
//     */
//    @GET("questionanswer/info")
//    suspend fun requestBoxInfo(): BaseResponse<BoxBean>
//
//
//    /**
//     * 编辑盒子
//     */
//    
//    @POST("questionanswer/edit")
//    suspend fun requestBoxEdit(@Body requestBody:RequestBody): BaseResponse<BoxBean>
//
//    /**
//     * 查看已回答和未回答/type=1 已回答 type=2 错过或者拒绝
//     */
//    @GET("questionanswer/historybytype")
//    suspend fun requestBoxHistory(@QueryMap map: MutableMap<String, String>): BaseResponse<HistoryBoxBean>
//
//    /**
//     * 查看盒子的状态
//     */
//    @GET("questionanswer/status/{id}")
//    suspend fun requestBoxStatus(@Path("id") id: String): BaseResponse
//
//    /**
//     * 是否接受问题
//     */
//    
//    @POST("questionanswer/acceptquestion")
//    suspend fun requestAcceptQuestion(@Body requestBody:RequestBody): BaseResponse
//
//    /**
//     * 新建回答
//     */
//    @Multipart
//    @POST("questionanswer/newanswer")
//    suspend fun requestNewAnswer(
//        @Part questionId: Part,
//        @Part audio: Part,
//        @Part duration: Part
//    ): BaseResponse<AnswerBean>
//
//    /**
//     * 分享盒子信息
//     */
//    @GET("questionanswer/share/{boxId}")
//    suspend fun requestShareBox(@Path("boxId") boxId: String): BaseResponse<ShareBean>
//
//    /**
//     * 查看问题是否过期
//     */
//    @GET("questionanswer/questionstatus")
//    suspend fun requestQuestionStatus(@Query("questionId") questionId: String): BaseResponse
//
//
//    /**
//     * 获取在线问题队列
//     */
//    @GET("questionanswer/nextlist/v2")
//    suspend fun requestQuestionList(): BaseResponse<QuestionListBean>
//
//    /**
//     * 获取超时标记已读
//     */
//    @POST("questionanswer/haveread")
//    suspend fun requestQuestionRead(): BaseResponse
//
//    /**
//     * 开屏列表
//     */
//    @GET("user/common/launchscreen")
//    suspend fun requestLaunchScreen(): BaseResponse<ArrayList<LaunchBean>>
//
//    /**
//     * 开屏列表
//     */
//    @GET("user/common/getConfigByName")
//    suspend fun requestConfigByName(@Query("name") name: String): BaseResponse<LabelBean>
//
//    /**
//     * 前置检测
//     */
//    @GET("channel/preCheck")
//    suspend fun requestPreCheck(@Query("action") action: String): BaseResponse
//
//    /**
//     * 邀请
//     */
//    
//    @POST("channel/invite")
//    suspend fun requestInvite(@Body requestBody:RequestBody): BaseResponse
//
//    /**
//     * 二维码Url参数解析
//     */
//    @GET("user/common/getShareUrl")
//    suspend fun requestShareUrl(@QueryMap map: MutableMap<String, String>): BaseResponse<String>
//
//
//    /**
//     * 申请提现
//     */
//    @GET("cash/apply")
//    suspend fun requestApply(): BaseResponse<ApplyRequestBean>
//
//
//    /**
//     * 绑定用户微信名创
//     */
//    
//    @POST("user/addwxaccount")
//    suspend fun requestAddWXAccount(@Body requestBody:RequestBody): BaseResponse
//
//
//    /**
//     * 提现记录
//     */
//    @GET("cash/list")
//    suspend fun requestWithdrawList(@QueryMap map: MutableMap<String, String>): BaseResponse<ArrayList<WithdrawBean>>
//
//
//    /**
//     * 单条提现记录详情
//     */
//    @GET("cash/details/{tradeId}")
//    suspend fun requestWithdrawDetail(@Path("tradeId") tradeId: String): BaseResponse<ArrayList<WithdrawBean>>
//
//
//    /**
//     * 查询微信绑定信息
//     */
//    @GET("user/wxaccount")
//    suspend fun requestWxAccount(): BaseResponse<WXAccountBindBean>
//
//
//    /**
//     * 账户直说币余额相关
//     */
//    @GET("cash/account")
//    suspend fun requestAccount(): BaseResponse<AccountBean>
//
//
//    /**
//     * 收益历史/我收到的
//     */
//    @GET("cash/receivedlist")
//    suspend fun requestReceived(@QueryMap map: MutableMap<String, String>): BaseResponse<LinkedList<ReceivedBean>>
//
//
//    /**
//     * 消费历史
//     */
//    @GET("cash/consumelist")
//    suspend fun requestConsume(@QueryMap map: MutableMap<String, String>): BaseResponse<ArrayList<ConsumeBean>>
//
//
//    /**
//     * 充值商品列表
//     */
//    @GET("shop/goodsList")
//    suspend fun requestGoodsList(@Query("type") type: String): BaseResponse<ArrayList<GoodBean>>
//
//
//    /**
//     * 礼物列表
//     */
//    @GET("gift/list")
//    suspend fun requestGiftList(): BaseResponse<ArrayList<GiftBean>>
//
//
//    /**
//     * 打赏礼物
//     */
//    
//    @POST("gift/give")
//    suspend fun requestGive(@Body requestBody:RequestBody): BaseResponse
//
//
//    /**
//     * 账户余额(钻石)
//     */
//    @GET("user/account")
//    suspend fun requestDiamond(): BaseResponse<DiamondBean>
//
//    /**
//     * 礼物人气排行
//     */
//    @GET("gift/hotRank")
//    suspend fun requestHotRank(): BaseResponse<ArrayList<RankBean>>
//
//    /**
//     * 礼物土豪排行
//     */
//    @GET("gift/richRank")
//    suspend fun requestRichRank(): BaseResponse<ArrayList<RankBean>>
//
//    /**
//     * 上周榜首
//     */
//    @GET("gift/topRank")
//    suspend fun requestTopRank(): BaseResponse<TopRankBean>
//
//    /**
//     * 购买钻石
//     */
//    @POST("shop/order")
//    suspend fun requestOrder(@Body requestBody:RequestBody): BaseResponse<OrderBean>
//
//    /**
//     * 微信支付
//     */
//    
//    @POST("shop/pay/wx")
//    suspend fun requestPayWX(@Body requestBody:RequestBody): BaseResponse<PrepayBean>
//
//    /**
//     * 支付宝支付
//     */
//    
//    @POST("shop/pay/ali")
//    suspend fun requestPayZFB(@Body requestBody:RequestBody): BaseResponse<String>
//
//    /**
//     * 订单查询
//     */
//    @GET("shop/order/{orderId}")
//    suspend fun requestOrderInfo(@Path("orderId") orderId: String): BaseResponse<OrderBean>
//
//
//    /**
//     * 实名认证
//     */
//    @Multipart
//    @POST("roleapply/partner")
//    suspend fun requestApplyAuth(
//        @Part frontPic: Part,
//        @Part backPic: Part,
//        @Part userPic: Part,
//        @Part name: Part,
//        @Part idCode: Part,
//        @Part contactCode: Part
//    ): BaseResponse<String>
//
//    /**
//     * 任务进度
//     */
//    @GET("roleapply/applydetails")
//    suspend fun requestTask(@Query("type") type: String): BaseResponse<ArrayList<TaskDetailBean>>
//
//    /**
//     * 申请状态查询
//     */
//    @GET("roleapply/applystatus")
//    suspend fun requestApplyStatus(@Query("type") type: String): BaseResponse<ApplyStatusBean>
//
//    /**
//     * 小B信息
//     */
//    @GET("host/info/{hostId}")
//    suspend fun requestHostInfo(@Path("hostId") hostId: String): BaseResponse<UserInfoBean>
//
//    /**
//     * 开始匹配
//     */
//    
//    @POST("match/join")
//    suspend fun requestStartMatch(@Body requestBody:RequestBody): BaseResponse<SortBean>
//
//    /**
//     * 终止匹配
//     */
//    
//    @POST("match/out")
//    suspend fun requestStopMatch(@Body requestBody:RequestBody): BaseResponse<String>
//
//    /**
//     * 开始聊天
//     */
//    
//    @POST("chat/start")
//    suspend fun requestStartChat(@Body requestBody:RequestBody): BaseResponse<ChatTimeBean>
//
//    /**
//     * 挂断聊天
//     */
//    
//    @POST("chat/hangUp")
//    suspend fun requestStopChat(@Body requestBody:RequestBody): BaseResponse<TotalTimeBean>
//
//    /**
//     * 兑换时间
//     */
//    
//    @POST("shop/exchange")
//    suspend fun requestConvertTime(@Body requestBody:RequestBody): BaseResponse<OrderBean>
//
//    /**
//     * 点亮爱心
//     */
//    
//    @POST("chat/support")
//    suspend fun requestChatSupport(@Body requestBody:RequestBody): BaseResponse<ChatTimeBean>
//
//    /**
//     * 评价
//     */
//    
//    @POST("chat/review")
//    suspend fun requestChatReview(@Body requestBody:RequestBody): BaseResponse<ChatTimeBean>
//
//    /**
//     * 服务器时间
//     */
//    
//    @POST("chat/servertime")
//    suspend fun requestServerTime(@Body requestBody:RequestBody): BaseResponse<ChatTimeBean>
//
//    /**
//     * 时间消费历史
//     */
//    @GET("chat/consumelist")
//    suspend fun requestTimeConsume(@QueryMap map: MutableMap<String, String>): BaseResponse<ArrayList<ConsumeTimeBean>>
//
//    /**
//     * 每日签到
//     */
//    @GET("activity/checkin")
//    suspend fun requestCheckIn(): BaseResponse<CheckInBean>
//
//    /**
//     * 查询用户当前的配置信息
//     */
//    @GET("host/settinginfo")
//    suspend fun requestSettingInfo(): BaseResponse<SettingInfoBean>
//
//    /**
//     * 修改接单配置
//     */
//    
//    @POST("host/settings")
//    suspend fun requestSetting(@Body requestBody:RequestBody): BaseResponse
//
//    /**
//     * app开始状态
//     */
//    
//    @POST("match/appStatus")
//    suspend fun requestAppStatus(@Body requestBody:RequestBody): BaseResponse
//
//    /**
//     * 首页热聊
//     */
//    @GET("chat/hotchat")
//    suspend fun requestChatHot(@QueryMap map: MutableMap<String, String>): BaseResponse<ArrayList<HotChatBean>>
//
//    /**
//     * 首页陪伴主播列表
//     */
//    @GET("chat/onlinehost")
//    suspend fun requestOnlineHost(@QueryMap map: MutableMap<String, String>): BaseResponse<ArrayList<OnlineHostBean>>
//
//    /**
//     * banner列表
//     */
//    @GET("banner/list")
//    suspend fun requestBanner(): BaseResponse<ArrayList<BannerBean>>
//
//    /**
//     * 通话记录
//     */
//    @GET("chat/chatlist")
//    suspend fun requestChatList(@QueryMap map: MutableMap<String, String>): BaseResponse<ArrayList<CallBean>>
//
//    /**
//     * 用户留存
//     */
//    @GET("stats/retention")
//    suspend fun requestRetention(@Query("day") day: String): BaseResponse<RetentionBean>
//
//    /**
//     * 主播状态是否在接单中
//     */
//    @GET("host/status")
//    suspend fun requestAutoMatch(@Query("name") name: String): BaseResponse<AutoMatchBean>
//
//    /**
//     * 订阅匹配开始提醒
//     */
//    
//    @POST("match/setremind")
//    suspend fun requestMatchRemind(@Body requestBody:RequestBody): BaseResponse

}