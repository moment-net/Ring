package com.alan.module.my.adapter

import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.alan.module.my.R
import com.alan.mvvm.base.http.responsebean.WithdrawBean
import com.alan.mvvm.base.ktx.dp2px
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class WithDrawDetailAdapter :
    BaseQuickAdapter<WithdrawBean, BaseViewHolder>(R.layout.item_withdraw_detail) {

    override fun convert(holder: BaseViewHolder, item: WithdrawBean) {
        val cl_root: ConstraintLayout = holder.getView<ConstraintLayout>(R.id.cl_root)
        val iv_dot: ImageView = holder.getView<ImageView>(R.id.iv_dot)
        val view_line: View = holder.getView<View>(R.id.view_line)
        val tv_content: TextView = holder.getView<TextView>(R.id.tv_content)
        val tv_result: TextView = holder.getView<TextView>(R.id.tv_result)
        val tv_date: TextView = holder.getView<TextView>(R.id.tv_date)

        val itemPosition = getItemPosition(item)
        if (itemPosition == 0) {
            val lp_dot = iv_dot.layoutParams
            lp_dot.width = context.dp2px(11f)
            lp_dot.height = context.dp2px(11f)
            iv_dot.layoutParams = lp_dot
            iv_dot.setImageResource(R.drawable.shape_circle_black)
            view_line.visibility = View.VISIBLE
            val layoutParams = view_line.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.topMargin = context.dp2px(5f)
            view_line.layoutParams = layoutParams
            tv_content.setText(item.result)
            tv_result.visibility = View.GONE
            tv_date.setText(item.operateTime)
        } else if (itemPosition == 1) {
            val lp_dot = iv_dot.layoutParams
            lp_dot.width = context.dp2px(11f)
            lp_dot.height = context.dp2px(11f)
            iv_dot.layoutParams = lp_dot
            iv_dot.setImageResource(R.drawable.shape_circle_black)
            if (data.size > 2) {
                val layoutParams = view_line.layoutParams as ConstraintLayout.LayoutParams
                layoutParams.bottomToBottom = cl_root.id
                view_line.layoutParams = layoutParams
            } else {
                val layoutParams = view_line.layoutParams as ConstraintLayout.LayoutParams
                layoutParams.bottomToBottom = iv_dot.id
                view_line.layoutParams = layoutParams
            }
            val layoutParams = view_line.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.topMargin = context.dp2px(0f)
            view_line.layoutParams = layoutParams
            tv_content.setText(item.result)
            tv_result.visibility = View.GONE
            tv_date.setText(item.operateTime)
        } else if (itemPosition == 2) {
            val lp_dot = iv_dot.layoutParams
            lp_dot.width = context.dp2px(21f)
            lp_dot.height = context.dp2px(21f)
            iv_dot.layoutParams = lp_dot
            if (TextUtils.equals(item.status, "-1")) {
                //失败
                iv_dot.setImageResource(R.drawable.icon_withdraw_error)
                tv_result.visibility = View.VISIBLE
                tv_result.text = "原因：" + item.processMsg
            } else if (TextUtils.equals(item.status, "1")) {
                //成功
                iv_dot.setImageResource(R.drawable.icon_withdraw_success)
                tv_result.visibility = View.GONE
            }
            view_line.visibility = View.INVISIBLE
            val layoutParams = view_line.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.topMargin = context.dp2px(0f)
            view_line.layoutParams = layoutParams
            tv_content.setText(item.result)
            tv_date.setText(item.operateTime)
        }

    }

}