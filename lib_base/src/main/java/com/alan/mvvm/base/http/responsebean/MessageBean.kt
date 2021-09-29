package com.alan.mvvm.base.http.responsebean

import com.chad.library.adapter.base.entity.MultiItemEntity

data class MessageBean(
    var id: String? = null,
    var title: String? = null,
    var content: ContentBean? = null,
    var url: String? = null,
    var userId: String? = null,
    var isRead: Int = 0,
    var source: Int = 0,
    var createTime: String? = null
) : MultiItemEntity {

    companion object {
        //关注
        const val FOCUS_MSG = 4

        //邀请加入
        const val INVITE_MSG = 0

        //举报违规
        const val SPEAK_MSG = 5

        //打招呼
        const val GREET_MSG = 6

        //礼物相关
        const val GIFT_MSG = 13

        //干饭相关
        const val MEAL_MSG = 14


    }


    class ContentBean {
        /**
         * user : {"id":4321,"userId":1367161016680480,"userName":"散了流年﹋淡了曾经","gender":1,"avatar":"http://heartmusicavatar.oss-cn-beijing.aliyuncs.com/avatar/default.png","followStatus":0}
         * room : {"id":2,"name":"chat room test","chatRoomId":"0","tag":{"location":"北京","job":"老司机","hobby":"唱歌"},"onlineNum":1}
         */
        var msg: String? = null
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

    override val itemType: Int
        get() = source


}