package com.alan.module.my.adapter

import com.alan.module.my.R
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class DiamondListAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_diamond) {

    override fun convert(holder: BaseViewHolder, item: String) {
//        helper.setText(R.id.tv_num, item.getNum().toString() + "钻石")
//        helper.setText(R.id.tv_price, NumberUtils.getDoubleNum(item.getAmount()).toString() + "¥")
//        helper.getView<View>(R.id.tv_price).setClickable(false)
//        if (item.getActivity() != null) {
//            helper.setGone(R.id.iv_first, false)
//        } else {
//            helper.setGone(R.id.iv_first, true)
//        }
    }

}