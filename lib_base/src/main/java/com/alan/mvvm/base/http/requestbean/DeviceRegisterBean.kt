package com.alan.mvvm.base.http.requestbean

data class DeviceRegisterBean(
    var deviceType: String? = null,
    val deviceUuid: String? = null,
    val deviceBrand: String? = null,
    val deviceToken: String? = null
)
