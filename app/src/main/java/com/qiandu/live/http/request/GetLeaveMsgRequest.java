package com.qiandu.live.http.request;

import com.google.gson.reflect.TypeToken;
import com.qiandu.live.http.response.ResList;
import com.qiandu.live.http.response.Response;
import com.qiandu.live.model.LeaveMsgInfo;

import java.lang.reflect.Type;

/**
 * Created by admin on 2017/5/16.
 */
public class GetLeaveMsgRequest extends IRequest {

    public GetLeaveMsgRequest(String userId) {
        mParams.put("act","gain");
        mParams.put("user_id",userId);
        mParams.put("sys","android");
    }

    @Override
    public String getUrl() {
        return  getHost()+"private_msg.php?";
    }

    @Override
    public Type getParserType() {
        return new TypeToken<Response<ResList<LeaveMsgInfo>>>() {}.getType();
    }
}
