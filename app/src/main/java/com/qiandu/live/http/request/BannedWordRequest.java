package com.qiandu.live.http.request;

import com.google.gson.reflect.TypeToken;
import com.qiandu.live.http.response.BanerResp;
import com.qiandu.live.http.response.ResList;
import com.qiandu.live.http.response.Response;

import java.lang.reflect.Type;

/**
 * Created by admin on 2017/5/17.
 */
public class BannedWordRequest extends IRequest {

    public BannedWordRequest(String user_id, String live_id) {
        mParams.put("user_id",user_id);
        mParams.put("live_id",live_id);
    }

    @Override
    public String getUrl() {
        return getHost()+"make_silent.php";
    }

    @Override
    public Type getParserType() {
        return new TypeToken<Response>() {
        }.getType();
    }
}
