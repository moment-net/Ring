package com.alan.mvvm.base.http.responsebean

data class MatchStatusBean(
    val inMatch: Boolean,
    val isFollowed: Boolean,
    val orderStatus: String
)