package com.qiandu.live.http.request;

import com.google.gson.reflect.TypeToken;
import com.qiandu.live.http.response.Response;

import java.lang.reflect.Type;

/**
 * Created by admin on 2017/5/9.
 */
public class LeaveMsgRequest extends IRequest {

    public LeaveMsgRequest(String send  ,String send_id,String acce_id,String content) {
        mParams.put("act", send);
        mParams.put("send_id", send_id);
        mParams.put("acce_id", acce_id);
        mParams.put("content", content);
    }

    @Override
    public String getUrl() {
        return getHost() + "private_msg.php";
    }

    @Override
    public Type getParserType() {
        return new TypeToken<Response>() {
        }.getType();
    }
}
