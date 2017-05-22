package com.qiandu.live.http.request;


import com.qiandu.live.http.response.Response;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * Created by admin on 2017/4/19.
 */
public class GuanzhuListendRequest extends IRequest{
    public GuanzhuListendRequest(String userId, String partnerId, String act){
        mParams.put("user_id",userId);
        mParams.put("live_id",partnerId);
        mParams.put("act",act);
    }


    @Override
    public String getUrl() {
        return getHost()+"attention.php";
    }

    @Override
    public Type getParserType() {
        return new TypeToken<Response<GuanzhuListendResp>>() {}.getType();
    }
}
