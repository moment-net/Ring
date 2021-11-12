package com.alan.mvvm.common.constant

/**
 * 作者：alan
 * 时间：2021/7/28
 * 备注：路由地址
 */
object RouteUrl {
    object MainModule {
        //首页
        const val ACTIVITY_MAIN_MAIN: String = "/main/main";

        //登录
        const val ACTIVITY_MAIN_LOGIN: String = "/main/login";

        //登陆手机号
        const val ACTIVITY_MAIN_PHONE: String = "/main/phone"

        //登陆获取验证码
        const val ACTIVITY_MAIN_CODE: String = "/main/code"

        //登陆微信信息
        const val ACTIVITY_MAIN_WXINFO: String = "/main/wx"

        //他的提醒
        const val ACTIVITY_MAIN_TAREMIND: String = "/main/taremind"

        //我的提醒
        const val ACTIVITY_MAIN_MYREMIND: String = "/main/myremind"

        //选择标签喜好
        const val ACTIVITY_MAIN_TARGET: String = "/main/target"

        //选择标签类型
        const val ACTIVITY_MAIN_TYPE: String = "/main/type"

        //选择状态标签
        const val ACTIVITY_MAIN_STATE: String = "/main/state"

        //发布想法
        const val ACTIVITY_MAIN_THINK: String = "/main/think"

        //发布Now
        const val ACTIVITY_MAIN_NOW: String = "/main/now"

        //我的页面
        const val ACTIVITY_MAIN_MY: String = "/main/my"

        //图片展示页面
        const val ACTIVITY_MAIN_PREVIEW: String = "/main/preview";
    }

    object HomeModule {
        //更换铃声
        const val ACTIVITY_HOME_RING: String = "/home/ring";

        //设置提醒
        const val ACTIVITY_HOME_REMIND: String = "/home/remind";
    }

    object ChatModule {
        //聊天页面
        const val ACTIVITY_CHAT_DETAIL: String = "/chat/detail";

        //显示图片
        const val ACTIVITY_CHAT_IMAGE: String = "/chat/image";

        //显示视频
        const val ACTIVITY_CHAT_VIDEO: String = "/chat/video";

        //录制视频或图片
        const val ACTIVITY_CHAT_CAMERA: String = "/chat/camera";
    }

    object CallModule {
        //视频通话页面
        const val ACTIVITY_CALL_CALL: String = "/call/call";
    }

    object MyModule {

        //设置页面
        const val ACTIVITY_MY_SET: String = "/my/set";

        //消息页面
        const val ACTIVITY_MY_MSG: String = "/my/msg";

        //系统消息页面
        const val ACTIVITY_MY_SYSTEMMSG: String = "/my/systemmsg";

        //个人信息页面
        const val ACTIVITY_MY_PERSONINFO: String = "/my/personinfo";

        //关注页面
        const val ACTIVITY_MY_FOCUS: String = "/my/focus";

        //我的钻石
        const val ACTIVITY_MY_DIAMOND: String = "/my/diamond";

        //我的账单
        const val ACTIVITY_MY_BILL: String = "/my/bill";

        //我的钱包
        const val ACTIVITY_MY_WALLET: String = "/my/wallet";

        //消费记录
        const val ACTIVITY_MY_PAYRECORD: String = "/my/payrecord";

        //支付结果
        const val ACTIVITY_MY_PAYRESULT: String = "/my/payresult";

        //提现
        const val ACTIVITY_MY_WITHDRAW: String = "/my/withdraw";

        //提现详情
        const val ACTIVITY_MY_WITHDRAWDETAIL: String = "/my/withdrawdetail";

        //账号与安全
        const val ACTIVITY_MY_SAFE: String = "/my/safe";

        //选择类型卡片
        const val ACTIVITY_MY_CARD: String = "/my/card";

        //卡片
        const val ACTIVITY_MY_CARDSET: String = "/my/cardset";

        //管家详情页面
        const val ACTIVITY_MY_MANAGER: String = "/my/manager";
    }

    object WebModule {
        //WEB浏览器页面
        const val ACTIVITY_WEB_WEB: String = "/web/web";
    }

    object JsonModule {
        //json服务
        const val ACTIVITY_JSON_JSON: String = "/json/json";
    }
}