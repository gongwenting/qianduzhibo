package com.qiandu.live.http.response;

import com.qiandu.live.http.IDontObfuscate;


/**
 * @description: 创建直播返回
 *
 * @author: Andruby
 * @time: 2016/11/2 18:07
 */
public class CreateLiveResp  extends IDontObfuscate {


    private int liveok;
    private String logId;
    private String liveurl;

    public String getPushUrl() {
        return liveurl;
    }

    public void setPushUrl(String pushUrl) {
        this.liveurl = pushUrl;
    }
    public int getLiveok() {
        return liveok;
    }

    public void setLiveok(int liveok) {
        this.liveok = liveok;
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }
}
