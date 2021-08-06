package com.alan.mvvm.base.coil

import android.content.Context
import android.widget.ImageView
import androidx.annotation.ColorInt
import coil.Coil
import coil.ImageLoader
import coil.load
import coil.request.CachePolicy
import coil.util.CoilUtils
import com.alan.mvvm.base.BaseApplication
import okhttp3.OkHttpClient

object CoilUtil {

    /**
     *
     */
    fun initCoil() {
        val imageLoader = ImageLoader.Builder(BaseApplication.mContext)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .crossfade(true)
            .okHttpClient {
                OkHttpClient.Builder()
                    .cache(CoilUtils.createDefaultCache(BaseApplication.mContext))
                    .build()
            }
            .build()
        Coil.setImageLoader(imageLoader)
    }


    fun loadRoundBorder(
        context: Context,
        url: String,
        iv: ImageView,
        roundRadius: Float,
        borderWidth: Float,
        @ColorInt borderColor: Int
    ) {
        iv.load(url) {
            transformations(
                CircleCropBorderTransformation(
                    roundRadius,
                    roundRadius,
                    roundRadius,
                    roundRadius,
                    borderWidth,
                    borderColor
                ),
            )
        }
    }
}