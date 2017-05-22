package com.qiandu.live.http.response;

import com.qiandu.live.http.IDontObfuscate;

/**
 * Created by admin on 2017/5/16.
 */
// TODO: 2017/5/16
public class JudgmentResp extends IDontObfuscate {

    /**
     * id : 444
     * username : 15563630808
     * avatar : http://www.qianduzhibo.com/static/user/2017/0504/avatar8581015219.jpeg
     * miaoshu : 范德萨范德萨发夺三
     * nickname : dazui
     * gender : 男
     * coin : 46519636
     * is_liver : 1
     */

    private String id;
    private String username;
    private String avatar;
    private String miaoshu;
    private String nickname;
    private String gender;
    private String coin;
    private int is_liver;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getMiaoshu() {
        return miaoshu;
    }

    public void setMiaoshu(String miaoshu) {
        this.miaoshu = miaoshu;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public int getIs_liver() {
        return is_liver;
    }

    public void setIs_liver(int is_liver) {
        this.is_liver = is_liver;
    }
}
