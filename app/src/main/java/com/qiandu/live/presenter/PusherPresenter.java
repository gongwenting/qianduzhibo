package com.qiandu.live.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.qiandu.live.R;
import com.qiandu.live.adapter.UserAvatarListAdapter;
import com.qiandu.live.http.AsyncHttp;
import com.qiandu.live.http.request.BannedWordRequest;
import com.qiandu.live.http.request.CreateLiveRequest;
import com.qiandu.live.http.request.GetLeaveMsgRequest;
import com.qiandu.live.http.request.GroupMemberReuest;
import com.qiandu.live.http.request.LeaveMsgRequest;
import com.qiandu.live.http.request.QinMiduReuqest;
import com.qiandu.live.http.request.ReportUserRequest;
import com.qiandu.live.http.request.RequestComm;
import com.qiandu.live.http.request.StopLiveRequest;
import com.qiandu.live.http.response.Ainformation;
import com.qiandu.live.http.response.CreateLiveResp;
import com.qiandu.live.http.response.ResList;
import com.qiandu.live.http.response.Response;
import com.qiandu.live.model.LeaveMsgInfo;
import com.qiandu.live.model.LiveInfo;
import com.qiandu.live.model.SimpleUserInfo;
import com.qiandu.live.presenter.ipresenter.IPusherPresenter;
import com.qiandu.live.ui.customviews.BeautyDialogFragment;
import com.qiandu.live.ui.customviews.FilterDialogFragment;
import com.qiandu.live.utils.AsimpleCache.ACache;
import com.qiandu.live.utils.ListCacheUtils;
import com.qiandu.live.utils.LogUtil;
import com.qiandu.live.utils.OtherUtils;
import com.qiandu.live.utils.ToastUtils;
import com.qiandu.live.utils.UIUtils;
import com.tencent.rtmp.ITXLivePushListener;
import com.tencent.rtmp.TXLivePushConfig;
import com.tencent.rtmp.TXLivePusher;
import com.tencent.rtmp.ui.TXCloudVideoView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhao on 2017/3/2.
 */

public class PusherPresenter extends IPusherPresenter implements ITXLivePushListener, BeautyDialogFragment.SeekBarCallback, FilterDialogFragment.FilterCallback {

    public static final int LIVE_STATUS_ONLINE = 1;
    public static final int LIVE_STATUS_OFFLINE = 0;

    private final static String TAG = PusherPresenter.class.getSimpleName();
    private IPusherView mPusherView;
    private TXLivePusher mTXLivePusher;
    private TXCloudVideoView mTXCloudVideoView;
    private String mPushUrl;

    private PopupWindow mSettingPopup;
    private int mLocX;
    private int mLocY;

    private boolean mFlashOn = false;
    private BeautyDialogFragment mBeautyDialogFragment;
    private FilterDialogFragment mFilterDialogFragment;

    private int mBeautyLevel;
    private int mWhiteLevel;
    private boolean isBeauty;
    //观众列表
//    private RecyclerView mUserAvatarList;
//    private UserAvatarListAdapter mAvatarListAdapter;
    private ArrayList<SimpleUserInfo> mLiveInfoList = new ArrayList<>();
    private List<LeaveMsgInfo> mLeaveInfoMsgList = new ArrayList<>();

    public PusherPresenter(IPusherView baseView) {
        super(baseView);
        mPusherView = baseView;
        mBeautyDialogFragment = new BeautyDialogFragment();
        mBeautyDialogFragment.setSeekBarListener(this);


        mFilterDialogFragment = new FilterDialogFragment();
        mFilterDialogFragment.setFilterCallback(this);
    }

    @Override
    public void getPusherUrl(String userId, String groupId, String title, String coverPic,String position) {
        final CreateLiveRequest req = new CreateLiveRequest( userId, groupId, title, coverPic,position);
        AsyncHttp.instance().post(req, new AsyncHttp.IHttpListener() {
            @Override
            public void onStart(int requestId) {

            }

            @Override
            public void onSuccess(int requestId, Response response) {
                if (response.code == RequestComm.SUCCESS) {
                    CreateLiveResp resp = (CreateLiveResp) response.data;
                    if (resp != null) {
                        if (!TextUtils.isEmpty(resp.getPushUrl())) {
                            mPusherView.onGetPushUrl(resp.getPushUrl(), 0);
                        } else {
                            mPusherView.onGetPushUrl(null, 1);
                        }
                    } else {
                        mPusherView.onGetPushUrl(null, 1);
                    }
                } else {
                    mPusherView.showMsg(response.msg);
                    mPusherView.finish();
                }
            }

            @Override
            public void onFailure(int requestId, int httpStatus, Throwable error) {

            }
        });
    }

