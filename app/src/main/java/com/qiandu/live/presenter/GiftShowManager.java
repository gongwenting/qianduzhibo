package com.qiandu.live.presenter;

import android.content.Context;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.qiandu.live.R;
import com.qiandu.live.model.GiftShow;
import com.qiandu.live.model.GiftWithUerInfo;
import com.qiandu.live.model.LiveUserInfo;
import com.qiandu.live.utils.GlideCircleTransform;
import com.qiandu.live.utils.OtherUtils;

/**
 * @description: 展示礼物的管理类，在其内部持有一个单个用户发送的一个礼物展示用的信息对列，
 * 当对列为空时，会通过回调请求分配新的礼品信息
 * @author: Andruby
 * @time: 2016/12/18 14:04
 */
public class GiftShowManager {

    private LinearLayout mGiftContainer;//礼物的容器
    private Context mContext;//上下文

    private TranslateAnimation inAnim;//礼物View出现的动画 500
    private TranslateAnimation iconInAnim;//礼物view中图片出现的动画 1000
    private Animation outAnim;//礼物View消失的动画 500
    private Animation giftNumAnim;//修改礼物数量的动画 200

    public final static int SHOW_GIFT_FLAG = 0;//显示礼物
    public final static int REMOVE_GIFT_VIEW = 1;//播放跳数动画
    public final static int SHOW_NEXT_NUM = 2;//移除礼物

    private ImageView ivSenderImage;
    private TextView tvGiftNum;
    private TextView tvSenderName;
    private TextView tvGiftMsg;
    private ImageView ivGiftIcon;
    private View mGiftView;
    private GiftShow mGiftShow;

