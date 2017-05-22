package com.qiandu.live.http.request;

import com.qiandu.live.http.response.Response;
import com.qiandu.live.http.response.UploadResp;
import com.google.gson.reflect.TypeToken;
import com.qiandu.live.http.response.UploadRespp;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;


/**
 * @description: 图片上传请求
 *
 * @author: Andruby
 * @time: 2016/11/2 18:07
 */
public class UploadPicRequest extends IRequest {

    public final static int LIVE_COVER_TYPE = 1;
    public final static int USER_HEAD_TYPE = 2;

    public UploadPicRequest(File file) throws FileNotFoundException {
        mParams.put("thumb",file);
    }

    @Override
    public String getUrl() {
        return getHost() + "thumb_upload.php";
    }

    @Override
    public Type getParserType() {
        return new TypeToken<Response<UploadRespp>>() {}.getType();
    }
}