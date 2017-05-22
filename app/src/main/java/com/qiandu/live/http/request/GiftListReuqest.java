package com.qiandu.live.http.request;

import com.qiandu.live.http.response.ResList;
import com.qiandu.live.http.response.Response;
import com.qiandu.live.model.GiftInfo;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * @description: 礼物列表请求
 *
 * @author: Andruby
 * @time: 2016/11/2 18:07
 */

/**
 * Created by admin on 2017/4/26.
 */
public class GiftListReuqest extends IRequest {
    public GiftListReuqest(String userId){
        mParams.put("sys","android");
        mParams.put("user_id",userId);
    }
    @Override
    public String getUrl() {
        return  getHost()+"present.php";
    }

    @Override
    public Type getParserType() {
        return new TypeToken<Response<ResList<GiftInfo>>>() {}.getType();
    }
}
