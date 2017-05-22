package com.qiandu.live.http.request;

import com.google.gson.reflect.TypeToken;
import com.qiandu.live.http.response.CreateLiveResp;
import com.qiandu.live.http.response.Oderstring;
import com.qiandu.live.http.response.Response;

import java.lang.reflect.Type;

/**
 * Created by admin on 2017/5/2.
 */
public class PayRequest extends IRequest{
    public PayRequest(String user_id,String subject ,String money){
        mParams.put("user_id",user_id);
        mParams.put("subject",subject);
        mParams.put("money",money);

    }
    @Override
    public String getUrl() {
        return "http://qianduzhibo.com/api/qiandupay/alipay.php";
    }

    @Override
    public Type getParserType() {
        return  new TypeToken<Response<Oderstring>>() {
        }.getType();
    }
}
