package com.alan.mvvm.base.http.responsebean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GiftBean(
    var giftId: String? = null,
    var name: String? = null,
    var icon: String? = null,
    var price: Int = 0,
    var weights: Int = 0,
    var state: Int = 0,
    var effect: String? = null,
    var sound: String? = null,
) : Parcelable
