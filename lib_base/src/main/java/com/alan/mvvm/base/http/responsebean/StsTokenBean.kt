package com.alan.mvvm.base.http.responsebean

data class StsTokenBean(
    var accessKeyId: String,
    var accessKeySecret: String,
    var bucketDomain: String,
    var bucketName: String,
    var endPoint: String,
    var expiration: String,
    var securityToken: String,
    var serverTime: String
)