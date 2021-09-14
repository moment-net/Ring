package com.alan.mvvm.common.constant

import com.alan.mvvm.base.utils.StorageUtil

object Constants {
    //微信 APP_ID
    const val APP_ID: String = "wx723c4def2d7abe1f";
    const val APP_SECRET = "a7f2375225078560e757875cf3c6b86d"
    const val APP_SNSAPI = "snsapi_userinfo"

    //手机号一键登录appid
    const val ONELOGIN_APP_ID: String = "ffdde7182d61db6a28285978e037ae46"

    //声网
    const val AGORA_ID = "aef85e061fc048658a23a96860a26718"
//const val AGORA_ID = "d90373e913834741b538844be09bcc2d"

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


    //跳转拍照页面返回类型
    const val TYPE_IMAGE = 11
    const val TYPE_VIDEO = 12
}