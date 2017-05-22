package com.qiandu.live.ui.gift;

import android.content.Context;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.qiandu.live.R;
import com.qiandu.live.adapter.GiftPagerAdapter;
import com.qiandu.live.model.GiftInfo;
import com.qiandu.live.ui.customviews.BallSpinLoadingView;
import com.qiandu.live.utils.LogUtil;
import com.qiandu.live.utils.ToastUtils;

import java.util.ArrayList;

/**
 * @description: 礼物的View
 * @author: Andruby
 * @time: 2016/12/17 10:23
 */
public class LiveGiftView extends LinearLayout implements View.OnClickListener,
        GiftPagerAdapter.GiftItemClickListener {
    private static final String TAG = LiveGiftView.class.getSimpleName();
    private Context mContext;
    private ViewPager mVpGift;
    private int sybCount;
    private GiftInfo sendGiftInfo;
    private IndicatorView mVpGiftInd;
    private BallSpinLoadingView loadView;
    private FrameLayout mRlRootView;
    private TextView mTvGotoPay;
    private TextView mTvBalance;
    private RelativeLayout mGiftRootView;
    //礼物数据集合
    private ArrayList<ArrayList<GiftInfo>> mGiftPagerList;

    private LiveGiftViewListener mLiveGiftViewListener;
    //新的礼物
    private GiftPagerAdapter mGiftPagerAdapter;
    private ImageView mGiftImage;
    private TextView mGiftCount;
    public final static int SEND_GIFT = 1;
    private int mCoinCount;
    private TextView tvCoinCount;
    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SEND_GIFT:
                    //发送礼物
                    sendGiftToLiveHost();
                    break;
            }
        }
    };
    private boolean isAnimPlaying;
    private Animation giftScaleAnim;
    private Animation inAnim;
    private Animation outAnim;
    private View mOutSide;
    private String clickTagId;

    /**
     * 送出礼物
     */
    private void sendGiftToLiveHost() {
        if (sendGiftInfo == null) {
            return;
        }
        mGiftRootView.setVisibility(View.GONE);
        int totalPrice = sendGiftInfo.getCost()* sendGiftInfo.getGiftCount();
        if (sybCount >= totalPrice) {
            mLiveGiftViewListener.sendGift(sendGiftInfo);
            sendGiftInfo = null;
        } else {
            mLiveGiftViewListener.showPayDialog();
            sendGiftInfo = null;
        }
    }
    public LiveGiftView(Context context) {
        this(context, null);
    }

    public LiveGiftView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public LiveGiftView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.live_gift_view, this);
        mGiftPagerList = new ArrayList<>();
        initView();
        initListener();
        initAnim();
    }

    private void initAnim() {
        inAnim = AnimationUtils.loadAnimation(mContext, R.anim
                .live_gift_view_in);
        outAnim = AnimationUtils.loadAnimation(mContext, R.anim
                .live_gift_view_out);
    }
    public void initLiveGiftView(LiveGiftViewListener giftViewListener, RelativeLayout rootView) {
        mLiveGiftViewListener = giftViewListener;
        mGiftRootView = rootView;
        initGiftAnim();

    }
    private void initGiftAnim() {
        giftScaleAnim = AnimationUtils.loadAnimation(mContext, R.anim
                .live_gift_market);
        giftScaleAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isAnimPlaying = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isAnimPlaying = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mGiftImage = (ImageView) mGiftRootView.findViewById(R.id
                .live_gift_img);
        mGiftCount = (TextView) mGiftRootView.findViewById(R.id.live_gift_num);
    }
    private void initView() {
        mRlRootView = (FrameLayout) findViewById(R.id.loadingViewRoot);
        mOutSide = findViewById(R.id.outside);
        loadView = new BallSpinLoadingView(mContext);   //圆形进度条
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout
                .LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        loadView.setLayoutParams(layoutParams);
        mRlRootView.addView(loadView);

        tvCoinCount = (TextView) findViewById(R.id.tv_live_coin_count);

        mVpGift = (ViewPager) findViewById(R.id.vp_share_live_dialog);
        mVpGiftInd = (IndicatorView) findViewById(R.id.id_indicator);
        mTvGotoPay = (TextView) findViewById(R.id.tv_live_gift_pay);
        mTvBalance = (TextView) findViewById(R.id.tv_live_coin_count);
        setGifLoadingView(loadView, mRlRootView);
    }

    private void initListener() {
        mTvGotoPay.setOnClickListener(this);
        mOutSide.setOnClickListener(this);
    }
    /**
     * 设置金币个数
     *
     * @param coinCount
     */
    public void setCoinCount(int coinCount) {
        this.sybCount = coinCount;
        if (mTvBalance != null) {
            mTvBalance.setText(coinCount + "");
        }
    }

    /**
     * 设置并处理分页，直播礼物的礼物信息
     *
     * @param allGiftInfoList
     */
    public void setGiftPagerList(ArrayList<GiftInfo> allGiftInfoList) {
        if (allGiftInfoList != null) {
            ArrayList<GiftInfo> giftInfos = new ArrayList<>();
            while (allGiftInfoList.size() > 0) {
                GiftInfo firstGiftInfo = allGiftInfoList.remove(0);
                giftInfos.add(firstGiftInfo);
//                firstGiftInfo.setCurrentCount(100);
                if (giftInfos.size() < 8) {
                    continue;
                } else {
                    mGiftPagerList.add(giftInfos);
                    giftInfos = new ArrayList<>();
                }
            }
            if (!giftInfos.isEmpty()) {
                mGiftPagerList.add(giftInfos);
            }
        }
        showGiftInfo();
    }

    /**
     * 当dialog已经展示，数据没有到位时，获取到数据后重新刷新数据
     */
    private void showGiftInfo() {
        mGiftPagerAdapter = new GiftPagerAdapter(mContext);
        mGiftPagerAdapter.setGiftPagerList(mGiftPagerList);
        mGiftPagerAdapter.setOnItemGiftClickListener(this);
        //设置Viewpager的预加载页数为礼物分页-1，防止viewpager销毁页
        mVpGift.setOffscreenPageLimit(mGiftPagerList.size() - 1);
        mVpGift.setAdapter(mGiftPagerAdapter);
        mVpGiftInd.setViewPager(mVpGift);
        setGifLoadingView(loadView, mRlRootView);
    }

    /**
     * 根据Adapter中数据的长度判断是否需要展示loading
     *
     * @param loadView
     */
    private void setGifLoadingView(View loadView, View LoadRootView) {
        if (mGiftPagerList.size() > 0) {
            loadView.setVisibility(View.INVISIBLE);
            LoadRootView.setVisibility(View.INVISIBLE);
        } else {
            loadView.setVisibility(View.VISIBLE);
            LoadRootView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_live_gift_pay) {
            mLiveGiftViewListener.gotoPay();
        } else if (v.getId() == R.id.outside) {
            dismiss();
        }
    }

