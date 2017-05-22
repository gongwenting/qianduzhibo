package com.qiandu.live.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by admin on 2017/3/20.
 */
public class MyPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> list;
    private List<String> titls;

    public MyPagerAdapter(FragmentManager fm, List<Fragment> list, List<String> titls) {
        super(fm);
        this.list = list;
        this.titls = titls;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titls.get(position);
    }
}
