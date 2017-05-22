package com.qiandu.live.presenter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.qiandu.live.activity.BaseActivity;
import com.qiandu.live.fragment.HomeHotFragment;
import com.qiandu.live.fragment.HomeLatestFragment;
import com.qiandu.live.fragment.LiveListFragment;
import com.qiandu.live.fragment.LivePupolarUserFragment;
import com.qiandu.live.fragment.NewFramgent;
import com.qiandu.live.fragment.WeikeFramgent;
import com.qiandu.live.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 首页
 * @author: Andruby
 * @time: 2016/12/18 14:04
 */
public class LiveMainPresenter {

    private static final int TYPE_LIST = 0;
    private static final int TYPE_HOT = 1;
    private static final int TYPE_DOYEN = 3;

    public static final String[] TITLE = new String[]{" 关注 ", " 热门 ", " 最新 ", " 微客 "};
    public static final int[] TYPE = new int[]{TYPE_LIST, TYPE_HOT, TYPE_DOYEN, TYPE_DOYEN};

    private static final String TAG = LiveMainPresenter.class.getName();

    private BaseActivity mContext;
    private PagerAdapter pagerAdapter;
    private FragmentManager fragmentManager;

    public LiveMainPresenter(BaseActivity context, FragmentManager childFragmentManager) {
        mContext = context;
        pagerAdapter = new LiveMainPresenter.PagerAdapter(childFragmentManager);
    }

    public FragmentStatePagerAdapter getAdapter() {
        return pagerAdapter;
    }


    class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            LogUtil.e(TAG, "FragmentStatePagerAdapter.getItem : " + position);
            if (TYPE[position] == TYPE_LIST) {
                LogUtil.e(TAG, " LiveListFragment.newInstance ");
                return HomeHotFragment.newInstance(position);
            } else if (TYPE[position] == TYPE_HOT){
                LogUtil.e(TAG, "LivePupolarUserFragment.newInstance ");
                return LiveListFragment.newInstance(position);
            }else if(TYPE[position] == TYPE_DOYEN){
                LogUtil.e(TAG, "LivePupolarUserFragment.newInstance ");
                return HomeLatestFragment.newInstance(position);
            }
            return LiveListFragment.newInstance(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }

        @Override
        public int getCount() {
            return TITLE.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLE[position];
        }
    }
}
