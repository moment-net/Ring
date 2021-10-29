package com.alan.mvvm.common.views

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager

class ContentViewpager : ViewPager {

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val index = currentItem
        var height = 0
        val v: View? = (adapter!!.instantiateItem(this, index) as Fragment).getView()
        if (v != null) {
            v.measure(
                widthMeasureSpec,
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            )
            height = v.getMeasuredHeight()
        }
        Log.e("viewpager", "高度${index}：${height}")
        val heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)


    }

}