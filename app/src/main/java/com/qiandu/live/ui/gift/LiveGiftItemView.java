package com.qiandu.live.ui.gift;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qiandu.live.R;
import com.qiandu.live.adapter.LiveGiftAdapter;
import com.qiandu.live.model.GiftInfo;
import com.bumptech.glide.Glide;
import com.qiandu.live.utils.LogUtil;

/**
 * @description: 礼物的ItemView
 * @author: Andruby
 * @time: 2016/12/17 10:23
 */
public class LiveGiftItemView extends LinearLayout implements View.OnClickListener {
    private static final String TAG = LiveGiftItemView.class.getSimpleName();
    private Context mContext;
    private ImageView mGiftImage;
    private TextView mGiftName;
    private TextView mGiftPrice;
    private GiftInfo mGiftInfo;

    private LiveGiftAdapter.GiftViewClickListener mOnGiftViewClickListener;

    public LiveGiftItemView(Context context) {
        this(context, null);
    }

    public LiveGiftItemView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public LiveGiftItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.live_gift_item_view, this);
        initView();
    }

    public void setGiftViewClickListener(LiveGiftAdapter.GiftViewClickListener
                                                 onGiftViewClickListener) {
        mOnGiftViewClickListener = onGiftViewClickListener;
    }


    private void initView() {
        mGiftImage = (ImageView) findViewById(R.id.item_gift_icon);
        mGiftName = (TextView) findViewById(R.id.tv_item_gift_name);
        mGiftPrice = (TextView) findViewById(R.id.tv_item_gift_cost);
    }

    public void setData(GiftInfo data) {
        mGiftInfo = data;
        if (data != null) {
            setViewsVisibility(View.VISIBLE);

            LogUtil.d("IMGING",""+data.getBigpicUrl());

            Glide.with(mContext)
                    .load(data.getBigpicUrl())
                    .placeholder(R.drawable.live_head_placeholder)
                    .centerCrop()
                    .into(mGiftImage);

            mGiftName.setText(data.getName());
                if ( data.getCost()==0) {
                    mGiftPrice.setText(mContext.getString(R.string.gift_market_no_syb));
                } else {

                    mGiftPrice.setText(( data.getCost()) + mContext.getString(R.string.gift_market_text_syb));
                }
            mGiftImage.setOnClickListener(this);
        } else {
            setViewsVisibility(View.GONE);
        }
    }

    public GiftInfo getGiftInfo() {
        return mGiftInfo;
    }


    private void setViewsVisibility(int visible) {
        mGiftImage.setVisibility(visible);
        mGiftName.setVisibility(visible);
        mGiftPrice.setVisibility(visible);
    }

    @Override
    public void onClick(View v) {
        if (mGiftInfo.getCost()==0) {

            LogUtil.d("ONNNNNNNNNNNNNNNNNNNN","DIANJI");
            int currentCount = mGiftInfo.getCurrentCount();
            currentCount--;
            if (mOnGiftViewClickListener != null && currentCount > -1) {
                mGiftInfo.setCurrentCount(currentCount);
                mOnGiftViewClickListener.onGiftViewClick(mGiftInfo);
            }
        } else {
            mOnGiftViewClickListener.onGiftViewClick(mGiftInfo);
        }
    }

}
