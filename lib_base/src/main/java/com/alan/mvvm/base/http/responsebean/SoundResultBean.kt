package com.alan.mvvm.base.http.responsebean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SoundResultBean(
    var fileName: String,
    var fileUrl: String,
    var attribute: String,
    var highlySimilar: String,
    var similarity: ArrayList<SimilarityBean>,
) : Parcelable

@Parcelize
data class SimilarityBean(
    var sims: Double,
    var userName: String,
) : Parcelable
