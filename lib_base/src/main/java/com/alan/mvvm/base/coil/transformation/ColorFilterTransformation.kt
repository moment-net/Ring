package com.alan.mvvm.base.coil.transformation

import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import androidx.annotation.ColorInt
import androidx.core.graphics.applyCanvas
import coil.bitmap.BitmapPool
import coil.size.Size
import coil.transform.Transformation

/**
 * 作者：alan
 * 时间：2021/8/18
 * 备注：为图片添加蒙层
 */
class ColorFilterTransformation(@ColorInt private val color: Int) : Transformation {
    override fun key(): String = "${ColorFilterTransformation::class.java.name}-$color"

    override suspend fun transform(pool: BitmapPool, input: Bitmap, size: Size): Bitmap {
        val width = input.width
        val height = input.height
        val config = input.config
        val output = pool.get(width, height, config)

        output.applyCanvas {
            val paint = Paint()
            paint.isAntiAlias = true
            paint.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP)
            drawBitmap(input, 0f, 0f, paint)
        }

        return output
    }


}