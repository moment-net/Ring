package com.alan.mvvm.base.utils

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

/**
 * 作者：alan
 * 时间：2020/8/31
 * 备注：Recyclerview间隔
 */
class MyGridItemDecoration(private val distance: Int, color: Int) : ItemDecoration() {
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
        val gridLayoutManager: GridLayoutManager? = parent.layoutManager as GridLayoutManager?
        //列数
        val spanCount: Int = gridLayoutManager!!.spanCount
        val position = parent.getChildLayoutPosition(view)
        val column = position % spanCount
        if (false) {
            outRect.left = distance - column * distance / spanCount
            outRect.right = (column + 1) * distance / spanCount
            if (position < spanCount) {
                outRect.top = distance
            }
            outRect.bottom = distance
        } else {
            outRect.left = column * distance / spanCount
            outRect.right = distance - (column + 1) * distance / spanCount
            if (position >= spanCount) {
                outRect.top = distance
            }
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val view = parent.getChildAt(i)
            val dividerTop = view.bottom.toFloat()
            val dividerLeft = parent.left.toFloat()
            val dividerRight = parent.right.toFloat()
            val dividerBottom = dividerTop + distance
            c.drawRect(dividerLeft, dividerTop, dividerRight, dividerBottom, mPaint)
        }
    }


}