package com.qiandu.live.service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import com.qiandu.live.activity.LivePublisherActivity;
import com.qiandu.live.utils.Constants;
import com.qiandu.live.utils.LogUtil;

/**
 * @description: 前台进程
 *                  添加广播消息监听下线通知，若被挤下线则强制拉起陆平页面显示错误信息
 *
 * @author: Andruby
 * @time: 2016/12/18 14:04
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class ScreenRecordService extends Service {

    private static final String TAG = ScreenRecordService.class.getSimpleName();

    //被踢下线广播监听
    private LocalBroadcastManager mLocalBroadcatManager;
    private BroadcastReceiver mExitBroadcastReceiver;

    public ScreenRecordService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Notification.Builder builder = new Notification.Builder(this);
        Intent intent = new Intent(this, LivePublisherActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        final PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
        builder.setContentIntent(contentIntent);
        //builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setTicker("Foreground Service Start");
        builder.setContentTitle("正在进行录制");
        Notification notification = builder.build();
        //把该service创建为前台service
        startForeground(1, notification);

        mLocalBroadcatManager = LocalBroadcastManager.getInstance(this);
        mExitBroadcastReceiver = new ExitBroadcastRecevier();
        mLocalBroadcatManager.registerReceiver(mExitBroadcastReceiver, new IntentFilter(Constants.EXIT_APP));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocalBroadcatManager.unregisterReceiver(mExitBroadcastReceiver);
    }

    public class ExitBroadcastRecevier extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constants.EXIT_APP)) {
                LogUtil.e(TAG, "service broadcastReceiver receive exit app msg");
                //唤醒activity提示推流结束
//                Intent restartIntent = new Intent(getApplicationContext(), ScreenRecordActivity.class);
//                restartIntent.setAction(Constants.EXIT_APP);
//                restartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                getApplicationContext().startActivity(restartIntent);
            }
        }
    }
}
