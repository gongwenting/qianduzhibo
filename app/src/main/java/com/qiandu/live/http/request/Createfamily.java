package com.qiandu.live.http.request;

import com.qiandu.live.http.response.Response;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * Created by admin on 2017/4/21.
 */
public class Createfamily extends IRequest{
    /**
     *
     * 创建家族
     *
     * @return
     */
public Createfamily(String act, String userId, String logo, String title, String declaration){
    mParams.put("act",act);
    mParams.put("user_id",userId);
    mParams.put("clan_logo",logo);
    mParams.put("clan_name",title);
    mParams.put("clan_desc",declaration);
}

    @Override
    public String getUrl() {
        return getHost()+"clan.php";
    }

    @Override
    public Type getParserType() {
        return new TypeToken<Response>(){}.getType();
    }
}
