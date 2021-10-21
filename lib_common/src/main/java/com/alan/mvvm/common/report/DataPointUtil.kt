package com.alan.mvvm.common.report

object DataPointUtil {


    /**
     * 点击登录方式
     * type:
     * 1. 手机号 phone
     * 2. 微信 Wechat
     * 3. 手机号一键登录 Auto Phone
     */
    fun reportLogin(type: Int) {
        val loginType = if (type == 1) {
            ReportConstant.VALUE_PHONE
        } else if (type == 2) {
            ReportConstant.VALUE_WECHAT
        } else {
            ReportConstant.VALUE_AUTOPHONE
        }
        AmplitudeUtil.instance.logEvent(
            ReportConstant.EVENT_LOGIN,
            ReportConstant.KEY_TIME, System.currentTimeMillis().toString(),
            ReportConstant.KEY_LOGIN_TYPE, loginType
        )
    }

    /**
     * 点击获取验证码
     */
    fun reportGetCode() {
        AmplitudeUtil.instance.logEvent(
            ReportConstant.EVENT_GETCODE,
            ReportConstant.KEY_TIME, System.currentTimeMillis().toString(),
        )
    }

    /**
     * 重新点击获取验证码
     */
    fun reportReGetCode() {
        AmplitudeUtil.instance.logEvent(
            ReportConstant.EVENT_RESENTCODE,
            ReportConstant.KEY_TIME, System.currentTimeMillis().toString(),
        )
    }

    /**
     * 点击绑定手机号返回按钮
     */
    fun reportBindBack(userId: String) {
        AmplitudeUtil.instance.logEvent(
            ReportConstant.EVENT_BIND_PHONE_BACK,
            ReportConstant.KEY_USERID, userId,
            ReportConstant.KEY_TIME, System.currentTimeMillis().toString(),
        )
    }

    /**
     * 点击绑定手机号返回按钮
     */
    fun reportAutoPhoneBack() {
        AmplitudeUtil.instance.logEvent(
            ReportConstant.EVENT_PHONE_BACK,
            ReportConstant.KEY_TIME, System.currentTimeMillis().toString(),
        )
    }

    /**
     * 点击手机号返回按钮
     */
    fun reportLoginPhoneBack() {
        AmplitudeUtil.instance.logEvent(
            ReportConstant.EVENT_LOGIN_PHONE_BACK,
            ReportConstant.KEY_TIME, System.currentTimeMillis().toString(),
        )
    }

    /**
     * 输入验证码点击继续
     */
    fun reportCodeNext(isSuccess: Boolean) {
        val status = if (isSuccess) {
            ReportConstant.VALUE_TRUE
        } else {
            ReportConstant.VALUE_FALSE
        }
        AmplitudeUtil.instance.logEvent(
            ReportConstant.EVENT_CODE_NEXT,
            ReportConstant.KEY_TIME, System.currentTimeMillis().toString(),
            ReportConstant.KEY_STATUS, status,
        )
    }

    /**
     * 点击绑定手机号返回按钮
     */
    fun reportProfileNext(userId: String) {
        AmplitudeUtil.instance.logEvent(
            ReportConstant.EVENT_PROFILE_NEXT,
            ReportConstant.KEY_USERID, userId,
            ReportConstant.KEY_TIME, System.currentTimeMillis().toString(),
        )
    }

    /**
     * 注册成功
     * * type:
     * 1. 手机号 phone
     * 2. 微信 Wechat
     * 3. 手机号一键登录 Auto Phone
     */
    fun reportRegister(userId: String, type: Int) {
        val loginType = if (type == 1) {
            ReportConstant.VALUE_PHONE
        } else if (type == 2) {
            ReportConstant.VALUE_WECHAT
        } else {
            ReportConstant.VALUE_AUTOPHONE
        }
        AmplitudeUtil.instance.logEvent(
            ReportConstant.EVENT_REGISTER_SUCCESS,
            ReportConstant.KEY_USERID, userId,
            ReportConstant.KEY_TIME, System.currentTimeMillis().toString(),
            ReportConstant.KEY_LOGIN_TYPE, loginType,
        )
    }

    /**
     * 点击首页tab
     */
    fun reportTabHome(userId: String) {
        AmplitudeUtil.instance.logEvent(
            ReportConstant.EVENT_TAB_HOME,
            ReportConstant.KEY_USERID, userId,
            ReportConstant.KEY_TIME, System.currentTimeMillis().toString(),
        )
    }

