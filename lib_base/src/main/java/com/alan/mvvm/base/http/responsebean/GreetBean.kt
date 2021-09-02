package com.alan.mvvm.base.http.responsebean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GreetBean(
    var duration: Int = 0,
    val audioName: String = "",
    val audioPath: String? = "",
) : Parcelable
