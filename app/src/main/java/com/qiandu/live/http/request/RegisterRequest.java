package com.qiandu.live.http.request;

import com.qiandu.live.model.UserInfo;
import com.qiandu.live.http.response.Response;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * @description: 注册接口请求
 *
 * @author: Andruby
 * @time: 2016/11/2 18:07
 */
public class RegisterRequest extends IRequest {

		public RegisterRequest(String userName,String verifyCode, String password) {
			mParams.put("tel", userName);
			mParams.put("verifycode", verifyCode);
			mParams.put("password", password);
		}

		@Override
		public String getUrl() {
			return getHost() + "signmb.php";
		}

		@Override
		public Type getParserType() {
			return new TypeToken<Response<UserInfo>>() {
			}.getType();
		}
	}
