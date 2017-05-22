package com.qiandu.live.http.request;

import com.qiandu.live.http.response.Response;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * Created by admin on 2017/4/22.
 */
public class KeepFailyRequest extends IRequest{
    public KeepFailyRequest(String userId, String clanLogo, String clanName, String clanDesc){
        mParams.put("act","edit");
        mParams.put("user_id",userId);
        mParams.put("clan_logo",clanLogo);
        mParams.put("clan_name",clanName);
        mParams.put("clan_desc",clanDesc);

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
