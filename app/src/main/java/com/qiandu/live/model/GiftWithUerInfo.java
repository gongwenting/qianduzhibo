package com.qiandu.live.model;


import java.io.Serializable;


/**
 * @description: 用户礼物信息
 *
 * @author: Andruby
 * @time: 2016/11/4 14:12
 */
public class GiftWithUerInfo  implements Serializable {
    private GiftInfo giftInfo;
    private LiveUserInfo userInfo;
    private HostInfocharmCount anchorInfo;
    private String roomId;
    private int giftCount;

    public int getGiftCount() {
        return giftCount;
    }

    public void setGiftCount(int giftCount) {
        this.giftCount = giftCount;
    }

    public LiveUserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(LiveUserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public GiftInfo getGiftInfo() {
        return giftInfo;
    }

    public void setGiftInfo(GiftInfo giftInfo) {
        this.giftInfo = giftInfo;
    }

    public HostInfocharmCount getAnchorInfo() {
        return anchorInfo;
    }

    public void setAnchorInfo(HostInfocharmCount anchorInfo) {
        this.anchorInfo = anchorInfo;
    }
}