    /**
     * 点击聊天tab
     */
    fun reportTabChat(userId: String) {
        AmplitudeUtil.instance.logEvent(
            ReportConstant.EVENT_TAB_CHAT,
            ReportConstant.KEY_USERID, userId,
            ReportConstant.KEY_TIME, System.currentTimeMillis().toString(),
        )
    }

    /**
     * 点击我的tab
     */
    fun reportTabMy(userId: String) {
        AmplitudeUtil.instance.logEvent(
            ReportConstant.EVENT_TAB_MY,
            ReportConstant.KEY_USERID, userId,
            ReportConstant.KEY_TIME, System.currentTimeMillis().toString(),
        )
    }

    /**
     * 点击左上角我的头像
     */
    fun reportHomeMy(userId: String) {
        AmplitudeUtil.instance.logEvent(
            ReportConstant.EVENT_HOME_MY,
            ReportConstant.KEY_USERID, userId,
            ReportConstant.KEY_TIME, System.currentTimeMillis().toString(),
        )
    }

    /**
     * 点击快速匹配
     */
    fun reportHomeMatch(userId: String) {
        AmplitudeUtil.instance.logEvent(
            ReportConstant.EVENT_HOME_MATCH,
            ReportConstant.KEY_USERID, userId,
            ReportConstant.KEY_TIME, System.currentTimeMillis().toString(),
        )
    }

    /**
     * 点击现在
     */
    fun reportHomeNow(userId: String) {
        AmplitudeUtil.instance.logEvent(
            ReportConstant.EVENT_HOME_TAB_NOW,
            ReportConstant.KEY_USERID, userId,
            ReportConstant.KEY_TIME, System.currentTimeMillis().toString(),
        )
    }

    /**
     * 点击想法
     */
    fun reportHomeThink(userId: String) {
        AmplitudeUtil.instance.logEvent(
            ReportConstant.EVENT_HOME_TAB_THINK,
            ReportConstant.KEY_USERID, userId,
            ReportConstant.KEY_TIME, System.currentTimeMillis().toString(),
        )
    }

    /**
     * 点击一起
     */
    fun reportTogether(userId: String, friendId: String) {
        AmplitudeUtil.instance.logEvent(
            ReportConstant.EVENT_HOME_TOGETHER,
            ReportConstant.KEY_USERID, userId,
            ReportConstant.KEY_TIME, System.currentTimeMillis().toString(),
            ReportConstant.KEY_FRIENDID, friendId,
        )
    }

    /**
     * 点赞
     */
    fun reportLike(userId: String, friendId: String) {
        AmplitudeUtil.instance.logEvent(
            ReportConstant.EVENT_HOME_LIKE,
            ReportConstant.KEY_USERID, userId,
            ReportConstant.KEY_TIME, System.currentTimeMillis().toString(),
            ReportConstant.KEY_FRIENDID, friendId,
        )
    }


    /**
     * 点击菜单
     */
    fun reportHomeMenu(userId: String) {
        AmplitudeUtil.instance.logEvent(
            ReportConstant.EVENT_HOME_MENU,
            ReportConstant.KEY_USERID, userId,
            ReportConstant.KEY_TIME, System.currentTimeMillis().toString(),
        )
    }

    /**
     * 点击举报
     */
    fun reportReport(userId: String) {
        AmplitudeUtil.instance.logEvent(
            ReportConstant.EVENT_HOME_REPORT,
            ReportConstant.KEY_USERID, userId,
            ReportConstant.KEY_TIME, System.currentTimeMillis().toString(),
        )
    }

    /**
     * 点击屏蔽
     */
    fun reportBlock(userId: String) {
        AmplitudeUtil.instance.logEvent(
            ReportConstant.EVENT_HOME_BLOCK,
            ReportConstant.KEY_USERID, userId,
            ReportConstant.KEY_TIME, System.currentTimeMillis().toString(),
        )
    }

    /**
     * 点击详情聊天
     */
    fun reportChat(userId: String, friendId: String, isOnline: Boolean) {
        val status = if (isOnline) {
            ReportConstant.VALUE_ONLINE
        } else {
            ReportConstant.VALUE_OFFLINE
        }
        AmplitudeUtil.instance.logEvent(
            ReportConstant.EVENT_DETAIL_CHAT,
            ReportConstant.KEY_USERID, userId,
            ReportConstant.KEY_TIME, System.currentTimeMillis().toString(),
            ReportConstant.KEY_FRIENDID, friendId,
            ReportConstant.KEY_FRIEND_STATUS, status,
        )
    }

