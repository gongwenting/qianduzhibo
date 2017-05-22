package com.qiandu.live.logic;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.tencent.TIMGroupSettings;
import com.tencent.TIMManager;
import com.tencent.TIMUserStatusListener;
import com.qiandu.live.utils.Constants;

/**
 * @description: IMSDK init操作
 *
 * @author: Andruby
 * @time: 2016/11/4 14:12
 */
public class IMInitMgr {

    //标志位确定SDK是否初始化，避免客户SDK未初始化的情况，实现可重入的init操作
    private static boolean isSDKInit = false;

    /**
     * IMSDK init操作
     * @param context application context
     */
    public static void init(final Context context) {

        if (isSDKInit)
            return;

        //禁止服务器自动代替上报已读
        TIMManager.getInstance().disableAutoReport();
        //初始化imsdk
        TIMManager.getInstance().init(context);
        //初始化群设置
        TIMManager.getInstance().initGroupSettings(new TIMGroupSettings());
        //注册sig失效监听回调
        TIMManager.getInstance().setUserStatusListener(new TIMUserStatusListener() {
            @Override
            public void onForceOffline() {
                LocalBroadcastManager.getInstance(context.getApplicationContext()).sendBroadcast(new Intent(Constants.EXIT_APP));
            }

            @Override
            public void onUserSigExpired() {
                IMLogin.getInstance().reLogin();
            }
        });

        //初始化登录模块
        IMLogin.getInstance().init(context);
        //禁用消息存储
        //TIMManager.getInstance().disableStorage();

        isSDKInit = true;
    }
}
