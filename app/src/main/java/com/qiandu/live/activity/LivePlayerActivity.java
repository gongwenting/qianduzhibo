package com.qiandu.live.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
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
import com.qiandu.live.LiveApp;
import com.qiandu.live.R;
import com.qiandu.live.adapter.ChatMsgListAdapter;
import com.qiandu.live.adapter.LeaveMessageAdapter;
import com.qiandu.live.adapter.UserAvatarListAdapter;
import com.qiandu.live.http.response.Ainformation;
import com.qiandu.live.logic.FrequeMgr;
import com.qiandu.live.logic.IMLogin;
import com.qiandu.live.model.ChatEntity;
import com.qiandu.live.model.GiftInfo;
import com.qiandu.live.model.GiftWithUerInfo;
import com.qiandu.live.model.LeaveMsgInfo;
import com.qiandu.live.model.LiveInfo;
import com.qiandu.live.model.SimpleUserInfo;
import com.qiandu.live.model.UserInfo;
import com.qiandu.live.model.UserInfoCache;
import com.qiandu.live.presenter.IMChatPresenter;
import com.qiandu.live.presenter.LiveGiftPresenter;
import com.qiandu.live.presenter.LivePlayerPresenter;
import com.qiandu.live.presenter.ipresenter.IIMChatPresenter;
import com.qiandu.live.presenter.ipresenter.ILiveGiftPresenter;
import com.qiandu.live.presenter.ipresenter.ILivePlayerPresenter;
import com.qiandu.live.service.LiveGiftServices;
import com.qiandu.live.ui.customviews.EndDetailFragment;
import com.qiandu.live.ui.customviews.HeartLayout;
import com.qiandu.live.ui.customviews.InputLeaveMsgDialog;
import com.qiandu.live.ui.customviews.InputTextMsgDialog;
import com.qiandu.live.ui.gift.LiveGiftView;
import com.qiandu.live.utils.AsimpleCache.ACache;
import com.qiandu.live.utils.Constants;
import com.qiandu.live.utils.ListCacheUtils;
import com.qiandu.live.utils.LogUtil;
import com.qiandu.live.utils.OtherUtils;
import com.qiandu.live.utils.ToastUtils;
import com.tencent.TIMMessage;
import com.tencent.imcore.MemberInfo;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayConfig;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @Description: 观众 观看播放页面
 * @author: Andruby
 * @date: 2016年7月8日 下午4:46:44
 */
