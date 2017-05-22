package com.qiandu.live.model;


/**
 * @description: 成员信息
 *
 * @author: Andruby
 * @time: 2016/11/4 14:12
 */
public class GroupMemberInfo {
    private String userId;
    private String nickname;
    private String faceUrl;

    public GroupMemberInfo(String userId, String nickname, String faceUrl) {
        this.userId = userId;
        this.nickname = nickname;
        this.faceUrl = faceUrl;
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

    public String getFaceUrl() {
        return faceUrl;
    }

    public void setFaceUrl(String faceUrl) {
        this.faceUrl = faceUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GroupMemberInfo)) return false;

        GroupMemberInfo that = (GroupMemberInfo) o;

        if (getUserId() != null ? !getUserId().equals(that.getUserId()) : that.getUserId() != null)
            return false;
        if (getNickname() != null ? !getNickname().equals(that.getNickname()) : that.getNickname() != null)
            return false;
        return getFaceUrl() != null ? getFaceUrl().equals(that.getFaceUrl()) : that.getFaceUrl() == null;

    }

    @Override
    public int hashCode() {
        int result = getUserId() != null ? getUserId().hashCode() : 0;
        result = 31 * result + (getNickname() != null ? getNickname().hashCode() : 0);
        result = 31 * result + (getFaceUrl() != null ? getFaceUrl().hashCode() : 0);
        return result;
    }
}