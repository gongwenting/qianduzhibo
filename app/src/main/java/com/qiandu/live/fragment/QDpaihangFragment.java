package com.qiandu.live.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qiandu.live.R;
import com.qiandu.live.adapter.MyPagerAdapter;
import com.qiandu.live.ui.pagersliding.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/4/7.
 */
public class QDpaihangFragment extends Fragment {
    private ViewPager viewPager;

    private PagerSlidingTabStrip slidingTabStrip;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_paihang, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.vPager1);
        List<Fragment> list = new ArrayList<>();
        list.add(new RankingFragment_1());
        list.add(new RankingFragment_2());
        list.add(new RankingFragment_3());

        List<String> titles = new ArrayList<>();
        titles.add("主播");
        titles.add("富豪");
        titles.add("家族");
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(new MyPagerAdapter(getChildFragmentManager(), list, titles));
        slidingTabStrip = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
        slidingTabStrip.setViewPager(viewPager);
        slidingTabStrip.setTextColorResource(R.color.white);
        slidingTabStrip.setIndicatorColorResource(R.color.white);
        slidingTabStrip.setDividerColor(Color.TRANSPARENT);
        slidingTabStrip.setTextSelectedColorResource(R.color.white);
        slidingTabStrip.setTextSize(getResources().getDimensionPixelSize(R.dimen.h10));
        slidingTabStrip.setTextSelectedSize(getResources().getDimensionPixelSize(R.dimen.h12));
        slidingTabStrip.setUnderlineHeight(1);
        return view;
    }
}
