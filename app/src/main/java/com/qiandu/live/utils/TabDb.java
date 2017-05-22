package com.qiandu.live.utils;

import android.support.v4.app.Fragment;

import com.qiandu.live.R;
import com.qiandu.live.fragment.LiveMainFragment;
import com.qiandu.live.fragment.QDhuodongFramgent;
import com.qiandu.live.fragment.QDpaihangFragment;
import com.qiandu.live.fragment.UserInfoFragment;


/**
 * Created by admin on 2017/4/6.
 */
public class TabDb {
    public static String[] getTabsTxt(){
    String[] tabs={"首页","排行","直播" ,"活动", "我的"};
    return tabs;
}
    public static int[] getTabsImg(){
        int[] ids={R.drawable.homeed, R.drawable.paihanged, R.drawable.playinged, R.drawable.played, R.drawable.wodeed};
        return ids;
    }
    public static int[] getTabsImgLight(){
        int[] ids={R.drawable.home, R.drawable.paihang, R.drawable.playing, R.drawable.play, R.drawable.wode};
        return ids;
    }
    public static Class[] getFragments(){

        Class[] clz={LiveMainFragment.class, QDpaihangFragment.class,Fragment.class, QDhuodongFramgent.class, UserInfoFragment.class};
        return clz;
    }
}