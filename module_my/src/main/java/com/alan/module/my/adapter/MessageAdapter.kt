package com.alan.module.my.adapter

import com.alan.module.my.R
import com.alan.mvvm.base.http.responsebean.MessageBean
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class MessageAdapter() : BaseMultiItemQuickAdapter<MessageBean, BaseViewHolder>() {

    init {
        addItemType(MessageBean.FOCUS_MSG, R.layout.item_msg_focus)
        addItemType(MessageBean.INVITE_MSG, R.layout.item_msg_chat)
    }


    override fun convert(holder: BaseViewHolder, item: MessageBean) {
        when (holder.itemViewType) {
            MessageBean.FOCUS_MSG -> {

            }
            MessageBean.INVITE_MSG -> {

            }
        }
    }

}