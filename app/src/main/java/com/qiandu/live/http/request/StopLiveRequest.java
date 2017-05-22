package com.qiandu.live.http.request;

import com.qiandu.live.http.response.ResList;
import com.qiandu.live.http.response.Response;
import com.qiandu.live.model.LiveInfo;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * @description: 停止直播请求
 *
 * @author: Andruby
 * @time: 2016/11/2 18:07
 */
public class StopLiveRequest extends IRequest {

    public StopLiveRequest(String userId) {
        mParams.put("user_id",userId);

    }

    @Override
    public String getUrl() {
        return getHost()+"getLiveInfo.php";
    }

    @Override
    public Type getParserType() {
        return new TypeToken<Response<ResList<LiveInfo>>>() {}.getType();
    }
}
