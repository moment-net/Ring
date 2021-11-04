package com.alan.mvvm.common.report

object ReportConstant {
    //数据上报apikey
    const val AMPLITUDE_APPID: String = "4749d72c8e84913ea1a6bbaf59cab191";

    //--------------------设备信息相关key----------------------------------------------
    var KEY_DEVICE_ID = "Device_ID"
    var KEY_DEVICE_MODEL = "Device_Model"
    var KEY_SYSTEM_VERSION = "System_Version"
    var KEY_APP_VERSION = "App_Version"
    var KEY_IMEI = "IMEI"

    //--------------------点击事件key----------------------------------------------
    //1、选择登录方式
    var EVENT_LOGIN = "Login_Button"

    //2、点击获取验证码
    var EVENT_GETCODE = "Click_Get_VerifyCode"

    //3、点击重新发送
    var EVENT_RESENTCODE = "Click_Resent_VerifyCode"

    //4、点击绑定手机号返回按钮 （包含滑动返回）
    var EVENT_BIND_PHONE_BACK = "Click_ConnectPhonePage_Back"

    //5、点击手机号自动登录返回按钮（包含滑动返回）
    var EVENT_PHONE_BACK = "Click_AutoPhoneLoginPage_Back"

    //6、点击手机号登陆返回按钮（包含滑动返回）
    var EVENT_LOGIN_PHONE_BACK = "Click_PhoneLoginPage_Back"

    //7、输入验证码点击继续
    var EVENT_CODE_NEXT = "Click_VerifyCode_Next"

    //8、点击补充资料下一步
    var EVENT_PROFILE_NEXT = "Click_Fill_Profile_Next"

    //9、注册成功
    var EVENT_REGISTER_SUCCESS = "Signin_Success"

    //10、点击首页tab
    var EVENT_TAB_HOME = "Click_Home_Tab"

    //11、点击聊天tab
    var EVENT_TAB_CHAT = "Click_Chat_Tab"

    //12、点击我的tab
    var EVENT_TAB_MY = "Click_My_Tab"

    //13、点击左上角我的头像
    var EVENT_HOME_MY = "Click_My_Button"

    //14、点击快速匹配按钮
    var EVENT_HOME_MATCH = "Click_Pair_Button"

    //15、点击正在Tab
    var EVENT_HOME_TAB_NOW = "Click_Now_Tab"

    //16、点击想法Tab
    var EVENT_HOME_TAB_THINK = "Click_Idea_Tab"

    //17、点击首页正在卡片
    var EVENT_NOW_CARD = "Click_Now_Card"

    //18、点击首页想法卡片
    var EVENT_THINK_CARD = "Click_IdeaCard"

    //19、点击一起
    var EVENT_HOME_TOGETHER = "Click_together"

    //20、点击喜欢
    var EVENT_HOME_LIKE = "Click_Like"

    //21、点击卡片左上角三个点
    var EVENT_HOME_MENU = "Click_Menu"

    //22、点击举报按钮
    var EVENT_HOME_REPORT = "Click_Report"

    //23、点击屏蔽按钮
    var EVENT_HOME_BLOCK = "Click_Block"

    //24、点击详情中的聊天
    var EVENT_DETAIL_CHAT = "Click_ChatButton"

    //25、点击首页发布按钮
    var EVENT_HOME_PUBLISH = "Click_Create_Button"

    //26、点击发布弹窗 - 正在
    var EVENT_PUBLISH_NOW = "Click_Create_Now"

    //27、点击发布弹窗 - 想法
    var EVENT_PUBLISH_THINK = "Click_Create_Idea"

    //28、点击底部礼物按钮  （iOS）
    var EVENT_CHAT_GIFT = "Click_bottomSend_Gift"

    //29、点击赠送
    var EVENT_CHAT_GIVE = "Click_SendButton"

    //30、点击我的钻石
    var EVENT_MY_DIAMOND = "Click_My_Diamond"

    //31、点击充值钻石
    var EVENT_DIAMOND_RECHARGE = "Click_BuyDiamond"

    //32、点击取消购买按钮
    var EVENT_DIAMOND_CANCEL = "Click_CancelAppleIAP"

    //33、点击充值钻石页返回按钮
    var EVENT_DIAMOND_BACK = "Click_My_Diamond_BackButton"

    //34、点击语音通话按钮
    var EVENT_CHAT_CALL = "Click_Callout"

    //35、点击挂断按钮
    var EVENT_CHAT_HANGUP = "Click_Hangup"

