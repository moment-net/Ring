package com.alan.mvvm.common.im.callkit.base;

/**
 * 作者：alan
 * 时间：2021/9/16
 * 备注：
 * * callkit 用户信息配置
 * * niceName 用户昵称
 * * headUrl  用户头像（为本地绝对路径或者url)
 */
public class EaseCallUserInfo {
    private String nickName;
    private String headImage;
    private String userId;

    public EaseCallUserInfo(String nickName, String headImage) {
        this.nickName = nickName;
        this.headImage = headImage;
    }

    public EaseCallUserInfo() {
    }


    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
