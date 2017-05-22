package com.qiandu.live.model;

import java.io.Serializable;

/**
 * Created by admin on 2017/5/16.
 */
public class LeaveMsgInfo implements Serializable {

    /**
     * id : 77
     * send_id : 4465
     * acce_id : 68502
     * create_time : 2017-05-16 14:02
     * content : ggggggggggggggggggggggggggggg
     * avatar : http://www.qianduzhibo.com/static/user/2017/0512/avatar5862601869.jpeg
     */

    private String id;
    private String send_id;
    private String acce_id;
    private String create_time;
    private String content;
    private String avatar;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSend_id() {
        return send_id;
    }

    public void setSend_id(String send_id) {
        this.send_id = send_id;
    }

    public String getAcce_id() {
        return acce_id;
    }

    public void setAcce_id(String acce_id) {
        this.acce_id = acce_id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
