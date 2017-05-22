package com.qiandu.live.http.request;

import com.google.gson.reflect.TypeToken;
import com.qiandu.live.http.response.JudgmentResp;
import com.qiandu.live.http.response.Response;

import java.lang.reflect.Type;

/**
 * Created by admin on 2017/5/16.
 */
// TODO: 2017/5/16
public class JudgmentRequest extends IRequest {
    public JudgmentRequest(String gain, String userId){
        mParams.put("act",gain);
        mParams.put("user_id",userId);
    }
    @Override
    public String getUrl() {
        return getHost()+"user_info.php";
    }

    @Override
    public Type getParserType() {
        return new TypeToken<Response<JudgmentResp>>() {}.getType();
    }
}
