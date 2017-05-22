package com.qiandu.live.model;


/**
 * @description: 用户基本信息封装 id、nickname、faceurl
 *
 * @author: Andruby
 * @time: 2016/11/4 14:12
 */
public class SimpleUserInfo {

    public String user_id;
    public String username;
    public String avatar;
    public String nickname;

    public SimpleUserInfo(String userId, String username, String avatar) {
        this.user_id = userId;
        this.username = username;
        this.avatar = avatar;
    }
}
