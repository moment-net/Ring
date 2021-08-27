package com.alan.mvvm.base.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build.VERSION.SDK_INT
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.imageLoader
import coil.load
import coil.request.ImageRequest
import com.huantansheng.easyphotos.engine.ImageEngine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object CoilEngine : ImageEngine {

    /**
     * 加载图片到ImageView
     *
     * @param context   上下文
     * @param uri 图片路径Uri
     * @param imageView 加载到的ImageView
     */
    //安卓10推荐uri，并且path的方式不再可用
    override fun loadPhoto(context: Context, uri: Uri, imageView: ImageView) {
        imageView.load(uri) {}
    }

    /**
     * 加载gif动图图片到ImageView，gif动图不动
     *
     * @param context   上下文
     * @param gifUri   gif动图路径Uri
     * @param imageView 加载到的ImageView
     *                  <p>
     *                  备注：不支持动图显示的情况下可以不写
     */
    //安卓10推荐uri，并且path的方式不再可用
    override fun loadGifAsBitmap(context: Context, gifUri: Uri, imageView: ImageView) {
        val imageLoader = ImageLoader.Builder(context).componentRegistry {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder(context))
            } else {
                add(GifDecoder())
            }
        }.build()
        imageView.load(gifUri, imageLoader)
    }

    /**
     * 加载gif动图到ImageView，gif动图动
     *
     * @param context   上下文
     * @param gifUri   gif动图路径Uri
     * @param imageView 加载动图的ImageView
     *                  <p>
     *                  备注：不支持动图显示的情况下可以不写
     */
    //安卓10推荐uri，并且path的方式不再可用
    override fun loadGif(context: Context, gifUri: Uri, imageView: ImageView) {
        val imageLoader = ImageLoader.Builder(context).componentRegistry {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder(context))
            } else {
                add(GifDecoder())
            }
        }.build()
        imageView.load(gifUri, imageLoader)
    }

    /**
     * 获取图片加载框架中的缓存Bitmap
     *
     * @param context 上下文
     * @param uri    图片路径
     * @param width   图片宽度
     * @param height  图片高度
     * @return Bitmap
     * @throws Exception 异常直接抛出，EasyPhotos内部处理
     */
    //安卓10推荐uri，并且path的方式不再可用
    override fun getCacheBitmap(context: Context, uri: Uri, width: Int, height: Int): Bitmap {
        var bitmap: Bitmap? = null
        CoroutineScope(Dispatchers.IO).launch {
            val request = ImageRequest.Builder(context)
                .data(uri)
                .size(width, height)
                .build()
            val drawable = context.imageLoader.execute(request).drawable
            if (drawable != null) {
                bitmap = drawable.toBitmap()
            }
        }
        return bitmap!!
    }
}