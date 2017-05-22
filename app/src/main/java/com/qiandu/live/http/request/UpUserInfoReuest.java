package com.qiandu.live.http.request;

import com.google.gson.reflect.TypeToken;
import com.qiandu.live.http.response.FanilyDetailResp;
import com.qiandu.live.http.response.Response;

import java.lang.reflect.Type;

/**
 * Created by admin on 2017/5/3.
 */
public class UpUserInfoReuest extends IRequest{
    public UpUserInfoReuest(String nikename,String gender,String desc,String userid ){
        mParams.put("act","edit");
        mParams.put("nickname",nikename);
        mParams.put("gender",gender);
        mParams.put("desc",desc);
        mParams.put("user_id",userid);
    }
    @Override
    public String getUrl() {
        return getHost()+"user_info.php";
    }

    @Override
    public Type getParserType() {
        return new TypeToken<Response>() {}.getType();
    }

}
