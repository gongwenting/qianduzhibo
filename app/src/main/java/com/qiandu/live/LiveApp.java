package com.qiandu.live;

import android.app.Application;
import android.util.Log;

import com.qiandu.live.utils.LiveLogUitil;
import com.qiandu.live.logic.IMInitMgr;
import com.tencent.rtmp.TXLiveBase;

/**
 * @description: 小直播应用类，用于全局的操作，如
 *                  sdk初始化,全局提示框
 * @author: Andruby
 * @time: 2016/12/17 10:23
 */
public class LiveApp extends Application {

//    private RefWatcher mRefWatcher;

    private static final String BUGLY_APPID = "1400012894";

    private static LiveApp instance;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        initSDK();

//        mRefWatcher =
//        LeakCanary.install(this);
    }

    public static LiveApp getApplication() {
        return instance;
    }

//    public static RefWatcher getRefWatcher(Context context) {
//        LiveApp application = (LiveApp) context.getApplicationContext();
//        return application.mRefWatcher;
//    }

    /**
     * 初始化SDK，包括Bugly，IMSDK，RTMPSDK等
     */
    public void initSDK() {

        IMInitMgr.init(getApplicationContext());

        //设置rtmpsdk log回调，将log保存到文件
        TXLiveBase.getInstance().listener = new LiveLogUitil(getApplicationContext());

        //初始化httpengine
//        HttpEngine.getInstance().initContext(getApplicationContext());

        Log.w("LiveLogUitil","app init sdk");
    }

}
