package com.qiandu.live.fragment;

import android.os.Bundle;
import android.view.View;

import com.qiandu.live.R;

/**
 * Created by admin on 2017/5/6.
 */
public class WeikeFramgent extends BaseFragment{
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home_4;
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener(View view) {

    }
    public static WeikeFramgent newInstance(int listType) {
        WeikeFramgent fragment = new WeikeFramgent();
        Bundle bundle = new Bundle();
        bundle.putInt("LISTTYPE", listType);
        fragment.setArguments(bundle);
        return fragment;
    }

}
