package com.qiandu.live.activity;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qiandu.live.R;
import com.qiandu.live.adapter.ChatMsgListAdapter;
import com.qiandu.live.adapter.LeaveMessageAdapter;
import com.qiandu.live.adapter.UserAvatarListAdapter;
import com.qiandu.live.callback.GenericsCallback;
import com.qiandu.live.callback.JsonGenericsSerializator;
import com.qiandu.live.http.AsyncHttp;
import com.qiandu.live.http.request.FetchGroupMemberListReuest;
import com.qiandu.live.http.request.JsonBean;
import com.qiandu.live.http.request.RequestComm;
import com.qiandu.live.http.response.Response;
import com.qiandu.live.logic.IMLogin;
import com.qiandu.live.logic.UserInfoMgr;
import com.qiandu.live.model.ChatEntity;
import com.qiandu.live.model.GiftWithUerInfo;
import com.qiandu.live.model.LeaveMsgInfo;
import com.qiandu.live.model.LiveInfo;
import com.qiandu.live.model.SimpleUserInfo;
import com.qiandu.live.model.UserInfoCache;
import com.qiandu.live.presenter.IMChatPresenter;
import com.qiandu.live.presenter.PusherPresenter;
import com.qiandu.live.presenter.SwipeAnimationController;
import com.qiandu.live.presenter.ipresenter.IIMChatPresenter;
import com.qiandu.live.presenter.ipresenter.IPusherPresenter;
import com.qiandu.live.service.LiveGiftServices;
import com.qiandu.live.ui.customviews.EndDetailFragment;
import com.qiandu.live.ui.customviews.HeartLayout;
import com.qiandu.live.ui.customviews.InputLeaveMsgDialog;
import com.qiandu.live.ui.customviews.InputTextMsgDialog;
import com.qiandu.live.utils.AsimpleCache.ACache;
import com.qiandu.live.utils.Constants;
import com.qiandu.live.utils.LogUtil;
import com.qiandu.live.utils.OtherUtils;
import com.qiandu.live.utils.ToastUtils;
import com.tencent.TIMMessage;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePushConfig;
import com.tencent.rtmp.audio.TXAudioPlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;


/**
 * @Description: 主播 推流
 * @author: Andruby
 * @date: 2016年7月8日 下午4:46:44
 */
