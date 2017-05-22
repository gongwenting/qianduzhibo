package com.qiandu.live.model;

import android.content.Context;
import android.text.TextUtils;

import com.qiandu.live.http.IDontObfuscate;
import com.qiandu.live.utils.AsimpleCache.ACache;
import com.qiandu.live.utils.Constants;

/**
 * @description: 存储用户信息
 * @author: Andruby
 * @time: 2016/10/31 18:07
 */
public class UserInfoCache extends IDontObfuscate {

    public static void saveCache(Context context, UserInfo info) {
        //   Log.i(UserInfoCache.class.getSimpleName(), "saveCache: info.headPicSmall = " + info.headPicSmall);
        ACache.get(context).put("user_id", info.userid);
        ACache.get(context).put("username",info.username);
        ACache.get(context).put("nickname", info.nickname);
        ACache.get(context).put("head_pic", info.avatar);
        ACache.get(context).put("is_atten",info.isatten);
        ACache.get(context).put("desc",info.desc);

        ACache.get(context).put("gender",info.gender);
        //  ACache.get(context).put("head_pic_small", info.headPicSmall);
        ACache.get(context).put("sig_id", info.sigId);
//        ACache.get(context).put("token", info.token);
        ACache.get(context).put("sdk_app_id", info.sdkAppId);
        ACache.get(context).put("adk_account_type", info.sdkAccountType);


        if (info.sdkAppId != null && TextUtils.isDigitsOnly(info.sdkAppId)) {
            Constants.IMSDK_APPID = Integer.parseInt(info.sdkAppId);
        }
        if (info.sdkAccountType != null && TextUtils.isDigitsOnly(info.sdkAccountType)) {
            Constants.IMSDK_ACCOUNT_TYPE = Integer.parseInt(info.sdkAccountType);
        }
    }
//    public static void saveUploadMgr(Context context, UploadResp up) {
//        ACache.get(context).put("thumb", up.getUrl());
//    }
    public static String getUserId(Context context) {
        return ACache.get(context).getAsString("user_id");
    }
    public static String getIsatten(Context context) {
        return ACache.get(context).getAsString("is_atten");
    }
    public static String getThumb(Context context){
        return  ACache.get(context).getAsString("thumb");
    }
    public static String getNickname(Context context) {
        return ACache.get(context).getAsString("nickname");
    }
    public static  String getUsername(Context context){
        return  ACache.get(context).getAsString("username");
    }

    public static String getHeadPic(Context context) {
        return ACache.get(context).getAsString("head_pic");
    }

    public static String getSigId(Context context) {
        return ACache.get(context).getAsString("sig_id");
    }

    public static String getDesc(Context context) {
        return ACache.get(context).getAsString("desc");
    }
    public static String getGender(Context context){
        return  ACache.get(context).getAsString("gender");
    }
    public static String getaToken(Context context) {
        return ACache.get(context).getAsString("token");
    }

    public static String getSdkAppId(Context context) {
        return ACache.get(context).getAsString("sdk_app_id");
    }

    public static String getAccountType(Context context) {
        return ACache.get(context).getAsString("adk_account_type");
    }


    public static void clearCache(Context context) {
        ACache.get(context).remove("user_id");
        ACache.get(context).remove("username");
        ACache.get(context).remove("nickname");
        ACache.get(context).remove("head_pic");
        ACache.get(context).remove("is_atten");
        ACache.get(context).remove("sig_id");
        ACache.get(context).remove("desc");
        ACache.get(context).remove("gender");
        ACache.get(context).remove("token");
        ACache.get(context).remove("thumb");
        ACache.get(context).remove("sdk_app_id");
        ACache.get(context).remove("adk_account_type");
    }


}

