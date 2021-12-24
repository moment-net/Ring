package com.alan.mvvm.base.http.responsebean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChatBgBean(
    var name: String,
    var desc: String,
    var selected: Boolean = false,
    var url: String
) : Parcelable