package com.alan.module.my.adapter

import android.text.TextUtils
import android.widget.TextView
import com.alan.module.my.R
import com.alan.mvvm.base.http.responsebean.TagBean
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class CardItemAdapter : BaseQuickAdapter<TagBean, BaseViewHolder>(R.layout.item_card_click) {


    override fun convert(holder: BaseViewHolder, item: TagBean) {
        val tv_title = holder.getView<TextView>(R.id.tv_title)
        val tv_value = holder.getView<TextView>(R.id.tv_value)

        tv_title.setText(item.tagName)

        if (item.checkedValues == null || item.checkedValues.isEmpty()) {
            // 类型 input=输入 option=单选 multiOption=多选
            tv_value.text = if (TextUtils.equals(item.tagType, "input")) {
                "请输入"
            } else {
                "请选择"
            }
        } else {
            val stringBuilder = StringBuilder()
            for (value in item.checkedValues) {
                if (TextUtils.equals(item.checkedValues.last(), value)) {
                    stringBuilder.append("${value}")
                } else {
                    stringBuilder.append("${value},")
                }
            }
            tv_value.setText(stringBuilder.toString())
        }
    }

}