package com.qiandu.live.model;
/**
 * @description: 礼物实体类
 *
 * @author: Andruby
 * @time: 2016/11/4 14:12
 */
public class GiftVo {

    private String userId;//送礼物人的UserId
    private String userName;//送礼物人的Name
    private String userImage;//送礼物人的头像
    private int count;//送礼物的个数
    private String msg;//随着礼物的消息，目前无用
    // private int giftType = GiftTypeAndDrawableRes.LIVE_GIFT_ZSGOLD;//礼物类型  在GiftTypeAndDrawableRes中
    private long getGiftTimeTag; //获取到礼物的事件戳
    private int jumpNum;    //计数跳跃数值
    private int showNum;   //展示的数字
    private GiftInfo mGiftInfo;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getGetGiftTimeTag() {
        return getGiftTimeTag;
    }

    public void setGetGiftTimeTag(long getGiftTimeTag) {
        this.getGiftTimeTag = getGiftTimeTag;
    }

    public int getJumpNum() {
        return jumpNum;
    }

    public void setJumpNum(int jumpNum) {
        this.jumpNum = jumpNum;
    }

    public int getShowNum() {
        return showNum;
    }

    public void setShowNum(int showNum) {
        this.showNum = showNum;
    }

    public GiftInfo getGiftInfo() {
        return mGiftInfo;
    }

    public void setGiftInfo(GiftInfo giftInfo) {
        mGiftInfo = giftInfo;
    }
}
