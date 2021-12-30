package com.alan.mvvm.base.http.responsebean

import com.hyphenate.chat.EMMessage


data class SpeakVoiceBean(
    var content: String,
    var audio: String,
    var msg: EMMessage? = null,
)
