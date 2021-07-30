package com.alan.mvvm.common.constant

/**
 * 作者：alan
 * 时间：2021/7/28
 * 备注：路由地址
 */
object RouteUrl {
    object MainModule {
        //首页
        const val RING_ACTIVITY_MAIN: String = "/main/main";

        //登录
        const val RING_ACTIVITY_LOGIN: String = "/main/login";

        //登陆手机号
        const val RING_ACTIVITY_PHONE: String = "/main/phone"

        //登陆获取验证码
        const val RING_ACTIVITY_CODE: String = "/main/code"

        //登陆微信信息
        const val RING_ACTIVITY_WXINFO: String = "/main/wx"

    }

    object MyModule {
        const val RING_ACTIVITY_MY: String = "/my/my";
    }
}