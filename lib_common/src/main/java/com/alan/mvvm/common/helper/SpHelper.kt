package com.alan.mvvm.common.helper

import com.alan.mvvm.base.BaseApplication
import com.alan.mvvm.base.http.responsebean.LoginBean
import com.alan.mvvm.base.http.responsebean.UserInfoBean
import com.alan.mvvm.base.utils.EventBusUtils
import com.alan.mvvm.base.utils.GsonUtil
import com.alan.mvvm.base.utils.SpUtils
import com.alan.mvvm.common.constant.SpKey
import com.alan.mvvm.common.db.DbHelper
import com.alan.mvvm.common.event.UserEvent
import com.alan.mvvm.common.im.EMClientHelper
import com.alan.mvvm.common.report.AmplitudeUtil
import com.socks.library.KLog

/**
 * 作者：alan
 * 时间：2021/8/24
 * 备注：存储本地数据
 */
object SpHelper {

    /**
     * 设置是否同意隐私权限
     */
    fun setAgree(agree: Boolean) {
        KLog.e("xujm", "隐私权限：" + agree)
        SpUtils.put(SpKey.KEY_ISAGREE, agree)
    }

    /**
     * 获取是否同意隐私权限
     */
    fun isAgree(): Boolean {
        return SpUtils.getBoolean(SpKey.KEY_ISAGREE, false)!!
    }

    /**
     * 是否登录
     */
    fun isLogin(): Boolean {
        KLog.e("xujm", "获取登录：" + SpUtils.getBoolean(SpKey.KEY_ISLOGIN, false)!!)
        return SpUtils.getBoolean(SpKey.KEY_ISLOGIN, false)!!
    }

    /**
     * 设置是否登录
     */
    fun setLogin(login: Boolean) {
        KLog.e("xujm", "保存登录：" + login)
        SpUtils.put(SpKey.KEY_ISLOGIN, login)
    }


    /**
     * 获取token
     */
    fun getToken(): String {
        return SpUtils.getString(SpKey.KEY_TOKEN, "")!!
    }

    /**
     * 设置token
     */
    fun setToken(token: String) {
        SpUtils.put(SpKey.KEY_TOKEN, token)
    }

    /**
     * 判断是否是新用户
     */
    fun getNewUser(): Boolean {
        return SpUtils.getBoolean(SpKey.KEY_NEWUSER, false)!!
    }

    /**
     * 设置是否是新用户
     */
    fun setNewUser(isNew: Boolean) {
        SpUtils.put(SpKey.KEY_NEWUSER, isNew)
    }

    /**
     * 获取token
     */
    fun getUserInfo(): UserInfoBean? {
        val userinfo: String? = SpUtils.getString(SpKey.KEY_USERINFO, "")
        return GsonUtil.jsonToBean(userinfo, UserInfoBean::class.java)
    }

    /**
     * 设置token
     */
    fun setUserInfo(userInfoBean: UserInfoBean?) {
        val userInfo: String = GsonUtil.jsonToString(userInfoBean)
        SpUtils.put(SpKey.KEY_USERINFO, userInfo)
    }

    /**
     * 更新用户信息
     */
    fun updateUserInfo(obj: Any?) {
        when (obj) {
            is UserInfoBean -> {
                //更新用户
                setUserInfo(obj)
            }
            is LoginBean -> {
                //登陆
                setLogin(true)
                setToken(obj.token?.token ?: "")
                setNewUser(obj.newUser)
                setUserInfo(obj.user)
                AmplitudeUtil.instance.setUserId(obj.user!!.userId)
                DbHelper.instance.initDB(BaseApplication.mContext, obj.user!!.userId)
            }
        }
        EventBusUtils.postEvent(UserEvent(1))
    }

    /**
     * 清除用户信息
     */
    fun clearUserInfo() {
        setLogin(false)
        setToken("")
        setUserInfo(null)
        EMClientHelper.logoutEM(null)
        DbHelper.instance.closeDb()
    }


}