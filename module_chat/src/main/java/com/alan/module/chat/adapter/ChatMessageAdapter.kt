package com.alan.module.chat.adapter

import com.alan.module.chat.R
import com.alan.module.chat.adapter.itemprovider.*
import com.alan.mvvm.common.constant.IMConstant
import com.alan.mvvm.common.db.entity.UserEntity
import com.chad.library.adapter.base.BaseProviderMultiAdapter
import com.hyphenate.chat.EMMessage

class ChatMessageAdapter(userEntity: UserEntity) : BaseProviderMultiAdapter<EMMessage>() {

    init {
        addItemProvider(ChatTextLeftProvider(userEntity))
        addItemProvider(ChatTextRightProvider())
        addItemProvider(ChatImageLeftProvider(userEntity))
        addItemProvider(ChatImageRightProvider())
        addItemProvider(ChatVoiceLeftProvider(userEntity))
        addItemProvider(ChatVoiceRightProvider())
        addItemProvider(ChatVideoLeftProvider(userEntity))
        addItemProvider(ChatVideoRightProvider())
        addItemProvider(ChatCustomLeftProvider(userEntity))
        addItemProvider(ChatCustomRightProvider())
        addItemProvider(ChatVoiceCallLeftProvider(userEntity))
        addItemProvider(ChatVoiceCallRightProvider())

        addChildClickViewIds(R.id.iv_pic)
        addChildClickViewIds(R.id.iv_video)
    }


    override fun getItemType(data: List<EMMessage>, position: Int): Int {
        val message = data.get(position)
        when (message.type) {
            EMMessage.Type.TXT -> {
                if (message.getBooleanAttribute(IMConstant.MESSAGE_ATTR_IS_VOICE_CALL, false)) {
                    return if (message.direct() == EMMessage.Direct.RECEIVE) IMConstant.MESSAGE_TYPE_VOICECALL_LEFT else IMConstant.MESSAGE_TYPE_VOICECALL_RIGHT
                } else {
                    return if (message.direct() == EMMessage.Direct.RECEIVE) IMConstant.MESSAGE_TYPE_TXT_LEFT else IMConstant.MESSAGE_TYPE_TXT_RIGHT
                }
            }
            EMMessage.Type.IMAGE -> {
                return if (message.direct() == EMMessage.Direct.RECEIVE) IMConstant.MESSAGE_TYPE_IMAGE_LEFT else IMConstant.MESSAGE_TYPE_IMAGE_RIGHT
            }
            EMMessage.Type.VOICE -> {
                return if (message.direct() == EMMessage.Direct.RECEIVE) IMConstant.MESSAGE_TYPE_VOICE_LEFT else IMConstant.MESSAGE_TYPE_VOICE_RIGHT
            }
            EMMessage.Type.VIDEO -> {
                return if (message.direct() == EMMessage.Direct.RECEIVE) IMConstant.MESSAGE_TYPE_VIDEO_LEFT else IMConstant.MESSAGE_TYPE_VIDEO_RIGHT
            }
            EMMessage.Type.FILE -> {
                return if (message.direct() == EMMessage.Direct.RECEIVE) IMConstant.MESSAGE_TYPE_FILE_LEFT else IMConstant.MESSAGE_TYPE_FILE_RIGHT
            }
            EMMessage.Type.CUSTOM -> {
                return if (message.direct() == EMMessage.Direct.RECEIVE) IMConstant.MESSAGE_TYPE_CUSTOM_LEFT else IMConstant.MESSAGE_TYPE_CUSTOM_RIGHT
            }
            else -> {
                return if (message.direct() == EMMessage.Direct.RECEIVE) IMConstant.MESSAGE_TYPE_TXT_LEFT else IMConstant.MESSAGE_TYPE_TXT_RIGHT
            }
        }
    }


}