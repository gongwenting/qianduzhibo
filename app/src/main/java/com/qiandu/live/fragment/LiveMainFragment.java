package com.qiandu.live.fragment;

import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;

import com.qiandu.live.R;
import com.qiandu.live.presenter.LiveMainPresenter;
import com.qiandu.live.ui.pagersliding.PagerSlidingTabStrip;

/**
 * @description: 直播列表主页
 * @author: Andruby
 * @time: 2016/9/3 16:19
 */
public class LiveMainFragment extends BaseFragment implements ViewPager.OnPageChangeListener ,View.OnClickListener{
    private ViewPager mViewPager;
    private LiveMainPresenter mLiveMainPresenter;
    private PagerSlidingTabStrip pagerSlidingTabStrip;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_live_main;
    }

    @Override
    protected void initView(View view) {
        mViewPager =obtainView(R.id.viewpager);
        pagerSlidingTabStrip = obtainView(R.id.circle_index_indicator);
        pagerSlidingTabStrip.setTextColorResource(R.color.white);
        pagerSlidingTabStrip.setIndicatorColorResource(R.color.white);
        pagerSlidingTabStrip.setDividerColor(Color.TRANSPARENT);
        pagerSlidingTabStrip.setTextSelectedColorResource(R.color.white);
        pagerSlidingTabStrip.setTextSize(getResources().getDimensionPixelSize(R.dimen.h6));
        pagerSlidingTabStrip.setTextSelectedSize(getResources().getDimensionPixelSize(R.dimen.h10));
        pagerSlidingTabStrip.setUnderlineHeight(1);
    }

    @Override
    protected void initData() {
        //透明状态栏
       getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        mLiveMainPresenter = new LiveMainPresenter(mContext,getChildFragmentManager());
        mViewPager.setAdapter(mLiveMainPresenter.getAdapter());
        mViewPager.setCurrentItem(1);
        mViewPager.addOnPageChangeListener(this);
        pagerSlidingTabStrip.setViewPager(mViewPager);
        pagerSlidingTabStrip.setOnPageChangeListener(this);
    }

    protected void setListener(View view) {
        obtainView(R.id.search).setOnClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search:
//                SearchActivity.invoke(mContext);
                break;

        }
    }
}