public class LivePlayerActivity extends IMBaseActivity implements View.OnClickListener,
        ILivePlayerPresenter.ILivePlayerView, IIMChatPresenter.IIMChatView, InputTextMsgDialog.OnTextSendListener, ILiveGiftPresenter.ILiveGiftView, LiveGiftView.LiveGiftViewListener, InputLeaveMsgDialog.OnTextSendListener {
    private static final String TAG = LivePlayerActivity.class.getSimpleName();
    public final static int LIVE_PLAYER_REQUEST_CODE = 1000;
    private Button gift;
    private TXCloudVideoView mTXCloudVideoView;
    private TXLivePlayConfig mTXPlayConfig = new TXLivePlayConfig();
    private boolean mPausing = false;
    private String mPlayUrl = "";
    private boolean mPlaying = false;
    private LiveInfo mLiveInfo;
    //关注
    private ImageView guanzhu;
    private boolean guanzhumeiyou;
    private TextView zhuboxinxi;
    private String liveId;
    private RelativeLayout ribang;
    //关闭
    private ImageView guanbi;
    private LivePlayerPresenter mLivePlayerPresenter;
    private IMChatPresenter mIMChatPresenter;
    //zhubojingbi
    private String zhubojine;
    private int jine;

    //主播信息
    private ImageView ivHeadIcon;
    private ImageView ivRecordBall;
    private TextView tvPuserName;
    private TextView tvMemberCount;
    private int mMemberCount = 0; //实时人数
    private int mTotalCount = 0; //总观众人数
    private int mPraiseCount = 0;
    private long mLiveStartTime = 0;
    private UserInfo mUserinfo;
    private int mJoinCount = 0;
    private long mSecond = 0;
    //官方消息
    private ImageView ivLiveBg;
    private boolean mOfficialMsgSended = false;
    private InputTextMsgDialog mInputTextMsgDialog;
    private String noNuber;
    //消息列表
    private ArrayList<ChatEntity> mArrayListChatEntity = new ArrayList<>();
    private ChatMsgListAdapter mChatMsgListAdapter;
    private ListView mListViewMsg;
    private List<SimpleUserInfo> simList;
    //点赞频率控制
    private FrequeMgr mLikeFrequeControl;
    private HeartLayout mHeartLayout;

    //观众列表
    private RecyclerView mUserAvatarList;
    private UserAvatarListAdapter mAvatarListAdapter;
    private String intimacy;
    //礼物
    private LiveGiftView mLiveGiftView;
    private LiveGiftPresenter mLiveGiftPresenter;
    private Gson mGson = new Gson();
    private boolean isGifViewShowing;
    //礼物服务
    private FrameLayout mGiftRootView;
    private LiveGiftServices.LiveGiftShowBinder mLiveGiftShowBinder;

    /*
   留言墙
    */
    private Button btnLeaveMessage;
    private LinearLayout llLeaveMessageDetail;
    private ImageView ivCancleShowLeaveMessage;
    private RecyclerView rvLeaveMessage;
    private LeaveMessageAdapter leaveMessageAdapter;
    private List<LeaveMsgInfo> leaveMsgList;


    private InputLeaveMsgDialog mInputLeaveMsgDialog;

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
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    protected int getLayoutId() {
        Intent intent = new Intent(this, LiveGiftServices.class);
        bindService(intent, mGiftConn, BIND_AUTO_CREATE);
        return R.layout.activity_live_player;
    }
    private void initCount() {
        mLiveGiftPresenter.coinCount(ACache.get(this).getAsString("user_id"));
        mLiveGiftView.setCoinCount(jine);
        mLivePlayerPresenter.qinmidu(mLiveInfo.liveId);

        LogUtil.d("DDDD","D"+zhuboxinxi);
    }
    @Override
    protected void initView() {
        getDataFormIntent();
        initLiveView();

        //mPlayerView即step1中添加的界面view
        mTXCloudVideoView = obtainView(R.id.video_view);
        mLivePlayerPresenter = new LivePlayerPresenter(this);
        mLivePlayerPresenter.initPlayerView(mTXCloudVideoView, mTXPlayConfig);
        mIMChatPresenter = new IMChatPresenter(this);
        guanbi = (ImageView) findViewById(R.id.btn_close);
        guanbi.setOnClickListener(this);
        //主播信息
        tvPuserName = obtainView(R.id.tv_broadcasting_time);
        tvPuserName.setText(OtherUtils.getLimitString(mLiveInfo.userInfo.nickname, 10));
        ivRecordBall = obtainView(R.id.iv_record_ball);
        ivRecordBall.setVisibility(View.GONE);
        ivHeadIcon = obtainView(R.id.iv_head_icon);
        OtherUtils.showPicWithUrl(this, ivHeadIcon, mLiveInfo.userInfo.headpic, R.drawable.default_head);
        tvMemberCount = obtainView(R.id.tv_member_counts);

        gift = (Button) findViewById(R.id.btn_gift);
        mInputTextMsgDialog = new InputTextMsgDialog(this, R.style.InputDialog);
        mInputTextMsgDialog.setmOnTextSendListener(this);
        mLivePlayerPresenter.qinmidu(mLiveInfo.liveId);

        if (mPlayUrl != null) {
            mLivePlayerPresenter.startPlay(mPlayUrl, TXLivePlayer.PLAY_TYPE_LIVE_RTMP); //推荐FLV
        } else {
            showToast("主播正在休息.....");
        }

        mIMChatPresenter.joinGroup(mLiveInfo.groupId);
        //观众列表
        mUserAvatarList = obtainView(R.id.rv_user_avatar);
        mUserAvatarList.setVisibility(View.VISIBLE);

       mLivePlayerPresenter.groupMember(mLiveInfo.groupId);

        //显示人数
//        tvMemberCount.setText(simList.size());

//        mLivePlayerPresenter.qinmidu(liveId);
        intimacy=ACache.get(this).getAsString("intimacy");
        LogUtil.d("AAAAA",""+intimacy);
        zhuboxinxi.setText(intimacy);
        gift.setOnClickListener(this);
        mListViewMsg = obtainView(R.id.im_msg_listview);
        mChatMsgListAdapter = new ChatMsgListAdapter(this, mListViewMsg, mArrayListChatEntity);
        mListViewMsg.setAdapter(mChatMsgListAdapter);

        mHeartLayout = obtainView(R.id.heart_layout);

        ivLiveBg = obtainView(R.id.iv_live_bg);
        initGift();

        btnLeaveMessage = obtainView(R.id.btn_email);
        llLeaveMessageDetail = obtainView(R.id.leave_message);
        ivCancleShowLeaveMessage = obtainView(R.id.cancle_show);
        llLeaveMessageDetail.setVisibility(View.GONE);
        rvLeaveMessage = obtainView(R.id.leave_message_info);

    }

    private void getLeaveMsg(String userId) {
        LogUtil.i("GLM", "getLeaveMsg:" + userId);

        leaveMsgList = mLivePlayerPresenter.getLeaveMsg(userId);
        LogUtil.i("GLM", "getLeaveMsg:SIZE=" + leaveMsgList.size());

        leaveMsgList = ListCacheUtils.getLeaveMsgInfoList();

        leaveMessageAdapter = new LeaveMessageAdapter(mContext, leaveMsgList);
        rvLeaveMessage.setAdapter(leaveMessageAdapter);
        rvLeaveMessage.setLayoutManager(new LinearLayoutManager(mContext));
    }
    @Override
    protected void initData() {
        String headPic = ACache.get(this).getAsString("head_pic");
        if (!TextUtils.isEmpty(headPic)) {
            OtherUtils.blurBgPic(this, ivLiveBg, ACache.get(this).getAsString("head_pic"), R.drawable.bg);
        }

        leaveMsgList = new ArrayList<>();
        leaveMsgList = mLivePlayerPresenter.getLeaveMsg(UserInfoCache.getUserId(mContext));
    }

    private void initGift() {
        mGiftRootView = obtainView(R.id.liveGiftLayout);
        mLiveGiftView = obtainView(R.id.live_gift_view);
        mLiveGiftPresenter = new LiveGiftPresenter(this);
        mLiveGiftView.initLiveGiftView(this, (RelativeLayout) obtainView(R.id.gift_item_layout));
        mLiveGiftPresenter = new LiveGiftPresenter(this);
        mLiveGiftPresenter.giftList(ACache.get(this).getAsString("user_id"));
        mLiveGiftPresenter.coinCount(ACache.get(this).getAsString("user_id"));
        initCount();

    }


    private void getDataFormIntent() {
        Intent intent = getIntent();
        mLiveInfo = (LiveInfo) intent.getSerializableExtra(Constants.LIVE_INFO);
        mPlayUrl = mLiveInfo.plugAddressRtmp;
//        ainformation=(Ainformation)intent.getSerializableExtra(Constants.INTIMACY);
//         ain=ainformation.getIntimacy();
        LogUtil.e(TAG, "mPlayUrl:" + mPlayUrl);

    }

    @Override
    protected void setListener() {
        btnLeaveMessage.setOnClickListener(this);
        ivCancleShowLeaveMessage.setOnClickListener(this);
        ivHeadIcon.setOnClickListener(this);
    }


    /**
     * 初始化观看直播界面
     */
    private void initLiveView() {
        mTXCloudVideoView = obtainView(R.id.video_view);
        Intent i = getIntent();
        mLiveInfo.is_atten = (int) i.getLongExtra(Constants.IS_ATTEN, mLiveInfo.is_atten);
        liveId=mLiveInfo.liveId;


//        mLiveInfo.liveId=q.getStringExtra(Constants.LIVEID,mLiveInfo.liveId);
        LogUtil.d("is_atten", "is_atten+++++++++++++" + mLiveInfo.is_atten);
        guanzhu = (ImageView) findViewById(R.id.textview_guanzhu);
        zhuboxinxi= (TextView) findViewById(R.id.text_qinmidu);
        ribang= (RelativeLayout) findViewById(R.id.ribang);
        if (mLiveInfo.is_atten == 1) {
            guanzhumeiyou = true;
            guanzhu.setImageResource(R.mipmap.guanzhuguanzhu);
        } else {
            guanzhumeiyou = false;
            guanzhu.setImageResource(R.mipmap.guanzhued);
        }
        guanzhu.setOnClickListener(this);
        zhuboxinxi.setOnClickListener(this);

        ribang.setOnClickListener(this);
    }


    @Override

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ribang:
                Intent intent=new Intent(this,RibangActivity.class);

                LogUtil.i("live_id",mLiveInfo.liveId);
                intent.putExtra("live_id",mLiveInfo.liveId);
                startActivity(intent);
                break;

            case R.id.btn_close:
                showComfirmDialog(getString(R.string.msg_stop_watch), false);

                break;
            case R.id.btn_message_input:
                showInputMsgDialog();
                break;
            case R.id.textview_guanzhu:
                if (guanzhumeiyou) {
                    guanzhumeiyou = false;
                    mLivePlayerPresenter.guanzhu(ACache.get(this).getAsString("user_id"), mLiveInfo.liveId, "y");
                    guanzhu.setImageResource(R.mipmap.guanzhuguanzhu);

                } else {
                    guanzhumeiyou = true;
                    mLivePlayerPresenter.quxiaoguanzhu(ACache.get(this).getAsString("user_id"), mLiveInfo.liveId, "n");
                    guanzhu.setImageResource(R.mipmap.guanzhued);

                }
                break;
            case R.id.btn_gift:
                mLiveGiftView.show();
                break;

            case R.id.btn_email:
                llLeaveMessageDetail.setVisibility(View.VISIBLE);

                //获取留言消息
                getLeaveMsg(UserInfoCache.getUserId(mContext));

                break;
            case R.id.cancle_show:
                llLeaveMessageDetail.setVisibility(View.GONE);

                break;
            case R.id.btn_like:
                if (mLikeFrequeControl == null) {
                    mLikeFrequeControl = new FrequeMgr();
                    mLikeFrequeControl.init(2, 1);
                }
                if (mLikeFrequeControl.canTrigger()) {
                    if (!"1".equals(ACache.get(this).getAsString(mLiveInfo.liveId + "_first_praise"))) {
                        mIMChatPresenter.sendPraiseFirstMessage();

                        mHeartLayout.addFavor();
                    } else {
                        mIMChatPresenter.sendPraiseMessage();
                        mHeartLayout.addFavor();
                    }
                }
                Log.i(TAG, "onClick: sendPraiseMessage");
                break;
            case R.id.iv_head_icon:
                SimpleUserInfo hostInfo = new SimpleUserInfo(mLiveInfo.liveId,
                        mLiveInfo.userInfo.nickname,mLiveInfo.userInfo.headpic);
                showUserInfoDialog(mContext, hostInfo);
            default:
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
    public void onBackPressed() {
        showComfirmDialog(getString(R.string.msg_stop_watch), false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mLivePlayerPresenter.playerPause();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mLivePlayerPresenter.playerResume();

        mLivePlayerPresenter.startPlay(mPlayUrl, TXLivePlayer.PLAY_TYPE_LIVE_RTMP);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mGiftConn != null) {
            unbindService(mGiftConn);
        }
        stopPlay();

    }

    public static void invoke(Activity activity, LiveInfo liveInfo) {
        Intent intent = new Intent(activity, LivePlayerActivity.class);
        intent.putExtra(Constants.LIVE_INFO, liveInfo);
        activity.startActivityForResult(intent, LIVE_PLAYER_REQUEST_CODE);
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
    public void onPlayEvent(int event, Bundle bundle) {
        //播放相关事件
        if (event == TXLiveConstants.PLAY_EVT_PLAY_BEGIN) {
            ivLiveBg.setVisibility(View.GONE);
//            //可以上传播放状态
            if (!mOfficialMsgSended) {
                refreshMsg("", getString(R.string.live_system_name), getString(R.string.live_system_notify), Constants.AVIMCMD_ENTER_LIVE);
                mOfficialMsgSended = true;
            }
        }

        if (event < 0) {
            if (event == TXLiveConstants.PLAY_ERR_NET_DISCONNECT) {
                showComfirmDialog("请检查网络", true);
            }
        }
        if (event == TXLiveConstants.PLAY_WARNING_HW_ACCELERATION_FAIL) {
            mLivePlayerPresenter.enableHardwareDecode(false);
            stopPlay();
            mLivePlayerPresenter.startPlay(mPlayUrl, TXLivePlayer.PLAY_TYPE_LIVE_RTMP);
        }

        Log.i(TAG, "onPlayEvent: event =" + event + " event description = " + bundle.getString(TXLiveConstants.EVT_DESCRIPTION));

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
                    stopPlay();
//                    EndDetailFragment.invoke(getFragmentManager(), mSecond, mPraiseCount, mTotalCount);
                    showEndDetail();

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
            stopPlay();
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onNetStatus(Bundle bundle) {
        //播放信息及状态
        Log.i(TAG, "net status, CPU:" + bundle.getString(TXLiveConstants.NET_STATUS_CPU_USAGE) +
                ", RES:" + bundle.getInt(TXLiveConstants.NET_STATUS_VIDEO_WIDTH) + "*" + bundle.getInt(TXLiveConstants.NET_STATUS_VIDEO_HEIGHT) +
                ", SPD:" + bundle.getInt(TXLiveConstants.NET_STATUS_NET_SPEED) + "Kbps" +
                ", FPS:" + bundle.getInt(TXLiveConstants.NET_STATUS_VIDEO_FPS) +
                ", ARA:" + bundle.getInt(TXLiveConstants.NET_STATUS_AUDIO_BITRATE) + "Kbps" +
                ", VRA:" + bundle.getInt(TXLiveConstants.NET_STATUS_VIDEO_BITRATE) + "Kbps");
    }

    @Override
    public void doLikeResult(int result) {

    }

    @Override
    public void onGroupMembersResult(int retCode, int totalCount, ArrayList<SimpleUserInfo> membersList) {
        if (retCode == 0 && totalCount > 0) {
            mTotalCount += totalCount;
            mMemberCount += totalCount;
            tvMemberCount.setText("" + mMemberCount);
            if (membersList != null) {
                for (SimpleUserInfo userInfo : membersList) {
                    mAvatarListAdapter.addItem(userInfo);
                }
            }
        } else {
            LogUtil.e(TAG, "onGroupMembersResult failed");
        }
    }

    @Override
    public void onQinmidu(String kkkk) {
        zhubojine=kkkk;

        zhuboxinxi.setText(zhubojine);


}

    @Override
    public void onNeber(int ooo) {
        tvMemberCount.setText(String.format(Locale.CHINA, "%d", ooo));

    }

    @Override
    public void onHand(final ArrayList<SimpleUserInfo> result) {
        simList=result;
        mAvatarListAdapter = new UserAvatarListAdapter(this, IMLogin.getInstance().getLastUserInfo().identifier, result);//
        mUserAvatarList.setAdapter(mAvatarListAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mUserAvatarList.setLayoutManager(linearLayoutManager);
        mAvatarListAdapter.setmUserAvatarList(result);
        //Item的点击事件
        mAvatarListAdapter.setOnItemClickListener(new UserAvatarListAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(mContext, "Item:"+position, Toast.LENGTH_SHORT).show();
                showUserInfoDialog(mContext, result.get(position));
            }
        });
    }

    private void stopPlay() {
        mLivePlayerPresenter.quitGroup(mLiveInfo.userId);  // mLiveInfo.liveId,, mLiveInfo.groupId
        mLivePlayerPresenter.stopPlay(true);
        mTXCloudVideoView.onDestroy();
        mIMChatPresenter.quitGroup(mLiveInfo.groupId);
        ACache.get(this).put(mLiveInfo.groupId + "_first_praise", "0");
    }

    @Override
    public void onJoinGroupResult(int code, String msg) {
        if (code != 0) {
            showErrorAndQuit(Constants.ERROR_MSG_GROUP_NOT_EXIT);
            if (Constants.ERROR_QALSDK_NOT_INIT == code) {

                ((LiveApp) getApplication()).initSDK();
                if (mJoinCount > 1) {
                    showErrorAndQuit(Constants.ERROR_MSG_GROUP_NOT_EXIT);
                } else {
                    mIMChatPresenter.joinGroup(mLiveInfo.groupId);
//                    mJoinCount+=simList.size();
//                    mAvatarListAdapter.setmUserAvatarList(simList);
                }

            }
        } else {
//            mJoinCount+=simList.size();
//            mAvatarListAdapter.setmUserAvatarList(simList);
        }
    }



    @Override
    public void onGroupDeleteResult() {
        stopPlay();
        showEndDetail();
    }

    private void showEndDetail() {
        long second = 0;
        if (mLiveStartTime != 0) {
            second = (System.currentTimeMillis() - mLiveStartTime) / 1000;
        }
        EndDetailFragment.invoke(getFragmentManager(), second, mPraiseCount, mTotalCount);
//        unbindService(mGiftConn);
    }

    @Override
    public void receiveGift(boolean showGift, GiftWithUerInfo giftWithUerInfo) {
        mLiveGiftShowBinder.dispatchGift(giftWithUerInfo);

        //"android&"+
        //
        LogUtil.i("GIFTMSG", "getCost="+giftWithUerInfo.getGiftInfo().getCost());
        String result = giftWithUerInfo.getGiftInfo().getId()+"&"+giftWithUerInfo.getGiftInfo().getName()+"&"
                    +giftWithUerInfo.getGiftInfo().getBigpicUrl()+"&"+giftWithUerInfo.getGiftInfo().getGiftCount()+"&"+giftWithUerInfo.getGiftInfo().getCost();

        LogUtil.i("GIFTMSG", result);
        mIMChatPresenter.sendGiftMessage(result);

//        mIMChatPresenter.sendGiftMessage(mGson.toJson(giftWithUerInfo));
        initCount();
    }

    @Override
    public void handleTextMsg(SimpleUserInfo userInfo, String text) {
        ChatEntity entity = new ChatEntity();
        entity.setSenderName(userInfo.username + ":");
        entity.setContext(text);
        entity.setType(Constants.AVIMCMD_TEXT_TYPE);
        notifyMsg(entity);
    }

    @Override
    public void handlePraiseMsg(SimpleUserInfo userInfo) {
        mPraiseCount++;
        mHeartLayout.addFavor();
    }

    @Override
    public void handlePraiseFirstMsg(SimpleUserInfo userInfo) {
        mPraiseCount++;
        refreshMsg(userInfo.user_id, TextUtils.isEmpty(userInfo.username) ? userInfo.user_id : userInfo.username, "点亮了桃心", Constants.AVIMCMD_PRAISE_FIRST);
        mHeartLayout.addFavor();
    }

    @Override
    public void onSendMsgResult(int code, TIMMessage timMessage) {

    }

    @Override
    public void handleEnterLiveMsg(SimpleUserInfo userInfo) {
        Log.i(TAG, "handleEnterLiveMsg: ");
        //更新观众列表，观众进入显示
        //更新头像列表 返回false表明已存在相同用户，将不会更新数据
        if (!mAvatarListAdapter.addItem(userInfo))
            return;

       mMemberCount++;
        mLivePlayerPresenter.qinmidu(mLiveInfo.liveId);

        simList.add(userInfo);
        mAvatarListAdapter.addItem(userInfo);
       mAvatarListAdapter.setmUserAvatarList(simList);

        ChatEntity entity = new ChatEntity();
        entity.setSenderName(userInfo.username);
        if (userInfo.username.equals(""))
            entity.setContext(userInfo.user_id + "加入直播");
        else
            entity.setContext(userInfo.username + "加入直播");
        entity.setType(Constants.AVIMCMD_ENTER_LIVE);
        notifyMsg(entity);
    }

    @Override
    public void handleExitLiveMsg(SimpleUserInfo userInfo) {
        Log.i(TAG, "handleExitLiveMsg: ");
        //更新观众列表，观众退出显示
        if (mMemberCount > 0)
            mMemberCount--;
        mLivePlayerPresenter.qinmidu(mLiveInfo.liveId);
        simList.remove(userInfo);
        mAvatarListAdapter.removeItem(userInfo.user_id);
        mAvatarListAdapter.setmUserAvatarList(simList);

        ChatEntity entity = new ChatEntity();
        entity.setSenderName(userInfo.username);
//        if (userInfo.nickname.equals(""))
//            entity.setContext(userInfo.userId + "退出直播");
//        else
        entity.setContext(userInfo.username + "退出直播");
        entity.setType(Constants.AVIMCMD_EXIT_LIVE);
        notifyMsg(entity);
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
//LogUtil.d("DDDDDDDDDDD",""+
//        giftWithUerInfo.getUserInfo().getUserId()+"+++++++++++");
//        giftWithUerInfo.getUserInfo().getNickname()+"+++++++++++"+
//        giftWithUerInfo.getGiftInfo().getGiftCount()+"+++++++++++"+
//        giftWithUerInfo.getGiftInfo().getName());
//            giftWithUerInfo.getGiftInfo().getBigpicUrl();
//            giftWithUerInfo.getGiftInfo().getName();
//            giftWithUerInfo.getGiftInfo().getGiftCount();
//            giftWithUerInfo.getGiftInfo().getId();

//            String result = giftWithUerInfo.getGiftInfo().getId()+"&"+giftWithUerInfo.getGiftInfo().getName()+"&"
//                    +giftWithUerInfo.getGiftInfo().getBigpicUrl()+"&"+giftWithUerInfo.getGiftInfo().getGiftCount();
//
//            LogUtil.i("DDDDDDDDDDDDDD", result);
//
//            refreshMsg(giftWithUerInfo.getUserInfo().getUserId(), giftWithUerInfo.getUserInfo().getNickname(),
//                    "送出" + result, Constants.AVIMCMD_GIFT);

        }
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
    public void onTextSend(String msg, boolean tanmuOpen) {
        mIMChatPresenter.sendTextMsg(msg);
        ChatEntity entity = new ChatEntity();
        entity.setSenderName("我:");
        entity.setContext(msg);
        entity.setType(Constants.AVIMCMD_TEXT_TYPE);
        notifyMsg(entity);
    }


    @Override
    public void sendGiftFailed() {
        ToastUtils.makeText(this, "请查看你的账户余额", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSenderInfoCard(MemberInfo currentMember) {
    }

    @Override
    public void onCoinCount(int coinCount) {
        if (coinCount > 0) {
            mLiveGiftView.setCoinCount(coinCount);
            jine = coinCount;

        }
    }

    @Override
    public void onGiftList(ArrayList<GiftInfo> giftList) {
        mLiveGiftView.setGiftPagerList(giftList);
    }

    @Override
    public void onGiftListFailed() {
        ToastUtils.makeText(this, "get gift list error", Toast.LENGTH_SHORT);
    }

    @Override
    public void isShowing(boolean isShowing) {
        isGifViewShowing = isShowing;
    }


    @Override
    public void gotoPay() {
        startActivity(new Intent(this,CaifuActivity.class));
    }

    @Override
    public void showPayDialog() {
        mLiveGiftPresenter.showPayDialog();
    }

    @Override
    public void sendGift(GiftInfo giftInfo) {
        String result = giftInfo.getId()+"&"+giftInfo.getName()+"&"+giftInfo.getBigpicUrl()+"&"+giftInfo.getGiftCount();
        mLiveGiftPresenter.sendGift(giftInfo, ACache.get(this).getAsString("user_id"), mLiveInfo.liveId);
        LogUtil.i("DDDDDDDDDDDDDDDDDDDDDDDDDDDFFFFF", result);
        LogUtil.d("DDDDDDDDDDDDDDDDDDDDDDDDDDD", "" + giftInfo + "+++++++" + ACache.get(this).getAsString("user_id") + "+++++++" + mLiveInfo.liveId + "+++++++");
    }

    /**
     * 弹出禁言和留言的 对话框
     * @param mContext
     * @param userInfo
     */
    private void showUserInfoDialog(final Context mContext, final SimpleUserInfo userInfo) {
        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mContext, R.style.NoBackGroundDialog);
        final android.support.v7.app.AlertDialog dialog = builder.create();
        View view = View.inflate(mContext, R.layout.dialog_user_info, null);
        ImageView userIcon = (ImageView) view.findViewById(R.id.dialog_user_icon);
        TextView userNick = (TextView) view.findViewById(R.id.dialog_user_nick);
        final TextView userId = (TextView) view.findViewById(R.id.dialog_user_id);
        TextView userMsg = (TextView) view.findViewById(R.id.dialog_user_msg);
        TextView banned = (TextView) view.findViewById(R.id.dialog_banned);
        TextView leaveMsg = (TextView) view.findViewById(R.id.dialog_leave_message);
        TextView report = (TextView) view.findViewById(R.id.dialog_report);
//        ImageView cancle = (ImageView) view.findViewById(R.id.dialog_cancle);

        RequestManager req = Glide.with(mContext);
        req.load(userInfo.avatar).placeholder(R.drawable.bg).into(userIcon);
        userNick.setText(userInfo.username);
        userId.setText("ID:"+userInfo.user_id);

        builder.setView(view);

        banned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mContext, "禁言", Toast.LENGTH_SHORT).show();
                //mLiveInfo.liveId
                if (!userInfo.user_id.equals(mLiveInfo.liveId)){
                    Toast.makeText(mContext,"您没有权限", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(mContext,"您有权限", Toast.LENGTH_SHORT).show();
                }

                dialog.dismiss();
            }
        });

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reportId = ACache.get(mContext).getAsString("user_id");
                String reportedId = userInfo.user_id;

                mLivePlayerPresenter.reportUser(reportId, reportedId, mContext);
            }
        });
        leaveMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llLeaveMessageDetail.setVisibility(View.GONE);
                showLeaveMsgDialog(ACache.get(mContext).getAsString("user_id"), userInfo.user_id);

                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    private void sendString( String user_id, String acceId, String content) {
        mLivePlayerPresenter.sendLeaveMsg(user_id, acceId, content, mContext);
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

    @Override
    public void onLeaveMsgSend(String msg, String userId, String accId) {
        sendString(userId, accId, msg);
//        Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
    }
}