    /**
     * 点击首页发布按钮
     */
    fun reportPublish(userId: String) {
        AmplitudeUtil.instance.logEvent(
            ReportConstant.EVENT_HOME_PUBLISH,
            ReportConstant.KEY_USERID, userId,
            ReportConstant.KEY_TIME, System.currentTimeMillis().toString(),
        )
    }

    /**
     * 点击首页发布正在
     */
    fun reportPublishNow(userId: String) {
        AmplitudeUtil.instance.logEvent(
            ReportConstant.EVENT_PUBLISH_NOW,
            ReportConstant.KEY_USERID, userId,
            ReportConstant.KEY_TIME, System.currentTimeMillis().toString(),
        )
    }

    /**
     * 点击首页发布想法
     */
    fun reportPublishThink(userId: String) {
        AmplitudeUtil.instance.logEvent(
            ReportConstant.EVENT_PUBLISH_THINK,
            ReportConstant.KEY_USERID, userId,
            ReportConstant.KEY_TIME, System.currentTimeMillis().toString(),
        )
    }

    /**
     * 点击我的钻石
     */
    fun reportMyDiamond(userId: String) {
        AmplitudeUtil.instance.logEvent(
            ReportConstant.EVENT_MY_DIAMOND,
            ReportConstant.KEY_USERID, userId,
            ReportConstant.KEY_TIME, System.currentTimeMillis().toString(),
        )
    }

    /**
     * 点击充值钻石
     */
    fun reportBuyDiamond(userId: String, productId: String) {
        AmplitudeUtil.instance.logEvent(
            ReportConstant.EVENT_DIAMOND_RECHARGE,
            ReportConstant.KEY_USERID, userId,
            ReportConstant.KEY_TIME, System.currentTimeMillis().toString(),
            ReportConstant.KEY_PRODUCTID, productId,
        )
    }

    /**
     * 点击取消购买按钮
     */
    fun reportDiamondCancel(userId: String, productId: String) {
        AmplitudeUtil.instance.logEvent(
            ReportConstant.EVENT_DIAMOND_CANCEL,
            ReportConstant.KEY_USERID, userId,
            ReportConstant.KEY_TIME, System.currentTimeMillis().toString(),
            ReportConstant.KEY_PRODUCTID, productId,
        )
    }

    /**
     * 点击充值钻石页返回按钮
     */
    fun reportDiamondBack(userId: String) {
        AmplitudeUtil.instance.logEvent(
            ReportConstant.EVENT_DIAMOND_BACK,
            ReportConstant.KEY_USERID, userId,
            ReportConstant.KEY_TIME, System.currentTimeMillis().toString(),
        )
    }

    /**
     * 点击语音通话按钮
     * type
     * 1. 已接通 Success
     * 2. 对方拒绝 Refuse
     * 3. 占线/忙碌中 Busy
     */
    fun reportCall(userId: String, type: Int) {
        val status = if (type == 1) {
            ReportConstant.VALUE_SUCCESS
        } else if (type == 2) {
            ReportConstant.VALUE_REFUSE
        } else {
            ReportConstant.VALUE_BUSY
        }
        AmplitudeUtil.instance.logEvent(
            ReportConstant.EVENT_CHAT_CALL,
            ReportConstant.KEY_USERID, userId,
            ReportConstant.KEY_TIME, System.currentTimeMillis().toString(),
            ReportConstant.KEY_CALLSTATUS, status,
        )
    }

    /**
     * 点击挂断按钮
     */
    fun reportHangup(userId: String, waitTime: Long, callTime: Long) {
        AmplitudeUtil.instance.logEvent(
            ReportConstant.EVENT_CHAT_HANGUP,
            ReportConstant.KEY_USERID, userId,
            ReportConstant.KEY_TIME, System.currentTimeMillis().toString(),
            ReportConstant.KEY_WAITTIME, "${waitTime}",
            ReportConstant.KEY_CALLTIME, "${callTime}",
        )
    }

    /**
     * 点击缩略
     */
    fun reportCallScale(userId: String) {
        AmplitudeUtil.instance.logEvent(
            ReportConstant.EVENT_CALL_SCALE,
            ReportConstant.KEY_USERID, userId,
            ReportConstant.KEY_TIME, System.currentTimeMillis().toString(),
        )
    }
}