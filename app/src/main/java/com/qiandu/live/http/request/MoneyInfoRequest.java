package com.qiandu.live.http.request;

import com.google.gson.reflect.TypeToken;
import com.qiandu.live.http.response.ResList;
import com.qiandu.live.http.response.Response;
import com.qiandu.live.http.response.WealthCoinInfo;

import java.lang.reflect.Type;

/**
 * Created by admin on 2017/5/17.
 */
public class MoneyInfoRequest extends IRequest {

    public MoneyInfoRequest(String userId) {
        mParams.put("sys", "android");
        mParams.put("user_id", userId);
    }

    @Override
    public String getUrl() {
        return getHost()+"treasure.php";
    }

    @Override
    public Type getParserType() {
        return new TypeToken<Response<ResList<WealthCoinInfo>>>() {
        }.getType();
    }
}
