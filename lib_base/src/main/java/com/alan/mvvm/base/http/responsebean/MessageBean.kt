package com.alan.mvvm.base.http.responsebean

import android.os.Parcel
import android.os.Parcelable
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.tencent.bugly.crashreport.biz.UserInfoBean

data class MessageBean(
    var id: String? = null,
    var title: String? = null,
    var content: ContentBean? = null,
    var url: String? = null,
    var userId: String? = null,
    var isRead: Int = 0,
    var source: Int = 0,
    var createTime: String? = null
) : MultiItemEntity, Parcelable {
    /**
     * id : 3
     * title : 散了流年﹋淡了曾经 已将此群转让给你
     * content : {"user":{"id":4321,"userId":1367161016680480,"userName":"散了流年﹋淡了曾经","gender":1,"avatar":"http://heartmusicavatar.oss-cn-beijing.aliyuncs.com/avatar/default.png","followStatus":0},"room":{"id":2,"name":"chat room test","chatRoomId":"0","tag":{"location":"北京","job":"老司机","hobby":"唱歌"},"onlineNum":1}}
     * url :
     * userId : 1367161016680480
     * source : 3
     * isRead : 1
     * createTime : 2020-09-02 21:13
     */


    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        title = parcel.readString()
        url = parcel.readString()
        userId = parcel.readString()
        isRead = parcel.readInt()
        createTime = parcel.readString()
    }

    class ContentBean {
        /**
         * user : {"id":4321,"userId":1367161016680480,"userName":"散了流年﹋淡了曾经","gender":1,"avatar":"http://heartmusicavatar.oss-cn-beijing.aliyuncs.com/avatar/default.png","followStatus":0}
         * room : {"id":2,"name":"chat room test","chatRoomId":"0","tag":{"location":"北京","job":"老司机","hobby":"唱歌"},"onlineNum":1}
         */
        var user: UserInfoBean? = null
        var room: RoomBean? = null
        var enterCode: String? = null
        var roomId: String? = null
        var audioPath: String? = null
        var duration = 0
        var replyId: String? = null
        var greetingId: String? = null
        var isRead = 0

        class RoomBean {
            /**
             * id : 2
             * name : chat room test
             * chatRoomId : 0
             * tag : {"location":"北京","job":"老司机","hobby":"唱歌"}
             * onlineNum : 1
             */
            var id = 0
            var name: String? = null
            var chatRoomId: String? = null
            var tag: TagBean? = null
            var onlineNum = 0

            class TagBean {
                /**
                 * location : 北京
                 * job : 老司机
                 * hobby : 唱歌
                 */
                var location: String? = null
                var job: String? = null
                var hobby: String? = null
            }
        }
    }


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeString(url)
        parcel.writeString(userId)
        parcel.writeInt(isRead)
        parcel.writeString(createTime)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MessageBean> {
        //关注
        const val FOCUS_MSG = 4

        //邀请加入
        const val INVITE_MSG = 3

        //举报违规
        const val SPEAK_MSG = 5

        //打招呼
        const val GREET_MSG = 6

        //打招呼
        const val GIFT_MSG = 9

        override fun createFromParcel(parcel: Parcel): MessageBean {
            return MessageBean(parcel)
        }

        override fun newArray(size: Int): Array<MessageBean?> {
            return arrayOfNulls(size)
        }

    }

    override val itemType: Int = source

}