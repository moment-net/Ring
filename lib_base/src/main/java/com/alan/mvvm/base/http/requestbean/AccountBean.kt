package com.alan.mvvm.base.http.requestbean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AccountBean(
    var assetsCount: Double = 0.0,
    var cashCount: Double = 0.0,
    var dutyCount: Double = 0.0,
    var cashReal: Double = 0.0,
    var wxAccountName: String? = null,
) : Parcelable