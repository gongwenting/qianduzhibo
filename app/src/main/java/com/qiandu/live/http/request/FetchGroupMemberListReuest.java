package com.qiandu.live.http.request;

import com.qiandu.live.http.response.ResList;
import com.qiandu.live.http.response.Response;
import com.qiandu.live.model.SimpleUserInfo;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * @description: 观众列表请求
 *
 * @author: Andruby
 * @time: 2016/11/2 18:07
 */
public class FetchGroupMemberListReuest extends IRequest {

    public FetchGroupMemberListReuest(String groupId) {
        mParams.put("group_id", "@TGS#aGND3XYES");
    }

    @Override
    public String getUrl() {
        return getHost() + "group_live_list.php";
    }

    @Override
    public Type getParserType() {
        return new TypeToken<Response<SimpleUserInfo>>() {
        }.getType();
    }
}
