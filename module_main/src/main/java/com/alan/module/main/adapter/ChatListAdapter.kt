package com.alan.module.main.adapter

import android.text.TextUtils
import android.widget.ImageView
import com.alan.module.main.R
import com.alan.mvvm.base.coil.CoilUtils
import com.alan.mvvm.base.http.responsebean.AvatarInfoBean
import com.alan.mvvm.base.utils.DateUtils
import com.alan.mvvm.base.utils.GsonUtil
import com.alan.mvvm.common.constant.IMConstant
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.hyphenate.chat.EMConversation
import com.hyphenate.chat.EMMessage
import com.hyphenate.chat.EMTextMessageBody
import java.util.*

class ChatListAdapter : BaseQuickAdapter<EMConversation, BaseViewHolder>(R.layout.item_chatlist) {

    override fun convert(holder: BaseViewHolder, item: EMConversation) {
        var avatar: String? = null
        var userName: String? = null
        if (!TextUtils.isEmpty(item.extField)) {
            val avatarInfoBean = GsonUtil.jsonToBean(item.extField, AvatarInfoBean::class.java)
            avatar = avatarInfoBean?.avatar
            userName = avatarInfoBean?.userName
        } else {
            if (item.allMsgCount != 0) {
                val lastMessage = item.lastMessage
                if (lastMessage.direct() == EMMessage.Direct.RECEIVE) {
                    avatar = lastMessage.getStringAttribute(IMConstant.MESSAGE_ATTR_AVATAR)
                    userName = lastMessage.getStringAttribute(IMConstant.MESSAGE_ATTR_USERNAME)
                } else {
                    avatar = lastMessage.getStringAttribute(IMConstant.MESSAGE_ATTR_AVATAR_OTHER)
                    userName =
                        lastMessage.getStringAttribute(IMConstant.MESSAGE_ATTR_USERNAME_OTHER)
                }
                val avatarInfoBean = AvatarInfoBean(avatar, userName)
                item.extField = GsonUtil.jsonToString(avatarInfoBean)
            }
        }

        CoilUtils.loadCircle(
            holder.getView<ImageView>(R.id.iv_avatar),
            avatar!!
        )
        holder.setText(R.id.tv_name, userName)

        if (item.unreadMsgCount > 0) {
            holder.setText(R.id.tv_num, "${item.unreadMsgCount}")
            holder.setGone(R.id.tv_num, false)
        } else {
            holder.setGone(R.id.tv_num, true)
        }

        if (item.allMsgCount != 0) {
            val lastMessage = item.lastMessage
            holder.setText(R.id.tv_msg, getMessageText(lastMessage))
            holder.setText(R.id.tv_time, DateUtils.getTimestampString(Date(lastMessage.msgTime)))
            if (lastMessage.direct() == EMMessage.Direct.SEND && lastMessage.isAcked) {
                holder.setGone(R.id.iv_read, false)
            } else {
                holder.setGone(R.id.iv_read, true)
            }
        }
    }

    /**
     *
     * @param message
     * @param context
     * @return
     */
    fun getMessageText(message: EMMessage): String? {
        var digest = ""
        when (message.type) {
            EMMessage.Type.IMAGE -> digest = "[图片]"
            EMMessage.Type.VOICE -> digest = "[语音]"
            EMMessage.Type.VIDEO -> digest = "[视频]"
            EMMessage.Type.CUSTOM -> digest = "[自定义消息]"
            EMMessage.Type.TXT -> {
                val textBody: EMTextMessageBody = message.body as EMTextMessageBody
                if (message.getBooleanAttribute(IMConstant.MESSAGE_ATTR_IS_VOICE_CALL, false)) {
                    digest = "[语音通话]${textBody.message}"
                } else if (message.getBooleanAttribute(
                        IMConstant.MESSAGE_ATTR_IS_VIDEO_CALL,
                        false
                    )
                ) {
                    digest = "[视频通话]${textBody.message}"
                } else {
                    digest = textBody.message
                }
            }
            EMMessage.Type.FILE -> digest = "[文件]"
        }
        return digest
    }
}