    @Override
    public void startPusher(TXCloudVideoView videoView, TXLivePushConfig pusherConfig, String pushUrl) {
        if (mTXLivePusher == null) {
            mTXLivePusher = new TXLivePusher(mPusherView.getContext());
            mTXLivePusher.setConfig(pusherConfig);
        }
        mTXCloudVideoView = videoView;
        mTXCloudVideoView.setVisibility(View.VISIBLE);
        mTXLivePusher.startCameraPreview(mTXCloudVideoView);
        mTXLivePusher.setPushListener(this);
        mTXLivePusher.startPusher(pushUrl);


    }

    @Override
    public void setConfig(TXLivePushConfig pusherConfig) {
        if (mTXLivePusher == null) {
            mTXLivePusher.setConfig(pusherConfig);
        }
    }

    @Override
    public void stopPusher() {
        if (mTXLivePusher != null) {
            mTXLivePusher.stopCameraPreview(false);
            mTXLivePusher.setPushListener(null);
            mTXLivePusher.stopPusher();
        }
    }

    @Override
    public void resumePusher() {
        if (mTXLivePusher != null) {
            mTXLivePusher.resumePusher();
            mTXLivePusher.startCameraPreview(mTXCloudVideoView);
            mTXLivePusher.resumeBGM();
        }
    }

    @Override
    public void pausePusher() {
        if (mTXLivePusher != null) {
            mTXLivePusher.pauseBGM();
        }
    }

    @Override
    public void showSettingPopupWindow(final View targetView, int[] location) {
        targetView.setBackgroundResource(R.drawable.icon_setting_down);
        if (mSettingPopup == null) {
            View contentView = LayoutInflater.from(mPusherView.getContext()).inflate(R.layout.live_host_setting, null);
            contentView.findViewById(R.id.ll_live_setting_flash).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mTXLivePusher.turnOnFlashLight(!mFlashOn);
                    mFlashOn = !mFlashOn;
                    mSettingPopup.dismiss();
                }
            });
            contentView.findViewById(R.id.ll_live_setting_changecamera).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSettingPopup.dismiss();
                    mTXLivePusher.switchCamera();
                }
            });

            contentView.findViewById(R.id.ll_live_setting_beauty).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSettingPopup.dismiss();
                    //beautyLevel:0-9,默认为0，不开启美颜
                    //whiteLevel 0-3,默认为0，不开启美白

                    if (isBeauty) {
                        mTXLivePusher.setBeautyFilter(0, 0);
                        isBeauty = !isBeauty;
                    } else {
                        if (!mTXLivePusher.setBeautyFilter(7, 3)) {
                            ToastUtils.makeText(mPusherView.getContext(), R.string.beauty_disenable, Toast.LENGTH_SHORT);
                        } else {
                            isBeauty = !isBeauty;
                        }
                    }
