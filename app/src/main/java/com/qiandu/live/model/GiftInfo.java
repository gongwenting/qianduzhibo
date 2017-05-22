package com.qiandu.live.model;


import java.io.Serializable;

/**
 * @description: 礼物详情
 *
 * @author: Andruby
 * @time: 2016/11/4 14:12
 */
public class GiftInfo implements Serializable {


    /**
     * id : 50
     * name : 别墅
     * cost : 9999
     * rewardMsg :
     * type :
     * smpicUrl : http://zhibonew.zzsike.com/static/goods/2017/0504/sm_image8856574145.png
     * bigpicUrl : http://zhibonew.zzsike.com/static/team/2017/0427/14932810726803.jpg
     */

    private String id;
    private String name;
    private int cost;
    private String rewardMsg;
    private String type;
    private String smpicurl;
    private String bigpicurl;
    private int giftCount;//送出的礼物数

    private int limitTime = 10;//送出的礼物数
    private int CurrentTime;//送出的礼物数

    public int getLimitCount() {
        return limitCount;
    }

    public void setLimitCount(int limitCount) {
        this.limitCount = limitCount;
    }

    public int getGiftCount() {
        return giftCount;
    }

    public void setGiftCount(int giftCount) {
        this.giftCount = giftCount;
    }

    public int getLimitTime() {
        return limitTime;
    }

    public void setLimitTime(int limitTime) {
        this.limitTime = limitTime;
    }

    public int getCurrentTime() {
        return CurrentTime;
    }

    public void setCurrentTime(int currentTime) {
        CurrentTime = currentTime;
    }

    public int getCurrentCount() {
        return CurrentCount;
    }

    public void setCurrentCount(int currentCount) {
        CurrentCount = currentCount;
    }

    public boolean isCountDown() {
        return isCountDown;
    }

    public void setCountDown(boolean countDown) {
        isCountDown = countDown;
    }

    private int limitCount = 3;//送出的礼物数
    private int CurrentCount;//送出的礼物数
    private boolean isCountDown;//送出的礼物数

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getRewardMsg() {
        return rewardMsg;
    }

    public void setRewardMsg(String rewardMsg) {
        this.rewardMsg = rewardMsg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSmpicUrl() {
        return smpicurl;
    }

    public void setSmpicUrl(String smpicUrl) {
        this.smpicurl = smpicUrl;
    }

    public String getBigpicUrl() {
        return bigpicurl;
    }

    public void setBigpicUrl(String bigpicUrl) {
        this.bigpicurl = bigpicUrl;
    }

    @Override
    public String toString() {
        return "GiftInfo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", cost='" + cost + '\'' +
                ", rewardMsg='" + rewardMsg + '\'' +
                ", type='" + type + '\'' +
                ", smpicUrl='" + smpicurl + '\'' +
                ", bigpicUrl='" + bigpicurl + '\'' +
                '}';
    }
}
