package com.alan.module.my.adapter

import android.view.View
import com.alan.module.my.R
import com.alan.mvvm.base.http.responsebean.GoodBean
import com.alan.mvvm.base.utils.NumberUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class DiamondListAdapter : BaseQuickAdapter<GoodBean, BaseViewHolder>(R.layout.item_diamond) {

    override fun convert(helper: BaseViewHolder, item: GoodBean) {
        helper.setText(R.id.tv_num, item.num + "钻石")
        helper.setText(R.id.tv_price, "${NumberUtils.getDoubleNum(item.amount)}¥")
        helper.getView<View>(R.id.tv_price).setClickable(false)
        if (item.activity != null) {
            helper.setGone(R.id.iv_first, false)
        } else {
            helper.setGone(R.id.iv_first, true)
        }
    }

}