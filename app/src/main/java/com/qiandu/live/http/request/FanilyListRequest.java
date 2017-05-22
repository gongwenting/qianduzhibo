package com.qiandu.live.http.request;



import com.qiandu.live.http.response.Response;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * Created by admin on 2017/4/21.
 */
public class FanilyListRequest extends IRequest {
    public FanilyListRequest(String act,String userId){
        mParams.put("act",act);
        mParams.put("user_id",userId);
    }
    @Override
    public String getUrl() {
        return getHost()+"clan.php";
    }

    @Override
    public Type getParserType() {
        return new TypeToken<Response>() {}.getType();
    }
}
