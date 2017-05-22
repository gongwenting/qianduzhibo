package com.qiandu.live.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.qiandu.live.R;
import com.qiandu.live.model.GiftInfo;
import com.qiandu.live.ui.gift.CustomGridView;

import java.util.ArrayList;

/**
 * @Description: 礼物分页适配器
 * @author: Andruby
 * @date: 2016年7月9日 下午5:46:44
 */
public class GiftPagerAdapter extends PagerAdapter {

    private Context mContext;
    private ArrayList<ArrayList<GiftInfo>> mGiftPagerList;
    private GiftItemClickListener mGiftItemClickListener;

    public GiftPagerAdapter(Context context) {
        this.mContext = context;
    }


    public interface GiftItemClickListener {
        void onItemGiftClick(GiftInfo giftInfo);
    }

    public void setOnItemGiftClickListener(GiftItemClickListener onItemGiftClickListener) {
        mGiftItemClickListener = onItemGiftClickListener;
    }


    public void setGiftPagerList(ArrayList<ArrayList<GiftInfo>> giftPagerList) {
        mGiftPagerList = giftPagerList;
    }

    @Override
    public int getCount() {
        return mGiftPagerList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View giftPager = LayoutInflater.from(mContext).inflate(R.layout.gift_pager_item_layout, null);
        GridView gvGift = (CustomGridView) giftPager.findViewById(R.id.gv_live_gift);
        LiveGiftAdapter liveGiftAdapter = new LiveGiftAdapter(mContext, mGiftPagerList.get(position), mGiftItemClickListener);
        gvGift.setAdapter(liveGiftAdapter);
        container.addView(giftPager, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return giftPager;
    }
}
