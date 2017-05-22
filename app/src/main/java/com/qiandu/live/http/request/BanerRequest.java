package com.qiandu.live.http.request;

import com.google.gson.reflect.TypeToken;
import com.qiandu.live.http.response.BanerResp;
import com.qiandu.live.http.response.ResList;
import com.qiandu.live.http.response.Response;

import java.lang.reflect.Type;

/**
 * Created by admin on 2017/5/11.
 */
public class BanerRequest extends IRequest {
    public BanerRequest(String app,String android){
        mParams.put("position",app);
        mParams.put("sys",android);
    }
    @Override
    public String getUrl() {
        return getHost()+"banner_list.php";
    }

    @Override
    public Type getParserType() {
        return new TypeToken<Response<ResList<BanerResp>>>() {}.getType();
    }
}
