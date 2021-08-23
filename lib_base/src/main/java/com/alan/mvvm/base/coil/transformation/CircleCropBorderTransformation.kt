package com.alan.mvvm.base.coil.transformation

import android.graphics.*
import androidx.annotation.Px
import androidx.core.graphics.applyCanvas
import coil.bitmap.BitmapPool
import coil.decode.DecodeUtils
import coil.size.OriginalSize
import coil.size.PixelSize
import coil.size.Scale
import coil.size.Size
import coil.transform.Transformation
import kotlin.math.roundToInt

class CircleCropBorderTransformation(
    @Px private val radius: Float = 0f,
    @Px private val borderWidth: Float = 0f,
    @Px private val borderColor: Int = 0
) : Transformation {


    init {
        require(radius >= 0 && borderWidth >= 0) { "All radii must be >= 0." }
    }

    override fun key() =
        "${CircleCropBorderTransformation::class.java.name}-$radius,$borderWidth,$borderColor"

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
        output.applyCanvas {
            drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)

            val radii = floatArrayOf(
                radius,
                radius,
                radius,
                radius,
                radius,
                radius,
                radius,
                radius
            )
            paint.style = Paint.Style.FILL
            paint.color = borderColor
            val rectBg = RectF(0f, 0f, width.toFloat(), height.toFloat())
            val pathBg = Path().apply { addRoundRect(rectBg, radii, Path.Direction.CW) }
            drawPath(pathBg, paint)


            val matrix = Matrix()
            matrix.setTranslate(
                (outputWidth - input.width) / 2f,
                (outputHeight - input.height) / 2f
            )
            val shader = BitmapShader(input, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            shader.setLocalMatrix(matrix)
            paint.shader = shader


            val rect = RectF(
                borderWidth,
                borderWidth,
                width.toFloat() - borderWidth,
                height.toFloat() - borderWidth
            )
            val path = Path().apply { addRoundRect(rect, radii, Path.Direction.CW) }
            drawPath(path, paint)
        }

        return output
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return other is CircleCropBorderTransformation &&
                radius == other.radius &&
                borderWidth == other.borderWidth &&
                borderColor == other.borderColor
    }

    override fun hashCode(): Int {
        var result = radius.hashCode()
        result = 31 * result + borderWidth.hashCode()
        result = 31 * result + borderColor.hashCode()
        return result
    }

    override fun toString(): String {
        return "CircleCropBorderTransformation(radius=$radius, borderWidth=$borderWidth, " +
                "borderColor=$borderColor)"
    }

    /** Guard against null bitmap configs. */
    internal val Bitmap.safeConfig: Bitmap.Config
        get() = config ?: Bitmap.Config.ARGB_8888
}
