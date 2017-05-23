package com.qiandu.live;

import android.app.Application;
import android.util.Log;

import com.qiandu.live.utils.LiveLogUitil;
import com.qiandu.live.logic.IMInitMgr;
import com.tencent.rtmp.TXLiveBase;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.common.QueuedWork;

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
        //开启debug模式，方便定位错误，具体错误检查方式可以查看http://dev.umeng.com/social/android/quick-integration的报错必看，正式发布，请关闭该模式
        Config.DEBUG = true;
        /**
         * 对应平台没有安装的时候跳转转到应用商店下载
         * 在初始化sdk的时候添加如下代码即可：
         * Config.isJumptoAppStore = true
         * 其中qq 微信会跳转到下载界面进行下载，其他应用会跳到应用商店进行下载
         */
        Config.isJumptoAppStore = true;
        QueuedWork.isUseThreadPool = false;
        UMShareAPI.get(this);
//        mRefWatcher =
//        LeakCanary.install(this);
    }
    //各个平台的配置，建议放在全局Application或者程序入口
    {
        PlatformConfig.setWeixin("wx542e1371d8daf5a3", "d233dd6b61a462b26bd0c482d70b0b12");
        //豆瓣RENREN平台目前只能在服务器端配置
        PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad","http://sns.whalecloud.com");
        PlatformConfig.setYixin("yxc0614e80c9304c11b0391514d09f13bf");
        PlatformConfig.setQQZone("1106125626", "Lui59hmN2uKLFiya");
        PlatformConfig.setTwitter("3aIN7fuF685MuZ7jtXkQxalyi", "MK6FEYG63eWcpDFgRYw4w9puJhzDl0tyuqWjZ3M7XJuuG7mMbO");
        PlatformConfig.setAlipay("2015111700822536");
        PlatformConfig.setLaiwang("laiwangd497e70d4", "d497e70d4c3e4efeab1381476bac4c5e");
        PlatformConfig.setPinterest("1439206");
        PlatformConfig.setKakao("e4f60e065048eb031e235c806b31c70f");
        PlatformConfig.setDing("dingoalmlnohc0wggfedpk");
        PlatformConfig.setVKontakte("5764965","5My6SNliAaLxEm3Lyd9J");
        PlatformConfig.setDropbox("oz8v5apet3arcdy","h7p2pjbzkkxt02a");

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
