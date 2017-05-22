package com.qiandu.live.http.request;

import com.qiandu.live.http.response.ResList;
import com.qiandu.live.http.response.Response;
import com.google.gson.reflect.TypeToken;
import com.qiandu.live.model.LiveInfo;

import java.lang.reflect.Type;

/**
 * @description: 直播列表请求
 *
 * @author: Andruby
 * @time: 2016/11/2 18:07
 */
public class LiveListRequest extends IRequest {
	private int mListType;
	public LiveListRequest(String userId, int listType){
		mParams.put("user_id",userId);
		mListType = listType;
	}

	@Override
	public String getUrl() {
		if (mListType == 0){
			return getHost()+"live_list.php?act=atten_list";
		}else if (mListType == 1){
			return getHost()+"live_list.php?act=hot_list";
		}else if (mListType == 2){
			return getHost()+"live_list.php";
		}
		return getHost()+"live_list.php";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<ResList<LiveInfo>>>() {}.getType();
	}
}
