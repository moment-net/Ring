package com.alan.module.my.adapter

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alan.module.my.R
import com.alan.mvvm.base.http.responsebean.ReceivedBean
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class IncomeDataAdapter :
    BaseQuickAdapter<ReceivedBean, BaseViewHolder>(R.layout.item_income_data) {

    override fun convert(holder: BaseViewHolder, item: ReceivedBean) {
        holder.setText(R.id.tv_date, item.time)
        val recyclerView: RecyclerView = holder.getView<RecyclerView>(R.id.recyclerView)

        val incomeAdapter = IncomeAdapter()
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = incomeAdapter
        incomeAdapter.setList(item.list)

    }

}