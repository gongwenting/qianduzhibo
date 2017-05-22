package com.qiandu.live.model;

import java.io.Serializable;



public class LiveInfo implements Serializable {
    /**
     * @description: 直播信息
     * @author: Andruby
     * @time: 2016/11/4 14:12
     */
        public String userId;
        public String groupId;
        public String liveId;
        public int createTime;
        public int type;
        public int is_atten;
        public int viewCount;
        public int likeCount;
        public String position;
        public int intimacy;
        public String  plugAddressRtmp;
        public String fileId;
        public String liveCover;

        //TCLiveUserInfo
        public TCLiveUserInfo userInfo;


        public class TCLiveUserInfo implements Serializable {
            public String nickname;
            public String headpic;
            public String frontcover;

        }
    }
