package com.qiandu.live.http.response;

import java.io.Serializable;

/**
 * Created by admin on 2017/5/11.
 */
public class BanerResp implements Serializable {

    /**
     * title : 千度开播轮播
     * url :
     * image : http://www.qianduzhibo.com/static/team/2017/0511/14944864116322.jpg
     */

    private String title;
    private String url;
    private String image;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
