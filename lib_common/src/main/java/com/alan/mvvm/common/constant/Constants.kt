package com.alan.mvvm.common.constant

import com.alan.mvvm.base.utils.StorageUtil
import com.alan.mvvm.common.BuildConfig

object Constants {
    //微信 APP_ID
    const val APP_ID: String = "wx723c4def2d7abe1f";
    const val APP_SECRET = "a7f2375225078560e757875cf3c6b86d"
    const val APP_SNSAPI = "snsapi_userinfo"

    //手机号一键登录appid
    const val ONELOGIN_APP_ID: String = "ffdde7182d61db6a28285978e037ae46"

    //声网
    var AGORA_ID = if ((BuildConfig.DEBUG)) {
        //测试
        "aef85e061fc048658a23a96860a26718"
    } else {
        //正式
        "4a804efb062a4e249b4dacd8a9e370ea"
    }

    //BuglyId
    const val BUGLYID_RELEASE: String = "8005da8385";
    const val BUGLYID_DEBUG: String = "26a0ea814f";


    const val HW_APPID = "104736151"
    const val HW_APPSECRET = "88015c60c330fcc92ddfb595a11b9063a110aad618f1bca6643430ea7c8e7115"
    const val MI_APPID = "2882303761520047113"
    const val MI_APPKEY = "5802004721113"
    const val MI_APPSECRET = "dtoBGwtSH8CYUZIvwr1BOw=="
    const val OPPO_APPID = "a7f2375225078560e757875cf3c6b86d"
    const val OPPO_APPKEY = "5802004721113"
    const val OPPO_APPSECRET = "a7f2375225078560e757875cf3c6b86d"
    const val VIVO_APPID = "105510561"
    const val VIVO_APPSECRET = "a7f2375225078560e757875cf3c6b86d"

    //自我介绍招呼音频
    var PATH_GREET_SELF = "${StorageUtil.getExternalFileDir()}/greet_self.wav"
    var PATH_SOUND_TEST = "sound_test"

    //跳转拍照页面返回类型
    const val TYPE_IMAGE = 11
    const val TYPE_VIDEO = 12
}