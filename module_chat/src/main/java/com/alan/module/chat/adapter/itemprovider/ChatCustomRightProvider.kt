package com.alan.module.chat.adapter.itemprovider

import com.alan.module.chat.R
import com.alan.mvvm.common.constant.IMConstant
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.hyphenate.chat.EMMessage

class ChatCustomRightProvider() : BaseItemProvider<EMMessage>() {
    override val itemViewType: Int
        get() = IMConstant.MESSAGE_TYPE_CUSTOM_RIGHT


    override val layoutId: Int
        get() = R.layout.item_send_txt


    override fun convert(helper: BaseViewHolder, item: EMMessage) {


    }
}