package com.qiandu.live.http.request;

import android.util.Log;

import com.qiandu.live.http.response.Response;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * @description: 注册接口请求
 * @author: Andruby
 * @time: 2016/11/2 18:07
 */
public class VerifyCodeRequest extends IRequest {

	public VerifyCodeRequest(String mobile) {
		mParams.put("tel", mobile);
		Log.d("tel","mobile+"+mobile);
	}

	@Override
	public String getUrl() {
		return getHost() + "sms.php";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response>() {
		}.getType();
	}
}
