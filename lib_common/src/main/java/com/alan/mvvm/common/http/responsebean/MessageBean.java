package com.alan.mvvm.common.http.responsebean;


import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.tencent.bugly.crashreport.biz.UserInfoBean;

public class MessageBean implements MultiItemEntity {
    //关注
    public static final int FOCUS_MSG = 4;
    //邀请加入
    public static final int INVITE_MSG = 3;
    //举报违规
    public static final int SPEAK_MSG = 5;
    //打招呼
    public static final int GREET_MSG = 6;
    //打招呼
    public static final int GIFT_MSG = 9;
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

    private String id;
    private String title;
    private ContentBean content;
    private String url;
    private String userId;
    private int source;
    private int isRead;
    private String createTime;


    @Override
    public int getItemType() {
        //返回传入的itemType
        return source;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ContentBean getContent() {
        return content;
    }

    public void setContent(ContentBean content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public static class ContentBean {
        /**
         * user : {"id":4321,"userId":1367161016680480,"userName":"散了流年﹋淡了曾经","gender":1,"avatar":"http://heartmusicavatar.oss-cn-beijing.aliyuncs.com/avatar/default.png","followStatus":0}
         * room : {"id":2,"name":"chat room test","chatRoomId":"0","tag":{"location":"北京","job":"老司机","hobby":"唱歌"},"onlineNum":1}
         */

        private UserInfoBean user;
        private RoomBean room;
        private String enterCode;
        private String roomId;
        private String audioPath;
        private int duration;
        private String replyId;
        private String greetingId;
        private int isRead;

        public String getReplyId() {
            return replyId;
        }

        public void setReplyId(String replyId) {
            this.replyId = replyId;
        }

        public int getIsRead() {
            return isRead;
        }

        public void setIsRead(int isRead) {
            this.isRead = isRead;
        }

        public UserInfoBean getUser() {
            return user;
        }

        public void setUser(UserInfoBean user) {
            this.user = user;
        }

        public RoomBean getRoom() {
            return room;
        }

        public void setRoom(RoomBean room) {
            this.room = room;
        }

        public String getEnterCode() {
            return enterCode;
        }

        public void setEnterCode(String enterCode) {
            this.enterCode = enterCode;
        }

        public String getRoomId() {
            return roomId;
        }

        public void setRoomId(String roomId) {
            this.roomId = roomId;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public String getAudioPath() {
            return audioPath;
        }

        public void setAudioPath(String audioPath) {
            this.audioPath = audioPath;
        }

        public String getGreetingId() {
            return greetingId;
        }

        public void setGreetingId(String greetingId) {
            this.greetingId = greetingId;
        }

        public static class RoomBean {
            /**
             * id : 2
             * name : chat room test
             * chatRoomId : 0
             * tag : {"location":"北京","job":"老司机","hobby":"唱歌"}
             * onlineNum : 1
             */

            private int id;
            private String name;
            private String chatRoomId;
            private TagBean tag;
            private int onlineNum;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getChatRoomId() {
                return chatRoomId;
            }

            public void setChatRoomId(String chatRoomId) {
                this.chatRoomId = chatRoomId;
            }

            public TagBean getTag() {
                return tag;
            }

            public void setTag(TagBean tag) {
                this.tag = tag;
            }

            public int getOnlineNum() {
                return onlineNum;
            }

            public void setOnlineNum(int onlineNum) {
                this.onlineNum = onlineNum;
            }

            public static class TagBean {
                /**
                 * location : 北京
                 * job : 老司机
                 * hobby : 唱歌
                 */

                private String location;
                private String job;
                private String hobby;

                public String getLocation() {
                    return location;
                }

                public void setLocation(String location) {
                    this.location = location;
                }

                public String getJob() {
                    return job;
                }

                public void setJob(String job) {
                    this.job = job;
                }

                public String getHobby() {
                    return hobby;
                }

                public void setHobby(String hobby) {
                    this.hobby = hobby;
                }
            }
        }
    }
}
