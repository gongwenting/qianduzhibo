package com.qiandu.live.utils;

import android.content.Context;

import com.qiandu.live.model.LeaveMsgInfo;
import com.qiandu.live.model.SimpleUserInfo;

import java.util.List;

/**
 * Created by admin on 2017/5/15.
 */
public class ListCacheUtils {
    private static Context context;

    private static List<SimpleUserInfo> simpleUserInfoList;
    private static List<LeaveMsgInfo> leaveMsgInfoList;

    public static List<LeaveMsgInfo> getLeaveMsgInfoList() {
        return leaveMsgInfoList;
    }

    public static void setLeaveMsgInfoList(List<LeaveMsgInfo> leaveMsgInfoList) {
        ListCacheUtils.leaveMsgInfoList = leaveMsgInfoList;
    }

    public static List<SimpleUserInfo> getResult() {
//        if (result == null){
//            result =
//        }
        return simpleUserInfoList;
    }

    public static void setResult(List<SimpleUserInfo> result) {
        ListCacheUtils.simpleUserInfoList = result;
    }


}
