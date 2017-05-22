package com.qiandu.live.http.request;

import com.google.gson.reflect.TypeToken;
import com.qiandu.live.http.response.Ainformation;
import com.qiandu.live.http.response.FanilyDetailResp;
import com.qiandu.live.http.response.Response;

import java.lang.reflect.Type;

/**
 * Created by admin on 2017/5/11.
 */
public class QinMiduReuqest extends  IRequest{
    public QinMiduReuqest(String liveId){
        mParams.put("live_id",liveId);

    }
    @Override
    public String getUrl() {
        return getHost()+"live_info.php";
    }

    @Override
    public Type getParserType() {

        return new TypeToken<Response<Ainformation>>() {}.getType();

    }
}
