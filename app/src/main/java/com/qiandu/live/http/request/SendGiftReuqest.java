package com.qiandu.live.http.request;


import com.google.gson.reflect.TypeToken;
import com.qiandu.live.http.response.Response;

import java.lang.reflect.Type;

/**
 * @description: 发送礼物请求
 *
 * @author: Andruby
 * @time: 2016/11/2 18:07
 */
public class SendGiftReuqest extends IRequest {

    public SendGiftReuqest(String userId , String liveId, String giftId, int count) {

        mParams.put("user_id",userId);
        mParams.put("live_id", liveId);
        mParams.put("goods_id", giftId);
        mParams.put("count", count);
    }

    @Override
    public String getUrl() {
        return getHost() + "send_present.php";
    }

    @Override
    public Type getParserType() {
        return new TypeToken<Response>() {}.getType();
    }
}
