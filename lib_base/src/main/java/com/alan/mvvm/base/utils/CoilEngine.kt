package com.alan.mvvm.base.utils

import android.content.Context
import android.os.Build.VERSION.SDK_INT
import android.widget.ImageView
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.load
import com.alan.mvvm.base.R
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.engine.ImageEngine
import com.luck.picture.lib.listener.OnImageCompleteCallback
import com.luck.picture.lib.widget.longimage.SubsamplingScaleImageView
import java.io.File

object CoilEngine : ImageEngine {

//    /**
//     * 加载图片到ImageView
//     *
//     * @param context   上下文
//     * @param uri 图片路径Uri
//     * @param imageView 加载到的ImageView
//     */
//    //安卓10推荐uri，并且path的方式不再可用
//    override fun loadPhoto(context: Context, uri: Uri, imageView: ImageView) {
//        imageView.load(uri) {}
//    }
//
//    /**
//     * 加载gif动图图片到ImageView，gif动图不动
//     *
//     * @param context   上下文
//     * @param gifUri   gif动图路径Uri
//     * @param imageView 加载到的ImageView
//     *                  <p>
//     *                  备注：不支持动图显示的情况下可以不写
//     */
//    //安卓10推荐uri，并且path的方式不再可用
//    override fun loadGifAsBitmap(context: Context, gifUri: Uri, imageView: ImageView) {
//        val imageLoader = ImageLoader.Builder(context).componentRegistry {
//            if (SDK_INT >= 28) {
//                add(ImageDecoderDecoder(context))
//            } else {
//                add(GifDecoder())
//            }
//        }.build()
//        imageView.load(gifUri, imageLoader)
//    }
//
//    /**
//     * 加载gif动图到ImageView，gif动图动
//     *
//     * @param context   上下文
//     * @param gifUri   gif动图路径Uri
//     * @param imageView 加载动图的ImageView
//     *                  <p>
//     *                  备注：不支持动图显示的情况下可以不写
//     */
//    //安卓10推荐uri，并且path的方式不再可用
//    override fun loadGif(context: Context, gifUri: Uri, imageView: ImageView) {
//        val imageLoader = ImageLoader.Builder(context).componentRegistry {
//            if (SDK_INT >= 28) {
//                add(ImageDecoderDecoder(context))
//            } else {
//                add(GifDecoder())
//            }
//        }.build()
//        imageView.load(gifUri, imageLoader)
//    }
//
//    /**
//     * 获取图片加载框架中的缓存Bitmap
//     *
//     * @param context 上下文
//     * @param uri    图片路径
//     * @param width   图片宽度
//     * @param height  图片高度
//     * @return Bitmap
//     * @throws Exception 异常直接抛出，EasyPhotos内部处理
//     */
//    //安卓10推荐uri，并且path的方式不再可用
//    override fun getCacheBitmap(context: Context, uri: Uri, width: Int, height: Int): Bitmap {
//        var bitmap: Bitmap? = null
//        CoroutineScope(Dispatchers.IO).launch {
//            val request = ImageRequest.Builder(context)
//                .data(uri)
//                .size(width, height)
//                .build()
//            val drawable = context.imageLoader.execute(request).drawable
//            if (drawable != null) {
//                bitmap = drawable.toBitmap()
//            }
//        }
//        return bitmap!!
//    }

    /**
     * 预览大图页面
     * 加载图片
     *
     * @param context
     * @param url
     * @param imageView
     */
    override fun loadImage(context: Context, url: String, imageView: ImageView) {
        if (PictureMimeType.isContent(url)) {
            imageView.load(url) {
                placeholder(R.drawable.picture_image_placeholder)
            }
        } else {
            imageView.load(File(url)) {
                placeholder(R.drawable.picture_image_placeholder)
            }
        }
    }

    /**
     * 网络图片
     * 加载网络图片适配长图方案
     * # 注意：此方法只有加载网络图片才会回调
     *
     * @param context
     * @param url
     * @param imageView
     * @param longImageView
     * @param callback      网络图片加载回调监听 {link after version 2.5.1 Please use the #OnImageCompleteCallback#}
     */
    override fun loadImage(
        context: Context,
        url: String,
        imageView: ImageView,
        longImageView: SubsamplingScaleImageView?,
        callback: OnImageCompleteCallback?
    ) {
        imageView.load(url) {

        }
    }

    /**
     * 加载网络图片适配长图方案
     * # 注意：此方法只有加载网络图片才会回调
     *
     * @param context
     * @param url
     * @param imageView
     * @param longImageView
     * @ 已废弃
     */
    override fun loadImage(
        context: Context,
        url: String,
        imageView: ImageView,
        longImageView: SubsamplingScaleImageView?
    ) {
        imageView.load(url) {}
    }

    /**
     * 加载相册目录
     *
     * @param context   上下文
     * @param url       图片路径
     * @param imageView 承载图片ImageView
     */
    override fun loadFolderImage(context: Context, url: String, imageView: ImageView) {
        if (PictureMimeType.isContent(url)) {
            imageView.load(url) {
                placeholder(R.drawable.picture_image_placeholder)
                size(200, 200)
            }
        } else {
            imageView.load(File(url)) {
                placeholder(R.drawable.picture_image_placeholder)
                size(200, 200)
            }
        }
    }

    /**
     * 加载GIF图
     */
    override fun loadAsGifImage(context: Context, url: String, imageView: ImageView) {
        val imageLoader = ImageLoader.Builder(context).componentRegistry {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder(context))
            } else {
                add(GifDecoder())
            }
        }.build()
        imageView.load(url, imageLoader)
    }

    /**
     * 加载图片列表图片
     *
     * @param context   上下文
     * @param url       图片路径
     * @param imageView 承载图片ImageView
     */
    override fun loadGridImage(context: Context, url: String, imageView: ImageView) {
        if (PictureMimeType.isContent(url)) {
            imageView.load(url) {
                placeholder(R.drawable.picture_image_placeholder)
                size(200, 200)
            }
        } else {
            imageView.load(File(url)) {
                placeholder(R.drawable.picture_image_placeholder)
                size(200, 200)
            }
        }
    }
}