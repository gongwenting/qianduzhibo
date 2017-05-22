package com.qiandu.live.http.response;

/**
 * @description: 上传管理类
 *
 *
 * @author: Andruby
 * @time: 2016/11/4 14:12
 */
public class UploadResp {
    String avatar;

    public String getUrl() {
        return avatar;
    }

    public void setUrl(String url) {
        this.avatar = url;
    }
}
