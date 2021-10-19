package com.alan.mvvm.base.coil.transformation

import android.graphics.*
import androidx.core.graphics.applyCanvas
import coil.bitmap.BitmapPool
import coil.decode.DecodeUtils
import coil.size.OriginalSize
import coil.size.PixelSize
import coil.size.Scale
import coil.size.Size
import coil.transform.Transformation
import kotlin.math.min
import kotlin.math.roundToInt

class CircleCropOverTransformation() : Transformation {


    override fun key(): String = CircleCropOverTransformation::class.java.name

    override suspend fun transform(pool: BitmapPool, input: Bitmap, size: Size): Bitmap {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)


        val outputWidth: Int
        val outputHeight: Int
        when (size) {
            is PixelSize -> {
                val multiplier = DecodeUtils.computeSizeMultiplier(
                    srcWidth = input.width,
                    srcHeight = input.height,
                    dstWidth = size.width,
                    dstHeight = size.height,
                    scale = Scale.FILL
                )
                outputWidth = (size.width / multiplier).roundToInt()
                outputHeight = (size.height / multiplier).roundToInt()
            }
            is OriginalSize -> {
                outputWidth = input.width
                outputHeight = input.height
            }
        }

        val output = pool.get(outputWidth, outputHeight, input.safeConfig)


        val minSize = min(input.width, input.height)
        val srcRect = Rect(0, 0, minSize, minSize)
        val destRect = Rect(0, 0, outputWidth, outputHeight)
        val radius = minSize / 2f
        output.applyCanvas {
            drawCircle(radius, radius, radius, paint)
            paint.xfermode = XFERMODE
//            drawBitmap(input, radius - input.width / 2f, radius - input.height / 2f, paint)
            drawBitmap(input, srcRect, destRect, paint)
        }

        return output
    }

    override fun equals(other: Any?) = other is CircleCropOverTransformation

    override fun hashCode() = javaClass.hashCode()

    override fun toString() = "CircleCropOverTransformation()"

    private companion object {
        val XFERMODE = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    }

    /** Guard against null bitmap configs. */
    internal val Bitmap.safeConfig: Bitmap.Config
        get() = config ?: Bitmap.Config.ARGB_8888
}
