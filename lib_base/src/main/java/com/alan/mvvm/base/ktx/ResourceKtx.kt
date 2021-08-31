package com.alan.mvvm.base.ktx

import androidx.core.content.ContextCompat
import com.alan.mvvm.base.BaseApplication

fun Int.getResDimen() = BaseApplication.mContext.resources.getDimension(this)

fun Int.getResDrawable() = ContextCompat.getDrawable(BaseApplication.mContext, this)

fun Int.getResColor() = ContextCompat.getColor(BaseApplication.mContext, this)
