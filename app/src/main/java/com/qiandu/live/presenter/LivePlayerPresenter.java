package com.qiandu.live.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.qiandu.live.http.AsyncHttp;
import com.qiandu.live.http.request.GetLeaveMsgRequest;
import com.qiandu.live.http.request.GroupMemberReuest;
import com.qiandu.live.http.request.GuanzhuListendRequest;
import com.qiandu.live.http.request.GuanzhuListendResp;
import com.qiandu.live.http.request.LeaveMsgRequest;
import com.qiandu.live.http.request.QinMiduReuqest;
import com.qiandu.live.http.request.ReportUserRequest;
import com.qiandu.live.http.request.RequestComm;
import com.qiandu.live.http.request.StopLiveRequest;
import com.qiandu.live.http.response.Ainformation;
import com.qiandu.live.http.response.ResList;
import com.qiandu.live.http.response.Response;
import com.qiandu.live.model.LeaveMsgInfo;
import com.qiandu.live.model.SimpleUserInfo;
import com.qiandu.live.presenter.ipresenter.ILivePlayerPresenter;
import com.qiandu.live.utils.AsimpleCache.ACache;
import com.qiandu.live.utils.ListCacheUtils;
import com.qiandu.live.utils.LogUtil;
import com.tencent.rtmp.ITXLivePlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayConfig;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhao on 2017/3/11.
 */

public class LivePlayerPresenter extends ILivePlayerPresenter implements ITXLivePlayListener {

    private final static String TAG = LivePlayerPresenter.class.getSimpleName();
    private ILivePlayerView mLivePlayerView;
    private TXCloudVideoView mCloudVideoView;
    private TXLivePlayer mLivePLayer;
        //观众列表
    private ArrayList<SimpleUserInfo> mLiveInfoList = new ArrayList<>();
    private List<LeaveMsgInfo> mLeaveInfoMsgList = new ArrayList<>();

    public LivePlayerPresenter(ILivePlayerView baseView) {
        super(baseView);
        mLivePlayerView = baseView;
    }

    @Override
    public void initPlayerView(TXCloudVideoView cloudVideoView, TXLivePlayConfig livePlayConfig) {
        mCloudVideoView = cloudVideoView;
        mLivePLayer = new TXLivePlayer(mLivePlayerView.getContext());
        mLivePLayer.enableHardwareDecode(true);
        mLivePLayer.setPlayerView(cloudVideoView);
        mLivePLayer.setPlayListener(this);
        mLivePLayer.setConfig(livePlayConfig);
    }

    public void  enableHardwareDecode(boolean decode){
        mLivePLayer.enableHardwareDecode(false);
    }

    @Override
    public void playerPause() {
        mLivePLayer.pause();
    }

    @Override
    public void playerResume() {
        mLivePLayer.resume();
    }

    @Override
    public void startPlay(String playUrl, int playType) {
        mLivePLayer.startPlay(playUrl, playType);
    }

    public void stopPlay(boolean isClearLastImg) {

        mLivePLayer.stopPlay(isClearLastImg);
        mCloudVideoView.onDestroy();
    }


    @Override
    public void quitGroup(String userId) {
        StopLiveRequest quiteRequest = new StopLiveRequest(userId);
        AsyncHttp.instance().post(quiteRequest, null);
    }

    @Override
    public List<SimpleUserInfo> groupMember(String groupId) {
        GroupMemberReuest quiteRequest = new GroupMemberReuest(groupId);
        AsyncHttp.instance().post(quiteRequest, new AsyncHttp.IHttpListener() {
            @Override
            public void onStart(int requestId) {

            }

            @Override
            public void onSuccess(int requestId, Response response) {

                if (response != null && response.code == RequestComm.SUCCESS) {
                    ResList<SimpleUserInfo> resList = (ResList<SimpleUserInfo>) response.data;
                    ArrayList<SimpleUserInfo> result = (ArrayList<SimpleUserInfo>) resList.datas;
//                    if (resList != null && resList.datas != null) {
//                        mLivePlayerView.onGroupMembersResult(0, 0, (ArrayList<SimpleUserInfo>) resList.datas);
//
                    ListCacheUtils.setResult(result);
                    LogUtil.i(TAG, "username=" + result.get(0).username);
                    mLiveInfoList.addAll(result);
                    mLivePlayerView.onHand(result);
//                    } else {
//                        mLivePlayerView.onGroupMembersResult(0, 0, null);
//                    }
//                } else {
//                    mLivePlayerView.onGroupMembersResult(1, 0, null);
                }
            }

            @Override
            public void onFailure(int requestId, int httpStatus, Throwable error) {
                mLivePlayerView.onGroupMembersResult(1, 0, null);
            }
        });
        return mLiveInfoList;
    }

    private String isSucess;

