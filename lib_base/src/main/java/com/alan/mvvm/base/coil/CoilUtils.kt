package com.alan.mvvm.base.coil

import android.content.Context
import android.widget.ImageView
import androidx.annotation.ColorInt
import coil.Coil
import coil.ImageLoader
import coil.load
import coil.request.CachePolicy
import coil.transform.BlurTransformation
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import coil.util.CoilUtils
import com.alan.mvvm.base.BaseApplication
import com.alan.mvvm.base.coil.transformation.CircleCropBorderTransformation
import com.scwang.smart.refresh.layout.util.SmartUtil.dp2px
import okhttp3.OkHttpClient

object CoilUtils {
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

    /**
     * 加载图片
     */
    fun load(
        iv: ImageView,
        url: String
    ) {
        iv.load(url) {}
    }

    /**
     * 加载图片高斯模糊
     */
    fun loadBlur(
        iv: ImageView,
        url: String,
        context: Context,
        radius: Float,
        sampling: Float
    ) {
        iv.load(url) {
            transformations(
                BlurTransformation(context, radius, sampling)
            )
        }
    }

    /**
     * 加载圆形图片
     */
    fun loadCircle(
        iv: ImageView,
        url: String
    ) {
        iv.load(url) {
            transformations(
                CircleCropTransformation()
            )
        }
    }

    /**
     * 加载圆角带边框图片
     */
    fun loadRound(
        iv: ImageView,
        url: String,
        roundRadius: Float
    ) {
        iv.load(url) {
            transformations(
                RoundedCornersTransformation(
                    dp2px(roundRadius).toFloat(),
                ),
            )
        }
    }

    /**
     * 加载圆角带边框图片
     */
    fun loadRoundBorder(
        iv: ImageView,
        url: String,
        roundRadius: Float,
        borderWidth: Float,
        @ColorInt borderColor: Int
    ) {
        iv.load(url) {
            transformations(
                CircleCropBorderTransformation(
                    dp2px(roundRadius).toFloat(),
                    dp2px(borderWidth).toFloat(),
                    borderColor
                ),
            )
        }
    }
}