//    public void setCoinCount(int coinCount) {
//        tvCoinCount.setText(coinCount + "");
//    }


    /**
     * 礼物点击的回调
     *
     * @param giftInfo
     */
    @Override
    public void onItemGiftClick(GiftInfo giftInfo) {

        if (isAnimPlaying) {
            return;
        }
        if (mGiftRootView != null) {
            Shader shader = new LinearGradient(0, 0, 0, mGiftCount.getTextSize(),
                    mContext.getResources().getColor(R.color.live_gift_red_start),
                    mContext.getResources().getColor(R.color.live_gift_red_end),
                    Shader.TileMode.CLAMP);
            mGiftCount.getPaint().setShader(shader);

            String giftid = clickTagId;
            int count = 0;
            if (sendGiftInfo != null) {
                count = sendGiftInfo.getGiftCount();
            }


            if (!TextUtils.isEmpty(giftid)) {
                //不是第一次
                if (TextUtils.equals(giftid, giftInfo.getId())) {
                    //View中的礼物和上一个一样
                    if (mGiftRootView.getVisibility() == View.VISIBLE) {
                        //礼物view可见为连点
                        count++;
                        mGiftCount.setText("x" + count);
                        giftInfo.setGiftCount(count);
                        sendGiftInfo = giftInfo;
                        if (calculationTotalPrice()) {
                        delaySendGift(2000);
                        }
                    } else {
                        //礼物View不可见后，为又点了同一个礼物
                        mGiftCount.setText("x1");
                        giftInfo.setGiftCount(1);
                        sendGiftInfo = giftInfo;
                        mGiftImage.startAnimation(giftScaleAnim);
                        mGiftRootView.setVisibility(View.VISIBLE);
                        if (calculationTotalPrice()) {
                        delaySendGift(2000);
                        }
                    }
                } else {
                    //礼物view展示一个新礼物
                    sendGiftToLiveHost();
                    //一个新的礼物
                    Glide.with(mContext).load(giftInfo.getBigpicUrl()).placeholder(R.drawable
                            .live_head_placeholder).into(mGiftImage);
//                    mGiftImage.setTag(giftInfo.getGiftId());
                    clickTagId = giftInfo.getId();
                    mGiftCount.setText("x1");
                    giftInfo.setGiftCount(1);
                    sendGiftInfo = giftInfo;
                    mGiftImage.startAnimation(giftScaleAnim);
                    mGiftRootView.setVisibility(View.VISIBLE);
                    if (calculationTotalPrice()) {
                    delaySendGift(2200);
                    }
                }
            } else {
                //第一次点礼物
                Glide.with(mContext).load(giftInfo.getBigpicUrl()).placeholder(R.drawable
                        .live_head_placeholder).into(mGiftImage);
//                mGiftImage.setTag(giftInfo.getGiftId());
                clickTagId = giftInfo.getId();
                mGiftCount.setText("x1");
                giftInfo.setGiftCount(1);
                sendGiftInfo = giftInfo;
                mGiftImage.startAnimation(giftScaleAnim);
                mGiftRootView.setVisibility(View.VISIBLE);
                if (calculationTotalPrice()) {
                delaySendGift(2200);
                }
            }
        }
    }
    /**
     * 延迟发送礼物
     *
     * @param time 延迟时间
     */
    private void delaySendGift(long time) {
        mHandler.removeCallbacksAndMessages(null);
        mHandler.sendEmptyMessageDelayed(SEND_GIFT, time);
    }
    /**
     * 计算礼物价值，币不足时弹出提示
     */
    private boolean calculationTotalPrice() {
        int totalPrice = sendGiftInfo.getCost() * sendGiftInfo.getGiftCount();
        if (sybCount < totalPrice) {

            LogUtil.d("UUUUUUUUUUUU",""+sybCount+"////////////////"+totalPrice);
            sendGiftInfo = null;
            mGiftRootView.setVisibility(View.GONE);
            mLiveGiftViewListener.showPayDialog();
            return false;
        }
        sybCount -= totalPrice;
        return true;
    }
    public void show() {
        inAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                setVisibility(View.VISIBLE);
                mLiveGiftViewListener.isShowing(true);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        this.startAnimation(inAnim);
    }

    public void dismiss() {
        outAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setVisibility(View.GONE);
                mLiveGiftViewListener.isShowing(false);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        this.startAnimation(outAnim);
    }
    public interface LiveGiftViewListener {
        public void isShowing(boolean isShowing);

        public void showPayDialog();

        public void sendGift(GiftInfo giftInfo);

        void gotoPay();
    }
}
