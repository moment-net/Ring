package com.alan.mvvm.base.utils

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

/**
 * 作者：alan
 * 时间：2020/8/31
 * 备注：Recyclerview间隔
 */
class MyColorDecoration(
    private var left: Int = 0,
    private var top: Int = 0,
    private var right: Int = 0,
    private var bottom: Int = 0,
    color: Int
) : ItemDecoration() {
    private val mPaint: Paint

    init {
        mPaint = Paint()
        mPaint.isAntiAlias = true
        mPaint.color = color
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.left = left
        outRect.top = top
        outRect.right = right
        outRect.bottom = bottom
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val view = parent.getChildAt(i)
            val dividerTop = view.bottom.toFloat()
            val dividerLeft = parent.left.toFloat()
            val dividerRight = parent.right.toFloat()
            val dividerBottom = dividerTop + bottom
            c.drawRect(dividerLeft, dividerTop, dividerRight, dividerBottom, mPaint)
        }
    }


}