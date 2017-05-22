package com.qiandu.live.presenter.ipresenter;

import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.qiandu.live.base.BasePresenter;
import com.qiandu.live.base.BaseView;
import com.qiandu.live.model.LeaveMsgInfo;
import com.qiandu.live.model.SimpleUserInfo;
import com.tencent.rtmp.TXLivePushConfig;
import com.tencent.rtmp.ui.TXCloudVideoView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * @description: 推流
 * @author: Andruby
 * @time: 2016/12/15 11:54
 */
public abstract class IPusherPresenter implements BasePresenter {
    protected BaseView mBaseView;

    public IPusherPresenter(BaseView baseView) {
        mBaseView = baseView;
    }

    public abstract void getPusherUrl(final String userId, final String groupId, final String title,
                                      final String coverPic,final String position);

    public abstract void startPusher(TXCloudVideoView videoView, TXLivePushConfig pusherConfig, String pushUrl);

    public abstract void setConfig(TXLivePushConfig pusherConfig);

    public abstract void stopPusher();

    public abstract void resumePusher();

    public abstract void pausePusher();


    public abstract void showSettingPopupWindow(View targetView, int[] locations);

    /**
     * 直播状态改变1，在线，直播中，0不在线，直播结束
     * @param userId
     * @param status
     */
    public abstract void changeLiveStatus(String userId, int status);
    public abstract void qinmidu(String liveId);
    /**
     * 结束直播
     * @param userId
     * @param groupId
     */
    public abstract void stopLive(String userId, String groupId);

    public interface IPusherView extends BaseView {
        /**
         * @param pushUrl
         * @param errorCode 0表示成功 1表示失败
         */
        void onGetPushUrl(String pushUrl, int errorCode);

        void onPushEvent(int event, Bundle bundle);

        void onNetStatus(android.os.Bundle bundle);

        FragmentManager getFragmentMgr();

        void onQinmidu(String liveId);
        void finish();


        void oNneber(int ooo);

        void onHand(List<SimpleUserInfo> result);
    }
    /**
     * 当前观看直播的用户列表，限制50个人
     //     * @param userId
     //     * @param liveId
     //     * @param hostId
     //     * @param groupId
     //     * @param pageIndex
     //     * @param pageSize
     */
    public abstract List<SimpleUserInfo> groupMember(String groupId);

    /**
     * 发送留言消息
     */
    public abstract void sendLeaveMsg(String sendId, String acceId, String content, Context context);
    /**
     * 获取留言消息
     * @param userId
     * @return
     */
    public abstract List<LeaveMsgInfo> leaveMsg(String userId);

    /**
     * 发送禁言请求
     */
    public abstract void bannesWord(String userId, String liveId, Context context);

    /**
     * 发送举报请求
     * @param reporId
     * @param reportedId
     * @param context
     */
    public abstract void reportUser(String reporId, String reportedId, Context context);
}