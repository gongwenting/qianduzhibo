package com.qiandu.live.http.request;


import com.qiandu.live.http.response.FanilyDetailResp;
import com.qiandu.live.http.response.Response;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * Created by admin on 2017/4/21.
 */
public class FanilyDetailRequest extends IRequest{
    public FanilyDetailRequest(String userId) {
        mParams.put("act","detail");
        mParams.put("user_id",userId);
    }
    @Override
    public String getUrl() {
        return getHost()+"clan.php";
    }

    @Override
    public Type getParserType() {
        return new TypeToken<Response<FanilyDetailResp>>() {}.getType();
    }
}
