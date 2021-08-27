package com.alan.mvvm.base.http.requestbean

data class LoginThirdRequestBean(
    var oauthAccount: String = "",
    var oauthType: Int = 0,
    var unionId: String = "",
    var portrait: String = "",
    var nickname: String = "",
    var gender: Int = 0,
    var oauthToken: String = "",
    var installParam: String = "",
    var encryptData: EncryptDataBean?
)


