package com.qiandu.live.http.request;

import com.qiandu.live.http.response.ResList;
import com.qiandu.live.http.response.Response;
import com.qiandu.live.model.LiveInfo;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * @description: 退出直播请求
 * @author: Andruby
 * @time: 2016/11/2 18:07
 */
public class QuitGroupRequest extends IRequest {
    /**
     * 退出群组
     *
     * @param userId  用户ID
     * @param groupId 群组ID
     */
    public QuitGroupRequest(int requestId, String userId, String liveId, String hostId, String groupId) {
        mRequestId = requestId;
        mParams.put("action", "QuitGroup");
        mParams.put("userId", userId);
        mParams.put("hostId", hostId);
        mParams.put("liveId", liveId);
        mParams.put("groupId", groupId);
    }

    @Override
    public String getUrl() {
        return getHost() + "Live";
    }

    @Override
    public Type getParserType() {
        return new TypeToken<Response<ResList<LiveInfo>>>() {
        }.getType();
    }
}
