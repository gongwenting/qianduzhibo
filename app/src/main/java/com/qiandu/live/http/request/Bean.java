package com.qiandu.live.http.request;

import com.qiandu.live.http.IDontObfuscate;

import java.util.List;


/**
 * Created by admin on 2017/4/28.
 */

public class Bean  extends IDontObfuscate {


    /**
     * code : 100
     * msg : 获取成员列表成功
     * data : [{"username":"雅森木木","avatar":"http://zhibonew.zzsike.com/static/user/avatar20170313115039.jpg","user_id":"443"},{"username":"13126589986","avatar":"http://zhibonew.zzsike.com/static/user/avatar20170313115314.jpg","user_id":"442"},{"username":"15563630808","avatar":"http://zhibonew.zzsike.com/static/user/2017/0424/avatar0300685713.png","user_id":"444"},{"username":"13485900186","avatar":"http://zhibonew.zzsike.com/static/user/avatar20170313114523.jpg","user_id":"445"},{"username":"15138327872","avatar":"http://zhibonew.zzsike.com/static/user/2017/0426/avatar1688658059.png","user_id":"447"},{"username":"15939012510","avatar":"http://zhibonew.zzsike.com/static/user/2017/0425/avatar0861279584.png","user_id":"449"}]
     */

    private int code;
    private String msg;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * username : 雅森木木
         * avatar : http://zhibonew.zzsike.com/static/user/avatar20170313115039.jpg
         * user_id : 443
         */

        private String username;
        private String avatar;
        private String user_id;

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

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }
    }
}
