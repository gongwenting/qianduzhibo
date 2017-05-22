package com.qiandu.live.http.response;

/**
 * Created by admin on 2017/5/18.
 */

public class WealthUserInfo {

    /**
     * id : 4465
     * username : 18638865125
     * avatar : http://www.qianduzhibo.com/static/user/2017/0512/avatar5862601869.jpeg
     * miaoshu :
     * nickname : 亲爱的舒舒
     * gender : 女
     * coin : 5354933
     * balance : 39784
     * is_liver : 1
     */

    private String id;
    private String username;
    private String avatar;
    private String miaoshu;
    private String nickname;
    private String gender;
    private String coin;
    private String balance;
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

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public int getIs_liver() {
        return is_liver;
    }

    public void setIs_liver(int is_liver) {
        this.is_liver = is_liver;
    }
}
