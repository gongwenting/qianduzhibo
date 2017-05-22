package com.qiandu.live.presenter.ipresenter;

import android.content.Context;
import android.os.Bundle;

import com.qiandu.live.base.BasePresenter;
import com.qiandu.live.base.BaseView;
import com.qiandu.live.model.LeaveMsgInfo;
import com.qiandu.live.model.SimpleUserInfo;
import com.tencent.rtmp.TXLivePlayConfig;
import com.tencent.rtmp.ui.TXCloudVideoView;

import java.util.ArrayList;
import java.util.List;


/**
 * @description: 播放管理
 * @author: Andruby
 * @time: 2016/12/15 11:54
 */
public abstract class ILivePlayerPresenter implements BasePresenter {

    protected ILivePlayerView mBaseView;

    public ILivePlayerPresenter(ILivePlayerView baseView) {
        mBaseView = baseView;
    }

    /**
     * 初始化播放器
     *
     * @param cloudVideoView
     * @param livePlayConfig
     */
    public abstract void initPlayerView(TXCloudVideoView cloudVideoView, TXLivePlayConfig livePlayConfig);

    public abstract void playerPause();

    public abstract void playerResume();

    /**
     * 开始播放
     *
     * @param playUrl
     * @param playType
     */
    public abstract void startPlay(String playUrl,
                                   int playType);

    public abstract void stopPlay(boolean isClearLastImg);

//    /**
//     * 点赞接口
//     *
//     * @param userId
//     * @param liveId
//     * @param hostId
//     * @param groupId
//     */
//    public abstract void doLike(String userId, String liveId, String hostId, String groupId);
//
//    /**
//     * 进入直播群
//     * @param userId
//     * @param liveId
//     * @param hostId
//     * @param groupId
//     */
//    public abstract void enterGroup(String userId, String liveId, String hostId, String groupId);

    /**
     * 退出直播群
     * @param userId
//     * @param liveId
//     * @param hostId
//     * @param groupId
     */
    public abstract void quitGroup(String userId);

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
    public abstract List<LeaveMsgInfo> getLeaveMsg(String userId);

    /**
     * 发送举报请求
     * @param reporId
     * @param reportedId
     * @param context
     */
    public abstract void reportUser(String reporId, String reportedId, Context context);

    public abstract void guanzhu(String userId,String partnerId,String act);
    public abstract void qinmidu(String liveId);
    public abstract void quxiaoguanzhu(String userId,String partnerId,String act);
    public interface ILivePlayerView extends BaseView {
        void onPlayEvent(int i, Bundle bundle);

        void onNetStatus(Bundle bundle);

        void doLikeResult(int result);

        /**
         * 获取观众列表结果
         *
         * @param retCode
         * @param totalCount
         * @param membersList
         */
        void onGroupMembersResult(int retCode, int totalCount, ArrayList<SimpleUserInfo> membersList);

        void onQinmidu(String kkkk);


        void onNeber(int ooo);

        void onHand(ArrayList<SimpleUserInfo> result);

    }
}