    //36、点击接通按钮
    var EVENT_CHAT_CONNECT = "Click_Connect"

    //37、点击最小化
    var EVENT_CALL_SCALE = "Click_Call_Background"

    //38、点击通话页面关注
    var EVENT_CHAT_FOLLOW = "Click_CallPage_Follow"

    //39、点击通话页面送礼物
    var EVENT_SEND_GIFT = "Click_bottomSend_Gift"

    //40、点击弹窗 - 立即聊天
    var EVENT_WINDOW_CHAT = "Click_window_ChatButton"

    //41、点击补充资料状态 - 下一步按钮
    var EVENT_CLICK_SELECTSTATUS = "Click_SelectStatus"

    //42、点击补充资料状态  - 跳过按钮
    var EVENT_CLICK_SKIP_SLEECTSTATUS = "Click_Skip_SelectStatus"

    //43、点击banner -
    var EVENT_CLICK_BANNER = "Click_Banner"

    //44、点击Ring -
    var EVENT_CLICK_RING = "Click_RingCard"

    //45、点击快捷匹配按钮
    var EVENT_CLICK_FASTMATCH = "Click_QucikPairButton"

    //46、点击Ring 弹窗关闭匹配
    var EVENT_CLICK_STOPMATCH = "Click_RingWindow_StopPair"

    //47、点击Ring 弹窗保持开启
    var EVENT_CLICK_STARTMATCH = "Click_RingWindow_StillPair"

    //48、点击弹窗 - 立即聊天
    var EVENT_CLICK_MATCH_CHAT = "Click_window_ChatButton"

    //49、点击我的 - 添加社交卡片
    var EVENT_CLICK_ADDCARD = "Click_Add_MySocialCard"

    //50、点击完善社交卡片类型
    var EVENT_CLICK_CARDTYPE = "Click_MySocialCardType"

    //51、点击查看对方卡片
    var EVENT_CLICK_OTHERCARD = "Click_FriendsSocialCard"

    //52、点击完善自己的卡片信息才可以查看他人卡片信息弹窗按钮
    var EVENT_CLICK_CARD_COMMIT = "Click_ProfileJustice_Window"
    //--------------------点击事件Value----------------------------------------------

    //时间
    var KEY_TIME = "Time"

    //用户ID
    var KEY_USERID = "User_ID"

    //登录类型
    var KEY_LOGIN_TYPE = "Login_Type"

    //登录类型 - 手机号
    var VALUE_PHONE = "Phone"

    //登录类型 - 手机号一键登录
    var VALUE_AUTOPHONE = "Auto_Phone"

    //登录类型 - 微信
    var VALUE_WECHAT = "Wechat"

    //验证码状态
    var KEY_STATUS = "Status"

    //验证码状态-正确
    var VALUE_FALSE = "FALSE"

    //验证码状态-错误
    var VALUE_TRUE = "TRUE"

    //朋友
    var KEY_FRIENDID = "Friends_ID"

    //朋友在线状态
    var KEY_FRIEND_STATUS = "Friends_Status_Type"

    //在线
    var VALUE_ONLINE = "Online"

    //离线
    var VALUE_OFFLINE = "Offline"

    //商品
    var KEY_PRODUCTID = "ProductID"

    //通话记录id
    var KEY_SESSIONID = "SessionId"

    //接听状态
    var KEY_CALLSTATUS = "CallStatus"

    //接听状态 - 已接通
    var VALUE_SUCCESS = "Success"

    //接听状态 - 对方拒绝
    var VALUE_REFUSE = "Refuse"

    //接听状态 - 占线/忙碌中
    var VALUE_BUSY = "Busy"


    //呼叫等待时长
    var KEY_WAITTIME = "WaitCalltime"

    //通话时间
    var KEY_CALLTIME = "Calltime"


    //匹配用户ID
    var KEY_PAIRID = "PairUser_ID"

    //类型
    var KEY_CHATTYPE = "ChatType"

    //类型-文字
    var VALUE_TEXT = "Text"

    //类型-语音
    var VALUE_VOICE = "Voice"

    //ID
    var KEY_ID = "ID"

    //BannerID
    var KEY_BANNERID = "BannerID"

    //匹配状态
    var KEY_PAIRTYPE = "PairType"

    //匹配状态-开启
    var VALUE_START = "StartPair"

    //匹配状态-关闭
    var VALUE_STOP = "StopPair"


    //匹配类型ID
    var KEY_PAIRTYPEID = "PairTypeID"

    //CardTypeID
    var KEY_CARDTYPEID = "CardTypeID"


}