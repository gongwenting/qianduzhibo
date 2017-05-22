package com.qiandu.live.http.request;

import com.qiandu.live.http.response.CreateLiveResp;
import com.qiandu.live.http.response.Response;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;


/**
 * @description: 创建直播接口请求
 * @author: Andruby
 * @time: 2016/11/2 18:07
 */

/**
 * @description: 创建直播接口请求
 * @author: Andruby
 * @time: 2016/11/2 18:07
 */
public class CreateLiveRequest extends IRequest {

    public CreateLiveRequest(String userId, String groupId, String title, String liveCover,String position) {
        mParams.put("user_id", userId);
        mParams.put("group_id", groupId);
        mParams.put("title", title);
        mParams.put("thumb", liveCover);
        mParams.put("position",position);
    }

    @Override
    public String getUrl() {
        return getHost() + "getLiveInfo.php";
    }

    @Override
    public Type getParserType() {
        return new TypeToken<Response<CreateLiveResp>>() {
        }.getType();
    }
}