//                    mBeautyDialogFragment.show(mPusherView.getFragmentMgr(), "");

                }
            });

            contentView.findViewById(R.id.ll_live_setting_filter).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSettingPopup.dismiss();
                    mFilterDialogFragment.show(mPusherView.getFragmentMgr(), "");
                }
            });


            mSettingPopup = new PopupWindow(contentView, UIUtils.formatDipToPx(mPusherView.getContext(),
                    100), UIUtils.formatDipToPx(mPusherView.getContext(), 170));
            mSettingPopup.setFocusable(true);
            mSettingPopup.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mLocX = location[0] - (mSettingPopup.getWidth() - targetView
                    .getWidth()) / 2;
            mLocY = location[1] - (mSettingPopup.getHeight());
            mSettingPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    targetView.setBackgroundResource(R.drawable.icon_setting_up);
                }
            });
        }
        mSettingPopup.showAtLocation(targetView, Gravity.NO_GRAVITY, mLocX, mLocY);

    }

    /**
     * 更改直播状态
     *
     * @param userId 主播ID
     * @param status 状态 LIVE_STATUS_OFFLINE = 0; LIVE_STATUS_ONLINE = 1;
     */
    @Override
    public void changeLiveStatus(String userId, int status) {
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
                   mPusherView.onQinmidu(kkkk);
                    mPusherView.oNneber(ooo);
                }
            }

            @Override
            public void onFailure(int requestId, int httpStatus, Throwable error) {

            }
        });
    }

    @Override
    public void stopLive(String userId, String groupId) {
        StopLiveRequest stopLiveRequest = new StopLiveRequest( userId);
        AsyncHttp.instance().post(stopLiveRequest, new AsyncHttp.IHttpListener() {
            @Override
            public void onStart(int requestId) {

            }

            @Override
            public void onSuccess(int requestId, Response response) {

            }

            @Override
            public void onFailure(int requestId, int httpStatus, Throwable error) {

            }
        });
    }

    private List<SimpleUserInfo> simList;

    @Override
    public List<SimpleUserInfo> groupMember(String groupId) {
        simList = new ArrayList<>();

        GroupMemberReuest ber=new GroupMemberReuest(groupId);
        AsyncHttp.instance().post(ber, new AsyncHttp.IHttpListener() {
            @Override
            public void onStart(int requestId) {

            }

            @Override
            public void onSuccess(int requestId, Response response) {
                if (response.code == RequestComm.SUCCESS) {
                    ResList<SimpleUserInfo> resList = (ResList<SimpleUserInfo>) response.data;
                    ArrayList<SimpleUserInfo> result = (ArrayList<SimpleUserInfo>) resList.datas;

//                    simList = result;
                    ListCacheUtils.setResult(result);
                    LogUtil.i(TAG, "username=" + result.get(0).username);


                    mLiveInfoList.addAll(result);
                    mPusherView.onHand(result);
                    LogUtil.d("result","result"+mLiveInfoList.size());

                }
            }

            @Override
            public void onFailure(int requestId, int httpStatus, Throwable error) {

            }
        });
        return mLiveInfoList;
    }

    private String isLeaveMsgSucess;
    private String isReportedSuccess;
    private String isBannedWords;

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

                isLeaveMsgSucess = response.msg;
                Toast.makeText(context, isLeaveMsgSucess, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int requestId, int httpStatus, Throwable error) {

            }
        });
//        return isLeaveMsgSucessess;
    }

    @Override
    public List<LeaveMsgInfo> leaveMsg(String userId) {
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

                LogUtil.d("GLM","result"+mLeaveInfoMsgList.size());
            }

            @Override
            public void onFailure(int requestId, int httpStatus, Throwable error) {

            }
        });
        return mLeaveInfoMsgList;
    }

    @Override
    public void bannesWord(final String userId, final String liveId, final Context context) {
        BannedWordRequest request = new BannedWordRequest(userId, liveId);
        AsyncHttp.instance().post(request, new AsyncHttp.IHttpListener() {
            @Override
            public void onStart(int requestId) {

            }

            @Override
            public void onSuccess(int requestId, Response response) {
                isBannedWords = response.msg;
                LogUtil.i("Banned", "isSucess="+userId);
                LogUtil.i("Banned", "isSucess="+liveId);

                LogUtil.i("Banned", "isSucess="+response.code);
                LogUtil.i("Banned", "isSucess="+isBannedWords);
                Toast.makeText(context, isBannedWords, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int requestId, int httpStatus, Throwable error) {

            }
        });

//        return isBannedWords;
    }

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

//        return isReportedSuccess;
    }

    @Override
    public void start() {

    }

    @Override
    public void finish() {

    }

    @Override
    public void onPushEvent(int event, Bundle bundle) {
        //推流相关的事件
        mPusherView.onPushEvent(event, bundle);
    }

    @Override
    public void onNetStatus(Bundle bundle) {
        //网络变化后的回调
        mPusherView.onNetStatus(bundle);
    }

    @Override
    public void onProgressChanged(int progress, int state) {
        switch (state) {
            case BeautyDialogFragment.STATE_BEAUTY:
                mBeautyLevel = OtherUtils.filtNumber(9, 100, progress);
                break;
            case BeautyDialogFragment.STATE_WHITE:
                mWhiteLevel = OtherUtils.filtNumber(3, 100, progress);
                break;
        }
        mTXLivePusher.setBeautyFilter(mBeautyLevel, mWhiteLevel);
    }

    @Override
    public void setFilter(Bitmap filterBitmap) {
        mTXLivePusher.setFilter(filterBitmap);
    }
}