    @Override
    public void sendLeaveMsg(String sendId, String acceId, String content, final Context context) {
        LeaveMsgRequest request = new LeaveMsgRequest("send", sendId, acceId, content);
        LogUtil.i("onSuccess", "sendId=" + sendId);
        LogUtil.i("onSuccess", "gainId=" + acceId);
        AsyncHttp.instance().post(request, new AsyncHttp.IHttpListener() {
            @Override
            public void onStart(int requestId) {

            }

            @Override
            public void onSuccess(int requestId, Response response) {
                LogUtil.i("SLM", "sendLeaveMsg:code=" + response.code+"");

                isSucess = response.msg;
                Toast.makeText(context, isSucess, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int requestId, int httpStatus, Throwable error) {

            }
        });
//        return isSucess;
    }

    @Override
    public List<LeaveMsgInfo> getLeaveMsg(String userId) {
        GetLeaveMsgRequest request = new GetLeaveMsgRequest(userId);
        AsyncHttp.instance().post(request, new AsyncHttp.IHttpListener() {
            @Override
            public void onStart(int requestId) {

            }

            @Override
            public void onSuccess(int requestId, Response response) {
                ResList<LeaveMsgInfo> resList = (ResList<LeaveMsgInfo>) response.data;
                ArrayList<LeaveMsgInfo> result = (ArrayList<LeaveMsgInfo>) resList.datas;
                LogUtil.i("GLM", "leaveMsg: code=" + response.code);

                mLeaveInfoMsgList.addAll(result);

                ListCacheUtils.setLeaveMsgInfoList(result);

                LogUtil.d("GLM","result"+mLeaveInfoMsgList.size());
            }

            @Override
            public void onFailure(int requestId, int httpStatus, Throwable error) {

            }
        });
        return mLeaveInfoMsgList;
    }

    private String isReportedSuccess;

    @Override
    public void reportUser(String reporId, String reportedId, final Context context) {
        ReportUserRequest reportUserRequest = new ReportUserRequest(reporId, reportedId);
        AsyncHttp.instance().post(reportUserRequest, new AsyncHttp.IHttpListener() {
            @Override
            public void onStart(int requestId) {

            }

            @Override
            public void onSuccess(int requestId, Response response) {
                isReportedSuccess = response.msg;
                Toast.makeText(context, isReportedSuccess, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int requestId, int httpStatus, Throwable error) {

            }
        });
    }

    @Override
    public void guanzhu(String userId, String partnerId, String act) {
        final GuanzhuListendRequest guanzhuListendRequest=new GuanzhuListendRequest(userId,partnerId,act);
        LogUtil.d("GUANZHU","guanzhuListendResp+++++++++++++++"+userId+"++++++++++++"+partnerId+"++++++++++++"+"++++++++++++"+act);
        //                mLivePlayerPresenter.guanzhu(ACache.get(this).getAsString("user_id"),);
        AsyncHttp.instance().post(guanzhuListendRequest, new AsyncHttp.IHttpListener() {
            @Override
            public void onStart(int requestId) {

            }

            @Override
            public void onSuccess(int requestId, Response response) {
                if (response.code == RequestComm.SUCCESS) {

                    GuanzhuListendResp guanzhuListendResp= new GuanzhuListendResp();
                    if (guanzhuListendResp.getState()==1){
                        LogUtil.d("GUANZHU","guanzhuListendResp+++++++++++++++"+guanzhuListendResp);
                    }else {
                        LogUtil.d("GUANZHU","guanzhuListendResp+++++++++++++++"+guanzhuListendResp);
                    }
                }
            }

            @Override
            public void onFailure(int requestId, int httpStatus, Throwable error) {

            }
        });
    }

    @Override
    public void qinmidu(String liveId) {
        QinMiduReuqest qin=new QinMiduReuqest(liveId);
        AsyncHttp.instance().post(qin, new AsyncHttp.IHttpListener() {
            @Override
            public void onStart(int requestId) {

            }

            @Override
            public void onSuccess(int requestId, Response response) {
                if (response.code== RequestComm.SUCCESS){
                    Ainformation ainformation=(Ainformation)response.data;
                    int ooo=ainformation.getCounts();
                    String kkkk=ainformation.getIntimacy();
                    mLivePlayerView.onQinmidu(kkkk);
                    mLivePlayerView.onNeber(ooo);

//                    ACache.get(mLivePlayerView.getContext()).put("intimacy",kkkk);
//                    LogUtil.d("ZHUBOXINXIN",""+ooo+"+++++++++++++"+kkkk);
                }
            }

            @Override
            public void onFailure(int requestId, int httpStatus, Throwable error) {

            }
        });
    }


    @Override
    public void quxiaoguanzhu(String userId, String partnerId, String act) {
        final GuanzhuListendRequest guanzhuListendRequest=new GuanzhuListendRequest(userId,partnerId,act);
//                mLivePlayerPresenter.guanzhu(ACache.get(this).getAsString("user_id"),);
        AsyncHttp.instance().post(guanzhuListendRequest, new AsyncHttp.IHttpListener() {
            @Override
            public void onStart(int requestId) {

            }

            @Override
            public void onSuccess(int requestId, Response response) {
                if (response.code == RequestComm.SUCCESS) {

                    GuanzhuListendResp guanzhuListendResp= new GuanzhuListendResp();
                    if (guanzhuListendResp.getState()==1){
                        LogUtil.d("GUANZHU","guanzhuListendResp+++++++++++++++"+guanzhuListendResp);
                    }else {
                        LogUtil.d("GUANZHU","guanzhuListendResp+++++++++++++++"+guanzhuListendResp);
                    }
                }
            }

            @Override
            public void onFailure(int requestId, int httpStatus, Throwable error) {

            }
        });
    }


    @Override
    public void start() {

    }

    @Override
    public void finish() {

    }

    @Override
    public void onPlayEvent(int event, Bundle bundle) {
        Log.i(TAG, "onPlayEvent: event = " + event);
        mLivePlayerView.onPlayEvent(event,bundle);
    }

    @Override
    public void onNetStatus(Bundle bundle) {
        Log.i(TAG, "onNetStatus: cpu = " + bundle.getString(TXLiveConstants.NET_STATUS_CPU_USAGE));
        mLivePlayerView.onNetStatus(bundle);
    }
}
