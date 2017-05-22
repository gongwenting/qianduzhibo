package com.qiandu.live.http.request;

import com.google.gson.reflect.TypeToken;
import com.qiandu.live.http.response.Response;

import java.lang.reflect.Type;

/**
 * Created by admin on 2017/5/20.
 */
public class ReportUserRequest extends IRequest {
    public ReportUserRequest(String reporId, String reportedId) {
        mParams.put("act", "report");
        mParams.put("report_id", reporId);
        mParams.put("reported_id", reportedId);
    }

    @Override
    public String getUrl() {
        return getHost() + "report.php";
    }

    @Override
    public Type getParserType() {
        return new TypeToken<Response>() {
        }.getType();
    }
}