public class LivePublisherActivity extends IMBaseActivity implements View.OnClickListener,
        IPusherPresenter.IPusherView, IIMChatPresenter.IIMChatView, InputTextMsgDialog.OnTextSendListener, InputLeaveMsgDialog.OnTextSendListener {
    private static final String TAG = LivePublisherActivity.class.getSimpleName();
    private static String position;

    private TXCloudVideoView mTXCloudVideoView;
    private TXLivePushConfig mTXPushConfig = new TXLivePushConfig();
    private Handler mHandler = new Handler();

    private boolean mFlashOn = false;
    private boolean mPasuing = false;

    private String mPushUrl;
    private String mGroupId;
    private String mUserId;
    private String mTitle;
    private String liveId;
    private String mCoverPicUrl;
    private String mHeadPicUrl;
    private String mNickName;
    private String mLocation;
    private boolean mIsRecord;

    private String intimacy;
    private LinearLayout mAudioPluginLayout;
    private Button mBtnAudioEffect;
    private Button mBtnAudioClose;
    private TXAudioPlayer mAudioPlayer;
    private RelativeLayout mControllLayer;
    private SwipeAnimationController mTCSwipeAnimationController;

    private PusherPresenter mPusherPresenter;
    private int[] mSettingLocation = new int[2];
    private View btnSettingView;

    private IMChatPresenter mIMChatPresenter;

    //主播相关信息，头像、观众数
    private ImageView ivHeadIcon;
    private ImageView ivRecordBall;
    private TextView tvMemberCount;
    private int mMemberCount = 0; //实时人数
    private int mTotalCount = 0; //总观众人数
    private int mPraiseCount = 0; //点赞人数
    //播放信息：时间、红点
    private long mSecond = 0;
    private TextView tvBroadcastTime;
    private Timer mBroadcastTimer;
    private BroadcastTimerTask mBroadcastTimerTask;
    private ObjectAnimator mObjAnim;

    private InputTextMsgDialog mInputTextMsgDialog;
    private InputLeaveMsgDialog mInputLeaveMsgDialog;
    //消息列表
    private ArrayList<ChatEntity> mArrayListChatEntity = new ArrayList<>();
    private ChatMsgListAdapter mChatMsgListAdapter;
    private ListView mListViewMsg;

    private List<SimpleUserInfo> simList;

    /**
     * 点赞动画
     */
    private HeartLayout mHeartLayout;
    //亲密度
    private String zhubojine;
    //观众列表
    private RecyclerView mUserAvatarList;
    private UserAvatarListAdapter mAvatarListAdapter;
    private LiveInfo mLiveInfo;
    private RelativeLayout ribang;
    //官方提示
    private TextView zhuboxinxi;
    private boolean mOfficialMsgSended = false;
    private String onNuber;
    //直播背景
    private ImageView ivLiveBg;
    //礼物显示
    private FrameLayout mGiftRootView;
    private LiveGiftServices.LiveGiftShowBinder mLiveGiftShowBinder;
    //观看人数
    private String nuber;

    /*
   留言墙
    */
    private Button btnLeaveMessage;
    private LinearLayout llLeaveMessageDetail;
    private ImageView ivCancleShowLeaveMessage;
    private RecyclerView rvLeaveMessage;
    private LeaveMessageAdapter leaveMessageAdapter;
    private List<LeaveMsgInfo> leaveMsgList;
    private SwipeRefreshLayout srlLeaveMsg;

    private ServiceConnection mGiftConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mLiveGiftShowBinder = (LiveGiftServices.LiveGiftShowBinder) service;
            mLiveGiftShowBinder.initGiftShowManager(mContext, mGiftRootView);

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void setBeforeLayout() {
        super.setBeforeLayout();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

    }

    private void getDataFormIntent() {
        Intent intent = getIntent();
        mLiveInfo = (LiveInfo) intent.getSerializableExtra(Constants.LIVE_INFO);
        mUserId = intent.getStringExtra(Constants.USER_ID);
        liveId = intent.getStringExtra(Constants.LIVEID);
        LogUtil.i(TAG, "LiveID=" + liveId);
        mGroupId = intent.getStringExtra(Constants.GROUP_ID);
        mPushUrl = intent.getStringExtra(Constants.PUBLISH_URL);
        mTitle = intent.getStringExtra(Constants.ROOM_TITLE);
        mCoverPicUrl = intent.getStringExtra(Constants.COVER_PIC);
        mHeadPicUrl = intent.getStringExtra(Constants.USER_HEADPIC);
        mNickName = intent.getStringExtra(Constants.USER_NICK);
        mLocation = intent.getStringExtra(Constants.USER_LOC);
        mIsRecord = intent.getBooleanExtra(Constants.IS_RECORD, false);
        simList = new ArrayList<>();

    }

    @Override
    protected int getLayoutId() {
//        getlistview();
        mPusherPresenter = new PusherPresenter(this);
        mIMChatPresenter = new IMChatPresenter(this);
        return R.layout.activity_live_publisher;
    }


    @Override
    public void onReceiveExitMsg() {
        super.onReceiveExitMsg();

        LogUtil.e(TAG, "publisher broadcastReceiver receive exit app msg");
        //在被踢下线的情况下，执行退出前的处理操作：停止推流、关闭群组
        mTXCloudVideoView.onPause();
        stopPublish();
    }

    @Override
    protected void initView() {
        getDataFormIntent();
        mTXCloudVideoView = obtainView(R.id.video_view);
        btnSettingView = obtainView(R.id.btn_setting);
        mTCSwipeAnimationController = new SwipeAnimationController(this);
        mTCSwipeAnimationController.setAnimationView(mControllLayer);
        //主播信息
        tvBroadcastTime = obtainView(R.id.tv_broadcasting_time);
        tvBroadcastTime.setText(String.format(Locale.US, "%s", "00:00:00"));
        ivRecordBall = obtainView(R.id.iv_record_ball);
        ivHeadIcon = obtainView(R.id.iv_head_icon);
        OtherUtils.showPicWithUrl(this, ivHeadIcon, UserInfoCache.getHeadPic(mContext), R.drawable.default_head);
        tvMemberCount = obtainView(R.id.tv_member_counts);
        tvMemberCount.setText(nuber);


        mPusherPresenter.qinmidu(ACache.get(this).getAsString("user_id"));
        //荣誉值
        ribang = (RelativeLayout) findViewById(R.id.ribang);
        ribang.setOnClickListener(this);
        zhuboxinxi = (TextView) findViewById(R.id.text_qinmidu);
        intimacy = ACache.get(this).getAsString("intimacy");
        LogUtil.d("AAAAA", "" + intimacy);
        zhuboxinxi.setText(intimacy);
        recordAnmination();
        zhuboxinxi.setOnClickListener(this);

        mInputTextMsgDialog = new InputTextMsgDialog(this, R.style.InputDialog);
        mInputTextMsgDialog.setmOnTextSendListener(this);

        mListViewMsg = obtainView(R.id.im_msg_listview);
        mChatMsgListAdapter = new ChatMsgListAdapter(this, mListViewMsg, mArrayListChatEntity);
        mListViewMsg.setAdapter(mChatMsgListAdapter);

        mHeartLayout = obtainView(R.id.heart_layout);

        ivLiveBg = obtainView(R.id.iv_live_bg);

        btnLeaveMessage = obtainView(R.id.btn_email);
        llLeaveMessageDetail = obtainView(R.id.leave_message);
        ivCancleShowLeaveMessage = obtainView(R.id.cancle_show);
        llLeaveMessageDetail.setVisibility(View.GONE);
        rvLeaveMessage = obtainView(R.id.leave_message_info);
        srlLeaveMsg = obtainView(R.id.srl_leave_msg);

        //观众列表
        mUserAvatarList = obtainView(R.id.rv_user_avatar);
        mUserAvatarList.setVisibility(View.VISIBLE);
    }

    private void recordAnmination() {
        mObjAnim = ObjectAnimator.ofFloat(ivRecordBall, "alpha", 1.0f, 0f, 1.0f);
        mObjAnim.setDuration(1000);
        mObjAnim.setRepeatCount(-1);
        mObjAnim.start();
    }

    @Override
    protected void initData() {
        if (mTXCloudVideoView != null) {
            mTXCloudVideoView.disableLog(false);
        }
        mIMChatPresenter.createGroup();

        String headPic = ACache.get(this).getAsString("head_pic");
        if (!TextUtils.isEmpty(headPic)) {
            OtherUtils.blurBgPic(this, ivLiveBg, ACache.get(this).getAsString("head_pic"), R.drawable.bg);
        }
        initGift();
        leaveMsgList = new ArrayList<>();
        leaveMsgList = mPusherPresenter.leaveMsg(UserInfoCache.getUserId(mContext));
    }

    private void initGift() {
        mGiftRootView = obtainView(R.id.liveGiftLayout);
        Intent intent = new Intent(this, LiveGiftServices.class);
        bindService(intent, mGiftConn, BIND_AUTO_CREATE);
    }

    @Override
    protected void setListener() {
        btnLeaveMessage.setOnClickListener(this);
        ivCancleShowLeaveMessage.setOnClickListener(this);

        //点击主播头像显示主播信息
        ivHeadIcon.setOnClickListener(this);
    }

    private void startPublish() {
        mTXPushConfig.setAutoAdjustBitrate(false);
        mTXPushConfig.setVideoResolution(TXLiveConstants.VIDEO_RESOLUTION_TYPE_540_960);
        mTXPushConfig.setVideoBitrate(1000);
        mTXPushConfig.setVideoFPS(20);
        Log.i(TAG, "startPublish: MANUFACTURER " + Build.MANUFACTURER + " model:" + Build.MODEL);
        mTXPushConfig.setHardwareAcceleration(true);

//水印
//        mTXPushConfig.setWatermark(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher), 50, 50);
        //切后台推流图片
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pause_publish, options);
        mTXPushConfig.setPauseImg(bitmap);
        mPusherPresenter.startPusher(mTXCloudVideoView, mTXPushConfig, mPushUrl);


        if (mBroadcastTimer == null) {
            mBroadcastTimer = new Timer(true);
            mBroadcastTimerTask = new BroadcastTimerTask();
            mBroadcastTimer.schedule(mBroadcastTimerTask, 1000, 1000);
        }
    }

    @Override
    public void onJoinGroupResult(int code, String msg) {
        if (0 == code) {
            //获取推流地址
            LogUtil.e(TAG, "onJoin group success" + msg);
            mGroupId = msg;

            mPusherPresenter.getPusherUrl(mUserId, mGroupId, mTitle, ACache.get(this).getAsString("thumb"),position);
           LogUtil.e(TAG, "onJoin group success" +""+mUserId+"========"+mGroupId+"====="+mTitle+"====="+ACache.get(this).getAsString("thumb")+"====="+position);
//            LogUtil.i("tag", mGroupId);
//            LogUtil.i("tag", simList.size()+"");
//            rv.setadapter();
            LogUtil.d("GroupId", "mGroupId+" + mGroupId);
        } else if (Constants.NO_LOGIN_CACHE == code) {
            LogUtil.e(TAG, "onJoin group failed" + msg);
        } else {
            LogUtil.e(TAG, "onJoin group failed" + msg);
        }

        //获取观众列表
        mGroupIdd(mGroupId);
    }

    private void getLeaveMsg(String userId) {
        LogUtil.i("GLM", "getLeaveMsg:" + userId);

        leaveMsgList = mPusherPresenter.leaveMsg(userId);
        LogUtil.i("GLM", "getLeaveMsg:SIZE=" + leaveMsgList.size());
        leaveMessageAdapter = new LeaveMessageAdapter(mContext, leaveMsgList);
        rvLeaveMessage.setAdapter(leaveMessageAdapter);
        rvLeaveMessage.setLayoutManager(new LinearLayoutManager(mContext));
    }

    private void mGroupIdd(String groupId) {
        // 成员数据
        LogUtil.i(TAG, "groupId=" + groupId);
        mPusherPresenter.groupMember(groupId);

//
//        mAvatarListAdapter = new UserAvatarListAdapter(this, IMLogin.getInstance().getLastUserInfo().identifier, simList);//
//        mUserAvatarList.setAdapter(mAvatarListAdapter);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        mUserAvatarList.setLayoutManager(linearLayoutManager);
//        mAvatarListAdapter.removeItem("");
//        mAvatarListAdapter.setmUserAvatarList(simList);
//        LogUtil.i(TAG, "simList="+simList.size());
//        mAvatarListAdapter.setOnItemClickListener(new UserAvatarListAdapter.OnRecyclerViewItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                showUserInfoDialog(mContext, simList.get(position));
//            }
//        });
    }

    @Override
    public void onGroupDeleteResult() {

    }

    @Override
    public void handleTextMsg(SimpleUserInfo userInfo, String text) {
        refreshMsg(userInfo.user_id, userInfo.username + ":", text, Constants.AVIMCMD_TEXT_TYPE);
    }

    @Override
    public void handlePraiseMsg(SimpleUserInfo userInfo) {
        mPraiseCount++;
        mHeartLayout.addFavor();
    }

    @Override
    public void handlePraiseFirstMsg(SimpleUserInfo userInfo) {
        refreshMsg(userInfo.user_id, userInfo.username + ":", "点亮了桃心", Constants.AVIMCMD_PRAISE_FIRST);
        mHeartLayout.addFavor();
        mPraiseCount++;
    }

    @Override
    public void onSendMsgResult(int code, TIMMessage timMessage) {

    }

    @Override
    public void handleEnterLiveMsg(SimpleUserInfo userInfo) {
        Log.i(TAG, "handleEnterLiveMsg: ");
        //更新观众列表，观众进入显示
//        if (!mAvatarListAdapter.addItem(userInfo))
//            return;

        mMemberCount++;
        mPusherPresenter.qinmidu(ACache.get(this).getAsString("user_id"));
        mPusherPresenter.groupMember(mGroupId);
        simList.add(userInfo);
        mAvatarListAdapter.addItem(userInfo);
        mAvatarListAdapter.setmUserAvatarList(simList);
        refreshMsg(userInfo.user_id, TextUtils.isEmpty(userInfo.username) ? userInfo.user_id + ":" : userInfo.username + ":", "进入直播", Constants.AVIMCMD_ENTER_LIVE);

//        mAvatarListAdapter.addItem(userInfo);
//        mAvatarListAdapter.setmUserAvatarList(simList);
    }

    @Override
    public void handleExitLiveMsg(SimpleUserInfo userInfo) {
        Log.i(TAG, "handleExitLiveMsg: ");
        //更新观众列表，观众退出显示
//        mPusherPresenter.groupMember(mGroupId);
        if (mMemberCount > 0)
            mMemberCount--;
        mPusherPresenter.qinmidu(ACache.get(this).getAsString("user_id"));
        simList.remove(userInfo);
        mAvatarListAdapter.removeItem(userInfo.user_id);
        mAvatarListAdapter.setmUserAvatarList(simList);
//        mAvatarListAdapter.removeItem(userInfo.user_id);

        refreshMsg(userInfo.user_id, TextUtils.isEmpty(userInfo.username) ? userInfo.user_id + ":" : userInfo.username + ":", "退出直播", Constants.AVIMCMD_EXIT_LIVE);

//        mAvatarListAdapter.setmUserAvatarList(simList);
    }

    @Override
    public void handleLiveEnd(SimpleUserInfo userInfo) {

    }

    @Override
    public void handleGift(GiftWithUerInfo giftWithUerInfo) {
        if (giftWithUerInfo != null) {
            mLiveGiftShowBinder.dispatchGift(giftWithUerInfo);

            refreshMsg(giftWithUerInfo.getUserInfo().getUserId(), giftWithUerInfo.getUserInfo().getNickname(),
                    "送出" + giftWithUerInfo.getGiftInfo().getGiftCount() + giftWithUerInfo.getGiftInfo().getName(), Constants.AVIMCMD_GIFT);
            mPusherPresenter.qinmidu(ACache.get(this).getAsString("user_id"));
        }
    }

    /**
     * 关闭红点与计时动画
     */
    private void stopRecordAnimation() {
        if (mObjAnim != null) {
            mObjAnim.cancel();
            mObjAnim = null;
        }
        if (mBroadcastTimerTask != null) {
            mBroadcastTimerTask.cancel();
            mBroadcastTimer.cancel();
            mBroadcastTimerTask = null;
            mBroadcastTimer = null;
        }
    }

    private void stopPublish() {
        stopRecordAnimation();
        if (mPusherPresenter != null) {
//            mPusherPresenter.changeLiveStatus(mUserId, PusherPresenter.LIVE_STATUS_OFFLINE);
            mPusherPresenter.stopLive(mUserId, mGroupId);
            mPusherPresenter.stopPusher();
            mPusherPresenter = null;
        }
        if (mIMChatPresenter != null) {
            mIMChatPresenter.deleteGroup();
        }
        if (mAudioPlayer != null) {
            mAudioPlayer.stop();
            mAudioPlayer = null;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        mTXCloudVideoView.onResume();
        if (mPasuing) {
            mPasuing = false;
            if (mPusherPresenter != null) {
                mPusherPresenter.resumePusher();
            }
//            mIMChatPresenter.sendMessage(Constants.AVIMCMD_HOST_BACK, "");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mTXCloudVideoView.onPause();
        if (mPusherPresenter != null) {
            mPusherPresenter.pausePusher();
            mIMChatPresenter.sendMessage(Constants.AVIMCMD_HOST_LEAVE, "");
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTXCloudVideoView.onDestroy();
        stopPublish();
    }

    @Override
    public void onBackPressed() {
        showComfirmDialog(getString(R.string.msg_stop_push_error), false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ribang:
                Intent intent = new Intent(this, RibangActivity.class);
                intent.putExtra("live_id", ACache.get(this).getAsString("user_id"));
//                LogUtil.i(TAG, "ID="+ACache.get(this).getAsString("user_id"));

                startActivity(intent);
                break;
            case R.id.btn_close:
                showComfirmDialog(getString(R.string.msg_stop_push_error), false);
                break;
            case R.id.btn_message_input:
                showInputMsgDialog();
                break;
            case R.id.btn_setting:
                //setting坐标
                mPusherPresenter.showSettingPopupWindow(btnSettingView, mSettingLocation);
                break;
            case R.id.btn_email:
                llLeaveMessageDetail.setVisibility(View.VISIBLE);

//                srlLeaveMsg.setColorScheme(R.color.color1, R.color.color2,
//                        R.color.color3, R.color.color4);
//                srlLeaveMsg.setOnRefreshListener(this);
                //获取留言消息
                getLeaveMsg(UserInfoCache.getUserId(mContext));
                break;
            case R.id.cancle_show:
                llLeaveMessageDetail.setVisibility(View.GONE);

                break;
            case R.id.iv_head_icon://显示主播用户信息
                /**
                 * json.put("userId", ACache.get(mIMChatView.getContext()).getAsString("user_id"));
                 json.put("nickName", ACache.get(mIMChatView.getContext()).getAsString("nickname"));
                 json.put("headPic", ACache.get(mIMChatView.getContext()).getAsString("head_pic"));
                 */
                SimpleUserInfo hostInfo = new SimpleUserInfo(ACache.get(mContext).getAsString("user_id"),
                        ACache.get(mContext).getAsString("nickname"),
                        ACache.get(mContext).getAsString("head_pic"));

                showUserInfoDialog(mContext, hostInfo);
                break;
        }

    }

    private void showInputMsgDialog() {
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = mInputTextMsgDialog.getWindow().getAttributes();

        lp.width = display.getWidth(); //设置宽度
        mInputTextMsgDialog.getWindow().setAttributes(lp);
        mInputTextMsgDialog.setCancelable(true);
        mInputTextMsgDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        mInputTextMsgDialog.show();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (mSettingLocation[0] == 0 && mSettingLocation[1] == 0) {
            btnSettingView.getLocationOnScreen(mSettingLocation);
        }
    }

    public static void invoke(Activity activity, String roomTitle, String location, boolean isRecord, int bitrateType) {
        Intent intent = new Intent(activity, LivePublisherActivity.class);
        intent.putExtra(Constants.ROOM_TITLE,

                TextUtils.isEmpty(roomTitle) ? UserInfoMgr.getInstance().getNickname() : roomTitle);
        intent.putExtra(Constants.GROUP_ID, ACache.get(activity).getAsString("group_id"));
        intent.putExtra(Constants.USER_ID, ACache.get(activity).getAsString("user_id"));
        intent.putExtra(Constants.USER_NICK, ACache.get(activity).getAsString("nickname"));
//        intent.putExtra(Constants.USER_HEADPIC, ACache.get(activity).getAsString("head_pic_small"));
        intent.putExtra(Constants.COVER_PIC, ACache.get(activity).getAsString("head_pic"));
        intent.putExtra(Constants.USER_LOC, location);
        position=location;
        intent.putExtra(Constants.IS_RECORD, isRecord);
        intent.putExtra(Constants.BITRATE, bitrateType);
        activity.startActivity(intent);
    }

    @Override
    public void onGetPushUrl(String pushUrl, int errorCode) {
        mPushUrl = pushUrl;
        if (errorCode == 0) {
            startPublish();
//            mGroupIdd();
        } else {
            ToastUtils.showShort(this, "您不是主播请认证......");
            finish();
        }
    }

    @Override
    public void onPushEvent(int event, Bundle bundle) {
        //推流相关事件
        if (event == TXLiveConstants.PUSH_EVT_PUSH_BEGIN) {
            ivLiveBg.setVisibility(View.GONE);
//            mPusherPresenter.changeLiveStatus(mUserId, PusherPresenter.LIVE_STATUS_ONLINE);
            if (!mOfficialMsgSended) {
                refreshMsg("", getString(R.string.live_system_name), getString(R.string.live_system_notify), Constants.AVIMCMD_ENTER_LIVE);
                mOfficialMsgSended = true;
            }
        }

        if (event < 0) {
            if (event == TXLiveConstants.PUSH_ERR_OPEN_CAMERA_FAIL) {
                showComfirmDialog("打开摄像头失败", true);
            } else if (event == TXLiveConstants.PUSH_ERR_OPEN_MIC_FAIL) {
                showComfirmDialog("打开麦克风失败", true);
            } else if (event == TXLiveConstants.PUSH_ERR_NET_DISCONNECT) {
                showComfirmDialog("请检查网络", true);
            }
        }
        if (event == TXLiveConstants.PUSH_WARNING_HW_ACCELERATION_FAIL) {
            mTXPushConfig.setAutoAdjustBitrate(false);
            mTXPushConfig.setVideoResolution(TXLiveConstants.VIDEO_RESOLUTION_TYPE_360_640);
            mTXPushConfig.setVideoBitrate(500);
            mTXPushConfig.setVideoFPS(15);
            mTXPushConfig.setHardwareAcceleration(false);
            mPusherPresenter.setConfig(mTXPushConfig);
        }

        if (event == TXLiveConstants.PUSH_WARNING_NET_BUSY) {
            ToastUtils.makeText(this, "当前网络比较差,请检查网络！", Toast.LENGTH_SHORT);
        }

        Log.i(TAG, "onPushEvent: event =" + event + " event description = " + bundle.getString(TXLiveConstants.EVT_DESCRIPTION));

    }

    @Override
    public void onNetStatus(Bundle bundle) {
        //推流过程中网络相关的状态改变
        Log.i(TAG, "onNetStatus: cpu usage = " + bundle.getString(TXLiveConstants.NET_STATUS_CPU_USAGE));

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void dismissLoading() {

    }

    @Override
    public void showMsg(String msg) {
        ToastUtils.makeText(this, msg, Toast.LENGTH_SHORT);
    }

    @Override
    public void showMsg(int msg) {
        ToastUtils.makeText(this, msg, Toast.LENGTH_SHORT);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public FragmentManager getFragmentMgr() {
        return getFragmentManager();
    }

    @Override
    public void onQinmidu(String kkkk) {
        zhubojine = kkkk;
        LogUtil.d("dddddd", "dd" + kkkk);
        zhuboxinxi.setText(zhubojine);

    }

    @Override
    public void oNneber(int ooo) {
        tvMemberCount.setText(String.format(Locale.CHINA, "%d", ooo));
    }


    @Override
    public void onHand(final List<SimpleUserInfo> result) {
        simList=result;
        mAvatarListAdapter = new UserAvatarListAdapter(this, IMLogin.getInstance().getLastUserInfo().identifier, result);//
        mUserAvatarList.setAdapter(mAvatarListAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mUserAvatarList.setLayoutManager(linearLayoutManager);
        mAvatarListAdapter.removeItem("");
        mAvatarListAdapter.setmUserAvatarList(result);
        LogUtil.i(TAG, "oooo=" + result.size());
        mAvatarListAdapter.setOnItemClickListener(new UserAvatarListAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                showUserInfoDialog(mContext, result.get(position));
            }
        });
    }

    @Override
    public void onTextSend(String msg, boolean tanmuOpen) {
        mIMChatPresenter.sendTextMsg(msg);
        refreshMsg(mUserId, "我:", msg, Constants.AVIMCMD_TEXT_TYPE);
    }

    /**
     * 消息刷新显示
     *
     * @param name    发送者
     * @param context 内容
     * @param type    类型 （上线线消息和 聊天消息）
     */
    public void refreshMsg(String id, String name, String context, int type) {
        ChatEntity entity = new ChatEntity();
        name = TextUtils.isEmpty(name) ? getString(R.string.live_tourist) : name;
        entity.setId(id);
        entity.setSenderName(name);
        entity.setContext(context);
        entity.setType(type);
        notifyMsg(entity);
    }

    /**
     * 刷新消息列表
     *
     * @param entity
     */
    private void notifyMsg(final ChatEntity entity) {

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mArrayListChatEntity.add(entity);
                mChatMsgListAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onLeaveMsgSend(String msg, String userId, String accId) {
        sendString(userId, accId, msg);
//        Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
    }

//    public void getlistview() {
//        OkHttpUtils
//                .post()
//                .url("http://zhibonew.zzsike.com/m/molie/group_live_list.php")
//                .addParams("groupId", "@TGS#a5SW3XYEY")
//                .build()
//                .execute(new GenericsCallback<SimpleUserInfo>(new JsonGenericsSerializator()){
//
//                             @Override
//                             public void onError(Call call, Exception e, int id) {
//                                 LogUtil.d("UUUUUUUUUUUUUUUUU","response.avatar"+e);
//                             }
//
//                             @Override
//                             public void onResponse(SimpleUserInfo response, int id) {
//                               LogUtil.d("UUUUUUUUUUUUUUUUU","response.avatar"+response.avatar);
//                             }
//                });
//        FetchGroupMemberListReuest felist=new FetchGroupMemberListReuest(groupid);
//        AsyncHttp.instance().post(felist, new AsyncHttp.IHttpListener() {
//            @Override
//            public void onStart(int requestId) {
//
//            }
//
//            @Override
//            public void onSuccess(int requestId, Response response) {
//
//
//                Type type = new TypeToken<JsonBean>() { }.getType();
//                Gson gson = new Gson();
//
//                JsonBean a = gson.fromJson(json, type);

//            }
//
//            @Override
//            public void onFailure(int requestId, int httpStatus, Throwable error) {
//
//            }
//        });

//    }


    class BroadcastTimerTask extends TimerTask {

        @Override
        public void run() {
            mSecond++;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvBroadcastTime.setText(OtherUtils.formattedTime(mSecond));
                }
            });
        }
    }


    /**
     * 显示确认消息
     *
     * @param msg     消息内容
     * @param isError true错误消息（必须退出） false提示消息（可选择是否退出）
     */
    public void showComfirmDialog(String msg, Boolean isError) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(msg);

        if (!isError) {
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    stopPublish();
                    EndDetailFragment.invoke(getFragmentManager(), mSecond, mPraiseCount, mTotalCount);
                    unbindService(mGiftConn);

                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        } else {
            //当情况为错误的时候，直接停止推流
            stopPublish();
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    stopPublish();
//                    finish();
                }
            });
        }
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * 弹出禁言和留言的 对话框
     *
     * @param mContext
     * @param userInfo
     */
    private void showUserInfoDialog(final Context mContext, final SimpleUserInfo userInfo) {
        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mContext, R.style.NoBackGroundDialog);
        final android.support.v7.app.AlertDialog dialog = builder.create();
        View view = View.inflate(mContext, R.layout.dialog_user_info, null);
        final ImageView userIcon = (ImageView) view.findViewById(R.id.dialog_user_icon);
        TextView userNick = (TextView) view.findViewById(R.id.dialog_user_nick);
        final TextView userId = (TextView) view.findViewById(R.id.dialog_user_id);
        TextView userMsg = (TextView) view.findViewById(R.id.dialog_user_msg);
        TextView banned = (TextView) view.findViewById(R.id.dialog_banned);
        TextView report = (TextView) view.findViewById(R.id.dialog_report);
        TextView leaveMsg = (TextView) view.findViewById(R.id.dialog_leave_message);
