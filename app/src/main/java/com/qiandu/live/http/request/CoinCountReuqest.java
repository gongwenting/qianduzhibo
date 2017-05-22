package com.qiandu.live.http.request;

import com.qiandu.live.http.response.Response;
import com.qiandu.live.model.CoinCount;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * @description: 金币数量
 * @author: Andruby
 * @time: 2016/11/2 18:07
 */
public class CoinCountReuqest extends IRequest{
    public CoinCountReuqest(String userId){
        mParams.put("user_id", userId);

    }
    @Override
    public String getUrl() {
        return getHost()+"money_score.php";
    }

    @Override
    public Type getParserType() {
        return new TypeToken<Response<CoinCount>>() {}.getType();
    }
}

