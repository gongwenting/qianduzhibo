package com.qiandu.live.http.request;

import com.google.gson.reflect.TypeToken;
import com.qiandu.live.http.response.Response;

import java.lang.reflect.Type;

/**
 * Created by admin on 2017/5/18.
 */

public class ForgetPswdRequest extends IRequest {

    public ForgetPswdRequest(String mobile, String vertifycode, String newPswd) {
        mParams.put("tel", mobile);
        mParams.put("verifycode", vertifycode);
        mParams.put("password", newPswd);
    }

    @Override
    public String getUrl() {
        return getHost() + "find_pass.php";
    }

    @Override
    public Type getParserType() {
        return new TypeToken<Response>() {
        }.getType();
    }
}
