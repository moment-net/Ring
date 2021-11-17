package com.alan.module.main.adapter

import android.animation.ObjectAnimator
import android.net.Uri
import android.widget.ImageView
import coil.load
import com.alan.module.main.R
import com.alan.mvvm.base.ktx.gone
import com.alan.mvvm.base.ktx.visible
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.photoview.PhotoView
import java.io.File




/**
 * 作者：alan
 * 时间：2021/8/1
 * 备注：
 */
class PicVPAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_pic_detail) {

    init {
        addChildClickViewIds(R.id.iv_pic)
    }

    override fun convert(holder: BaseViewHolder, item: String) {
        val iv_pic = holder.getView<PhotoView>(R.id.iv_pic)
        val iv_load = holder.getView<ImageView>(R.id.iv_load)

//        val mScreenWidth = context.getScreenWidth()
//        val mScreenHeight = context.getScreenHeight()
//        val width = Math.min(item.width, item.height).toFloat()
//        val height = Math.max(item.height, item.width).toFloat()
//        if (width > 0 && height > 0) {
//            // 只需让图片的宽是屏幕的宽，高乘以比例
//            val displayHeight = Math.ceil((width * height / width).toDouble()).toInt()
//            //最终让图片按照宽是屏幕 高是等比例缩放的大小
//            val layoutParams = iv_pic.getLayoutParams()
//            layoutParams.width = mScreenWidth
//            layoutParams.height = if (displayHeight < mScreenHeight) displayHeight + mScreenHeight else displayHeight
//        }
//        val path = if (item.isCut() && !item.isCompressed()) {
//            // 裁剪过
//            item.getCutPath()
//        } else if (item.isCompressed() || item.isCut() && item.isCompressed()) {
//            // 压缩过,或者裁剪同时压缩过,以最终压缩过图片为准
//            item.getCompressPath()
//        } else if (item.isToSandboxPath()) {
//            // AndroidQ特有path
//            item.getAndroidQToPath()
//        } else {
//            // 原图
//            item.getPath()
//        }
//
        if (item.startsWith("http") || item.startsWith("https")) {
            val anim = ObjectAnimator.ofInt(iv_load, "ImageLevel", 0, 10000)
            anim.duration = 800
            anim.repeatCount = ObjectAnimator.INFINITE


            iv_pic.load(item) {
                placeholder(R.drawable.shape_trans)
                crossfade(true)
                listener(
                    onStart = {
                        iv_load.visible()
                        anim.start()
                    },
                    onError = { request, error ->
                        iv_load.gone()
                        anim.cancel()
                    },
                    onSuccess = { request, metadata ->
                        iv_load.gone()
                        anim.cancel()
                    })
            }
        } else if (PictureMimeType.isContent(item)) {
            iv_pic.load(Uri.parse(item)) {
                placeholder(R.drawable.shape_trans)
                crossfade(true)
            }
        } else {
            iv_pic.load(File(item)) {
                placeholder(R.drawable.shape_trans)
                crossfade(true)
            }
        }
    }
}