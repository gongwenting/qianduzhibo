package com.qiandu.live.http.request;

import com.qiandu.live.http.response.ResList;
import com.qiandu.live.http.response.Response;
import com.qiandu.live.model.SimpleUserInfo;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * @description: 观众列表请求
 * @author: Andruby
 * @time: 2016/11/2 18:07
 */
public class GroupMemberReuest extends IRequest {

    public GroupMemberReuest(String groupId) {
        mParams.put("sys","android");
        mParams.put("group_id", groupId);

    }

    @Override
    public String getUrl() {
        return getHost() + "group_live_list.php";
    }

    @Override
    public Type getParserType() {
        return new TypeToken<Response<ResList<SimpleUserInfo>>>() {
        }.getType();
    }
}
