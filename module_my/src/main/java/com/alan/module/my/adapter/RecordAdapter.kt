package com.alan.module.my.adapter

import com.alan.module.my.R
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class RecordAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_record) {

    override fun convert(holder: BaseViewHolder, item: String) {
//        helper.setText(R.id.tv_name, item.getMsg())
//        helper.setText(R.id.tv_time, item.getCreateTime())
//        if (item.getPoints() > 0) {
//            helper.setText(R.id.tv_num, "+" + item.getPoints())
//        } else {
//            helper.setText(R.id.tv_num, "" + item.getPoints())
//        }

    }

}