package com.qiandu.live.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.qiandu.live.R;
import com.qiandu.live.model.GiftWithUerInfo;
import com.qiandu.live.presenter.GiftShowManager;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @description: 礼物服务
 * @author: Andruby
 * @time: 2016/12/18 14:04
 */
public class LiveGiftServices extends Service implements GiftShowManager.ShowGiftIsEmptyListener {
    private static final String TAG = LiveGiftServices.class.getSimpleName();
    //展示礼物的布局相关
    private static GiftShowManager giftManger1;
    private static GiftShowManager giftManger2;
    private static GiftShowManager giftManger3;
    private LinkedBlockingQueue<GiftWithUerInfo> giftInfoQueue;//礼物的队列

    private ArrayList<GiftShowManager> mShowMagangerList;
    private LiveGiftShowBinder mLiveGiftShowBinder = new LiveGiftShowBinder();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        //请求服务器获取礼物数据
        return mLiveGiftShowBinder;
    }

    /**
     * 制空静态变量
     */
    public void destroyAllManager() {
        for (GiftShowManager gm : mShowMagangerList) {
            gm.destory();
        }
        mShowMagangerList = null;
    }

    @Override
    public void onDestroy() {
        destroyAllManager();
        super.onDestroy();
    }


    @Override
    public GiftWithUerInfo getMoreGiftInfo() {
        if (!giftInfoQueue.isEmpty()) {
            return giftInfoQueue.poll();
        }
        return null;
    }

    public class LiveGiftShowBinder extends Binder {
        public void initGiftShowManager(Context context, FrameLayout giftRootView) {

            giftInfoQueue = new LinkedBlockingQueue<>();

            giftManger1 = new GiftShowManager(context, (LinearLayout) giftRootView.findViewById(R.id.live_gift_con1));
            giftManger2 = new GiftShowManager(context, (LinearLayout) giftRootView.findViewById(R.id.live_gift_con2));
            giftManger3 = new GiftShowManager(context, (LinearLayout) giftRootView.findViewById(R.id.live_gift_con3));

            giftManger1.setShowGiftManagerIsEmptyListener(LiveGiftServices.this);
            giftManger2.setShowGiftManagerIsEmptyListener(LiveGiftServices.this);
            giftManger3.setShowGiftManagerIsEmptyListener(LiveGiftServices.this)
            ;
            giftManger1.startGetGift();
            giftManger2.startGetGift();
            giftManger3.startGetGift();

            mShowMagangerList = new ArrayList<>();
            mShowMagangerList.add(giftManger1);
            mShowMagangerList.add(giftManger2);
            mShowMagangerList.add(giftManger3);
        }

        public void dispatchGift(GiftWithUerInfo giftWithUerInfo) {
            if (giftInfoQueue != null) {
                giftInfoQueue.add(giftWithUerInfo);
            }
        }
    }


}
