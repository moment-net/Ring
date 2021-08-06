package com.alan.module.my.adapter

import com.alan.module.my.R
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class WithDrawDetailAdapter :
    BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_withdraw_detail) {

    override fun convert(holder: BaseViewHolder, item: String) {
//        val cl_root: ConstraintLayout = helper.getView<ConstraintLayout>(R.id.cl_root)
//        val iv_dot: ImageView = helper.getView<ImageView>(R.id.iv_dot)
//        val view_line: View = helper.getView<View>(R.id.view_line)
//        val tv_content: TextView = helper.getView<TextView>(R.id.tv_content)
//        val tv_result: TextView = helper.getView<TextView>(R.id.tv_result)
//        val tv_date: TextView = helper.getView<TextView>(R.id.tv_date)
//
//        val itemPosition = getItemPosition(item)
//        if (itemPosition == 0) {
//            val lp_dot = iv_dot.layoutParams
//            lp_dot.width = DensityUtil.dp2px(context, 11)
//            lp_dot.height = DensityUtil.dp2px(context, 11)
//            iv_dot.layoutParams = lp_dot
//            iv_dot.setImageResource(R.drawable.shape_circle_black)
//            view_line.visibility = View.VISIBLE
//            val layoutParams = view_line.layoutParams as MarginLayoutParams
//            layoutParams.topMargin = DensityUtil.dp2px(context, 5)
//            view_line.layoutParams = layoutParams
//            tv_content.setText(item.getResult())
//            tv_result.visibility = View.GONE
//            tv_date.setText(item.getOperateTime())
//        } else if (itemPosition == 1) {
//            val lp_dot = iv_dot.layoutParams
//            lp_dot.width = DensityUtil.dp2px(context, 11)
//            lp_dot.height = DensityUtil.dp2px(context, 11)
//            iv_dot.layoutParams = lp_dot
//            iv_dot.setImageResource(R.drawable.shape_circle_black)
//            if (data.size > 2) {
////                view_line.setVisibility(View.VISIBLE);
//                val layoutParams = view_line.layoutParams as ConstraintLayout.LayoutParams
//                layoutParams.bottomToBottom = cl_root.id
//                view_line.layoutParams = layoutParams
//            } else {
//                val layoutParams = view_line.layoutParams as ConstraintLayout.LayoutParams
//                layoutParams.bottomToBottom = iv_dot.id
//                view_line.layoutParams = layoutParams
//            }
//            val layoutParams = view_line.layoutParams as MarginLayoutParams
//            layoutParams.topMargin = DensityUtil.dp2px(context, 0)
//            view_line.layoutParams = layoutParams
//            tv_content.setText(item.getResult())
//            tv_result.visibility = View.GONE
//            tv_date.setText(item.getOperateTime())
//        } else if (itemPosition == 2) {
//            val lp_dot = iv_dot.layoutParams
//            lp_dot.width = DensityUtil.dp2px(context, 21)
//            lp_dot.height = DensityUtil.dp2px(context, 21)
//            iv_dot.layoutParams = lp_dot
//            if (TextUtils.equals(item.getStatus(), "-1")) {
//                //失败
//                iv_dot.setImageResource(R.mipmap.icon_withdraw_error)
//                tv_result.visibility = View.VISIBLE
//                tv_result.text = "原因：" + item.getProcessMsg()
//            } else if (TextUtils.equals(item.getStatus(), "1")) {
//                //成功
//                iv_dot.setImageResource(R.mipmap.icon_withdraw_success)
//                tv_result.visibility = View.GONE
//            }
//            view_line.visibility = View.INVISIBLE
//            val layoutParams = view_line.layoutParams as MarginLayoutParams
//            layoutParams.topMargin = DensityUtil.dp2px(context, 0)
//            view_line.layoutParams = layoutParams
//            tv_content.setText(item.getResult())
//            tv_date.setText(item.getOperateTime())
//        }

    }

}