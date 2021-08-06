package com.alan.module.main.adapter

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alan.module.main.R
import com.alan.mvvm.base.ktx.dp2px
import com.alan.mvvm.base.utils.MyColorDecoration
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class ManagerAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_manager) {

    override fun convert(holder: BaseViewHolder, item: String) {
        val rvTag: RecyclerView = holder.getView(R.id.rv_tag)

        var mAdapter = ManagerTagAdapter()
        rvTag.apply {
            addItemDecoration(
                MyColorDecoration(
                    0,
                    0,
                    context.dp2px(10f),
                    0,
                    ContextCompat.getColor(context, R.color.white)
                )
            )
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = mAdapter
        }

    }

}