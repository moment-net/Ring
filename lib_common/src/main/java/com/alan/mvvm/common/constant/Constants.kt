package com.alan.mvvm.common.constant

import com.alan.mvvm.base.utils.StorageUtil

object Constants {
    //微信 APP_ID
    const val APP_ID: String = "wx026d8f45bef20c99";
    const val APP_SECRET = "ece2c5623787de11acbdb04f5d72a76b"
    const val APP_SNSAPI = "snsapi_userinfo"

    //手机号一键登录appid
    const val ONELOGIN_APP_ID: String = "ffdde7182d61db6a28285978e037ae46"

    //自我介绍招呼音频
    var PATH_GREET_SELF = "${StorageUtil.getExternalFileDir()}/greet_self.wav"
}