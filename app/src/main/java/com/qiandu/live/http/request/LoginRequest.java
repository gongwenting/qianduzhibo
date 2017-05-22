package com.qiandu.live.http.request;


import com.qiandu.live.http.response.Response;
import com.qiandu.live.model.UserInfo;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * @description: 登陆请求
 * @author: Andruby
 * @time: 2016/11/2 18:07
 */
public class LoginRequest extends IRequest {

	/**
	 *
	 * @param userName
	 * @param password
	 */
	public LoginRequest(String userName, String password) {
		mParams.put("pwuser", userName);
		mParams.put("pwpwd", password);

	}

	@Override
	public String getUrl() {
		return getHost() + "login.php";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<UserInfo>>() {
		}.getType();
	}

	@Override
	public boolean cleanCookie() {
		return true;
	}
}
