package com.qiandu.live.http.request;

import com.google.gson.reflect.TypeToken;
import com.qiandu.live.http.response.Response;

import java.lang.reflect.Type;

/**
 * Created by admin on 2017/5/18.
 */

public class ForgetPswdSMSRequest extends IRequest  {
    public ForgetPswdSMSRequest(String mobile) {
        mParams.put("tel", mobile);
    }

    @Override
    public String getUrl() {
        return getHost() + "find_pass_sms.php";
    }

    @Override
    public Type getParserType() {
        return new TypeToken<Response>() {
        }.getType();
    }
}
