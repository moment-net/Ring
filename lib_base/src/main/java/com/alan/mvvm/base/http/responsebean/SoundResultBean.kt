package com.alan.mvvm.base.http.responsebean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SoundResultBean(
    var fileName: String,
    var fileUrl: String,
    var attribute: String,
    var downloadQr: String,
    var highlySimilar: String,
    var similarity: ArrayList<SimilarityBean>,
) : Parcelable

@Parcelize
data class SimilarityBean(
    var sims: Double,
    var name: String,
    var voiceType: String,
) : Parcelable
