package com.alan.module.chat.adapter.itemprovider

import com.alan.module.chat.R
import com.alan.module.im.constants.IMConstant
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.hyphenate.chat.EMMessage

class ChatVideoLeftProvider(avatar: String) : BaseItemProvider<EMMessage>() {
    override val itemViewType: Int
        get() = IMConstant.MESSAGE_TYPE_VIDEO_LEFT


    override val layoutId: Int
        get() = R.layout.item_receive_video


    override fun convert(helper: BaseViewHolder, item: EMMessage) {


    }
}