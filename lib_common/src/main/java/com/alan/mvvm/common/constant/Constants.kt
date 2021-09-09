package com.alan.mvvm.common.constant

import com.alan.mvvm.base.utils.StorageUtil

object Constants {
    //微信 APP_ID
    const val APP_ID: String = "wx723c4def2d7abe1f";
    const val APP_SECRET = "a7f2375225078560e757875cf3c6b86d"
    const val APP_SNSAPI = "snsapi_userinfo"

    //手机号一键登录appid
    const val ONELOGIN_APP_ID: String = "ffdde7182d61db6a28285978e037ae46"

    //自我介绍招呼音频
    var PATH_GREET_SELF = "${StorageUtil.getExternalFileDir()}/greet_self.wav"


    //跳转拍照页面返回类型
    const val TYPE_IMAGE = 11
    const val TYPE_VIDEO = 12
}