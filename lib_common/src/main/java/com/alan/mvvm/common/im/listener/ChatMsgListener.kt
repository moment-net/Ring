package com.alan.mvvm.common.im.listener

import com.hyphenate.chat.EMMessage

interface ChatMsgListener {
    fun onMessageReceived(messages: List<EMMessage>)
    fun onCmdMessageReceived(messages: List<EMMessage>)
    fun onMessageRead(messages: List<EMMessage>)
    fun onMessageDelivered(messages: List<EMMessage>)
    fun onMessageRecalled(messages: List<EMMessage>)
    fun onMessageChanged(message: EMMessage, change: Any)
}