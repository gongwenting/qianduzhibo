package com.qiandu.live.model;

import java.io.Serializable;

/**
 * author : qubian on 2016/12/26 15:07
 * description :
 */

public class CommUserInfo implements Serializable{
    private String userId;
    private String nickname;
    private String headPic;
    private int sex;

    public CommUserInfo(String userId, String nickname, String headPic) {
        this.userId = userId;
        this.nickname = nickname;
        this.headPic = headPic;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
