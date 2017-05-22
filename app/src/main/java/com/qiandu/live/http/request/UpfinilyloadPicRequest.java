package com.qiandu.live.http.request;

import com.qiandu.live.http.response.Response;
import com.qiandu.live.http.response.UpfinilyResp;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;

/**
 * Created by admin on 2017/4/21.
 */
public class UpfinilyloadPicRequest extends IRequest {

    public UpfinilyloadPicRequest(String userid,File file) throws FileNotFoundException {
        mParams.put("user_id",userid);
        mParams.put("clan",file);
    }

    @Override
    public String getUrl() {
        return getHost() + "thumb_upload.php";
    }

    @Override
    public Type getParserType() {
        return new TypeToken<Response<UpfinilyResp>>() {}.getType();
    }
}