package com.alan.mvvm.base.http.responsebean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CookerBean(
    val createTime: String,
    val likes: List<String>,
    val mealId: String,
    val tag: List<String>,
    val title: String,
    val user: UserInfoBean
) : Parcelable
