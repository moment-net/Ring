package com.alan.module.my.adapter

import android.text.TextUtils
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alan.module.my.R
import com.alan.mvvm.base.http.responsebean.TagBean
import com.alan.mvvm.base.ktx.dp2px
import com.alan.mvvm.base.ktx.gone
import com.alan.mvvm.base.ktx.visible
import com.alan.mvvm.base.utils.MyGridItemDecoration
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class CardInfoAdapter : BaseQuickAdapter<TagBean, BaseViewHolder>(R.layout.item_cardinfo) {

    init {
        addChildClickViewIds(R.id.tv_copy)
    }

    override fun convert(holder: BaseViewHolder, item: TagBean) {
        val tv_title = holder.getView<TextView>(R.id.tv_title)
        val tv_value = holder.getView<TextView>(R.id.tv_value)
        val tv_copy = holder.getView<TextView>(R.id.tv_copy)
        val rvLabel = holder.getView<RecyclerView>(R.id.rv_label)

        tv_title.setText(item.tagName)

        if (TextUtils.equals(item.tagType, "input")) {
            tv_value.visible()
            tv_copy.visible()
            rvLabel.gone()

            tv_value.setText(item.checkedValues.get(0))
        } else {
            tv_value.gone()
            tv_copy.gone()
            rvLabel.visible()

            val mAdapter = LabelShowAdapter()
            rvLabel.apply {
                addItemDecoration(
                    MyGridItemDecoration(
                        context.dp2px(10f),
                        ContextCompat.getColor(context, R.color.white)
                    )
                )
                layoutManager = GridLayoutManager(context, 2)
                adapter = mAdapter
            }
            mAdapter.setList(item.checkedValues)
        }
    }

}