    private ShowGiftIsEmptyListener mShowGiftIsEmptyListener;
    private GiftViewClickListener mOnGiftViewClickListener;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_GIFT_FLAG:
                    GiftWithUerInfo giftInfo = null;
                    if (mShowGiftIsEmptyListener != null) {
                        giftInfo = mShowGiftIsEmptyListener.getMoreGiftInfo();
                    }
                    if (giftInfo == null) {
                        startGetGift();
                    } else {
                        addGift(giftInfo);
                        giftIn();
                    }
                    break;
                case REMOVE_GIFT_VIEW:
                    giftOut();
                    break;
                case SHOW_NEXT_NUM:
                    giftNumAnim();
                    break;
            }
        }
    };

    public interface GiftViewClickListener {
        void onGiftViewClick(GiftShow showVo);
    }

    public GiftShowManager(Context mContext, LinearLayout giftContainer) {
        this.mContext = mContext;
        this.mGiftContainer = giftContainer;
        initAnim();
    }

    public void setGiftViewClickListener(GiftViewClickListener giftViewClickListener) {
        mOnGiftViewClickListener = giftViewClickListener;
    }

    public interface ShowGiftIsEmptyListener {
        GiftWithUerInfo getMoreGiftInfo();
    }

    public void setShowGiftManagerIsEmptyListener(GiftShowManager.ShowGiftIsEmptyListener
                                                          showGiftIsEmptyListener) {
        mShowGiftIsEmptyListener = showGiftIsEmptyListener;
    }

    /**
     * 礼物进入动画
     */
    private void giftIn() {
        if (mGiftView == null) {
            initView();
        }
        setGiftViewInfo();

        mGiftView.startAnimation(inAnim);//播放礼物View出现的动
        ivGiftIcon.startAnimation(iconInAnim);
        //礼物view进来，开始播放跳数动画
    }

    public void startGetGift() {
        mHandler.sendEmptyMessageDelayed(SHOW_GIFT_FLAG, 1000);
    }

    private void setGiftViewInfo() {
        //送礼物的头像
        OtherUtils.showPicWithUrl(mContext, ivSenderImage, mGiftShow.getUserImage(), R.drawable.live_head_placeholder);
        //显示礼物的数量
        Shader shader = new LinearGradient(0, 0, 0, tvGiftNum.getTextSize(),
                mContext.getResources().getColor(R.color.live_gift_red_start),
                mContext.getResources().getColor(R.color.live_gift_red_end),
                Shader.TileMode.CLAMP);
        tvGiftNum.getPaint().setShader(shader);
        tvGiftNum.setText("x1");
        //送礼物的用户名

        tvSenderName.setText(mGiftShow.getUserName());
        //送礼物的msg
        tvGiftMsg.setText(mContext.getString(R.string.live_gift_send)
                + mGiftShow.getGiftInfo().getGiftCount() + mContext.getString(R.string.live_gift_unit)
                + mGiftShow.getGiftInfo().getName());
        //礼物图片
        Glide.with(mContext)
                .load(mGiftShow.getGiftInfo().getBigpicUrl())
                .placeholder(R.drawable.live_head_placeholder)
                .transform(new GlideCircleTransform(mContext)).into(ivGiftIcon);
    }

    /**
     * 礼物条数
     */
    private void giftNumAnim() {
        //判断是否为最后一个，如果是，直接将展示数字调整为最终数字
        int jumpShowNum = mGiftShow.getShowNum() + mGiftShow.getJumpNum();
        if (jumpShowNum >= mGiftShow.getCount()) {
            mGiftShow.setShowNum(mGiftShow.getCount());
            //礼物移出动画
            mHandler.sendEmptyMessageDelayed(REMOVE_GIFT_VIEW, 400);
        } else {
            mGiftShow.setShowNum(jumpShowNum);
            //下一次数字动画
            mHandler.sendEmptyMessageDelayed(SHOW_NEXT_NUM, 400);
        }
        tvGiftNum.setText("x" + mGiftShow.getShowNum());
        if (tvGiftNum.getVisibility() != View.VISIBLE) {
            tvGiftNum.setVisibility(View.VISIBLE);
        }
        tvGiftNum.startAnimation(giftNumAnim);
    }

    /**
     * 礼物出去
     */
    private void giftOut() {
        mGiftView.startAnimation(outAnim);
    }

    public void destory() {
        mHandler.removeCallbacksAndMessages(null);
    }


    private void initAnim() {
        inAnim = (TranslateAnimation) AnimationUtils.loadAnimation(mContext, R.anim.live_gift_in);
        outAnim = AnimationUtils.loadAnimation(mContext, R.anim.live_gift_out);
        iconInAnim = (TranslateAnimation) AnimationUtils.loadAnimation(mContext, R.anim.live_gift_icon_in);
        giftNumAnim = AnimationUtils.loadAnimation(mContext, R.anim.live_gift_num);

        inAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mGiftView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //礼物数量跳动动画
                mHandler.sendEmptyMessage(SHOW_NEXT_NUM);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        outAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mGiftView.setVisibility(View.GONE);
                mGiftShow = null;
                mHandler.sendEmptyMessage(SHOW_GIFT_FLAG);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }

    private void initView() {
        mGiftView = LayoutInflater.from(mContext).inflate(R.layout.live_gifts_item_layout, null);
        mGiftContainer.addView(mGiftView);
        tvGiftNum = (TextView) mGiftView.findViewById(R.id.live_gift_num);
        ivSenderImage = (ImageView) mGiftView.findViewById(R.id.live_gift_user_icon);
        tvSenderName = (TextView) mGiftView.findViewById(R.id.live_gift_userName);
        tvGiftMsg = (TextView) mGiftView.findViewById(R.id.live_gift_usermsg);
        ivGiftIcon = (ImageView) mGiftView.findViewById(R.id.live_gift_img);

        mGiftView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnGiftViewClickListener != null) {
                    mOnGiftViewClickListener.onGiftViewClick(mGiftShow);
                }
            }
        });
    }


    //添加礼物显示
    public void addGift(GiftWithUerInfo giftWithUerInfo) {
        LiveUserInfo userInfo = giftWithUerInfo.getUserInfo();
        mGiftShow = new GiftShow();
        int count = giftWithUerInfo.getGiftCount();
        if (count == 0) {
            count = giftWithUerInfo.getGiftInfo().getGiftCount();
        }
        int jumpNum = 1;
        if (count > 120) {
            jumpNum = count / 120 + 1;
        }
        mGiftShow.setUserId(userInfo.getUserId());
        mGiftShow.setUserName(userInfo.getNickname());
        mGiftShow.setUserImage(userInfo.getUserImage());
        mGiftShow.setMsg(count + ""); //msg目前用来携带礼物的总数
        mGiftShow.setCount(count);
        mGiftShow.setGetGiftTimeTag(System.currentTimeMillis());
        mGiftShow.setJumpNum(jumpNum);
        mGiftShow.setShowNum(0);
        mGiftShow.setGiftInfo(giftWithUerInfo.getGiftInfo());
    }

}
