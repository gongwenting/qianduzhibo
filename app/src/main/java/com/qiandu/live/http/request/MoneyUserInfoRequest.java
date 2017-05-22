package com.qiandu.live.http.request;


import com.google.gson.reflect.TypeToken;
import com.qiandu.live.http.response.MoneyUsetInfo;
import com.qiandu.live.http.response.Response;

import java.lang.reflect.Type;

/**
 * Created by admin on 2017/5/18.
 */

public class MoneyUserInfoRequest extends IRequest {
    public MoneyUserInfoRequest(String userId) {
        mParams.put("user_id", userId);
        mParams.put("act", "gain");
    }

    @Override
    public String getUrl() {
        return getHost()+"user_info.php?";
    }

    @Override
    public Type getParserType() {
        return new TypeToken<Response<MoneyUsetInfo>>() {
        }.getType();
    }
}
