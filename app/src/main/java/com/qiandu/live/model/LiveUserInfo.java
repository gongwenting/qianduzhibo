package com.qiandu.live.model;


/**
 * @description: 直播打赏状态信息
 *
 * @author: Andruby
 * @time: 2016/11/4 14:12
 */
public class LiveUserInfo {
    private String userId;
    private String userImage;
    private String nickname;
    private long charmCount;
    private long sybCount;

    public LiveUserInfo(String userId, String nickname, String userImage) {
        this.userId = userId;
        this.nickname = nickname;
        this.userImage = userImage;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public long getCharmCount() {
        return charmCount;
    }

    public void setCharmCount(long charmCount) {
        this.charmCount = charmCount;
    }

    public long getSybCount() {
        return sybCount;
    }

    public void setSybCount(long sybCount) {
        this.sybCount = sybCount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
