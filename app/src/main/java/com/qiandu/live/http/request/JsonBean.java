package com.qiandu.live.http.request;

import java.util.List;

/**
 * Created by admin on 2017/5/3.
 */

public class JsonBean {

    /**
     * code : 100
     * msg : 获取成员列表成功
     * data : [{"username":"90000009009","avatar":"http://zhibonew.zzsike.com/static/robot/9009.jpg","user_id":"9009"},{"username":"90000009008","avatar":"http://zhibonew.zzsike.com/static/robot/9008.jpg","user_id":"9008"},{"username":"90000009007","avatar":"http://zhibonew.zzsike.com/static/robot/9007.jpg","user_id":"9007"},{"username":"90000009006","avatar":"http://zhibonew.zzsike.com/static/robot/9006.jpg","user_id":"9006"},{"username":"90000009005","avatar":"http://zhibonew.zzsike.com/static/robot/9005.jpg","user_id":"9005"},{"username":"90000009004","avatar":"http://zhibonew.zzsike.com/static/robot/9004.jpg","user_id":"9004"},{"username":"90000009003","avatar":"http://zhibonew.zzsike.com/static/robot/9003.jpg","user_id":"9003"},{"username":"90000009002","avatar":"http://zhibonew.zzsike.com/static/robot/9002.jpg","user_id":"9002"},{"username":"90000009000","avatar":"http://zhibonew.zzsike.com/static/robot/9000.jpg","user_id":"9000"},{"username":"90000009001","avatar":"http://zhibonew.zzsike.com/static/robot/9001.jpg","user_id":"9001"}]
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
         * username : 90000009009
         * avatar : http://zhibonew.zzsike.com/static/robot/9009.jpg
         * user_id : 9009
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
