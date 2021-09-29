package com.alan.mvvm.base.http.responsebean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GoodBean(
    var id: String? = null,
    val goodsId: String? = null,
    val name: String? = null,
    val type: String? = null,
    val amount: Float = 0f,
    val point: Int = 0,
    val detail: String? = null,
    val date: String? = null,
    val status: String? = null,
    val num: String? = null,
    val activity: ActivityClass? = null
) : Parcelable

@Parcelize
data class ActivityClass(
    var original_amount: Int = 0,
    val name: String? = null
) : Parcelable