package com.alan.mvvm.base.http.responsebean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AppearanceBean(
    val buttton: String,
    val avatar: String,
    val name: String,
    val url: String
) : Parcelable