//        ImageView cancle = (ImageView) view.findViewById(R.id.dialog_cancle);

        RequestManager req = Glide.with(mContext);
        req.load(userInfo.avatar).placeholder(R.drawable.bg).into(userIcon);
        userNick.setText(userInfo.username);
        userId.setText("ID:" + userInfo.user_id);

        builder.setView(view);

        banned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mContext, "禁言", Toast.LENGTH_SHORT).show();
                liveId = ACache.get(mContext).getAsString("user_id");
                LogUtil.i("Banned", "liveId=" + liveId);
                if (!userInfo.user_id.equals(liveId)){
                    mPusherPresenter.bannesWord(userInfo.user_id, liveId, mContext);
                }else{
                    Toast.makeText(mContext, "此操作不能进行", Toast.LENGTH_SHORT).show();
                }

            }
        });

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2017/5/20
                String reportId = ACache.get(mContext).getAsString("user_id");
                String reportedId = userInfo.user_id;
                if (!reportId.equals(reportedId)){
                    mPusherPresenter.reportUser(reportId, reportedId, mContext);
                }else{
                    Toast.makeText(mContext, "此操作不能进行", Toast.LENGTH_SHORT).show();
                }
            }
        });

        leaveMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLeaveMsgDialog(ACache.get(mContext).getAsString("user_id"), userInfo.user_id);
            }
        });

        builder.create().show();
    }

    private void sendString(String user_id, String acceId, String content) {
        mPusherPresenter.sendLeaveMsg(user_id, acceId, content, mContext);
    }

    private void showLeaveMsgDialog(String userId, String accId) {
        mInputLeaveMsgDialog = new InputLeaveMsgDialog(this, R.style.InputDialog, userId, accId);
        mInputLeaveMsgDialog.setmOnTextSendListener(this);

        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = mInputTextMsgDialog.getWindow().getAttributes();

        lp.width = display.getWidth(); //设置宽度
        mInputLeaveMsgDialog.getWindow().setAttributes(lp);
        mInputLeaveMsgDialog.setCancelable(true);
        mInputLeaveMsgDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        mInputLeaveMsgDialog.show();
    }
}
