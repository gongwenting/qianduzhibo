package com.qiandu.live.fragment;

import android.os.Bundle;
import android.view.View;

import com.qiandu.live.R;
import com.qiandu.live.activity.BaseActivity;

/**
 * Created by admin on 2017/5/6.
 */
public class NewFramgent extends BaseFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home_3;
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

    public static NewFramgent newInstance(int listType) {
        NewFramgent fragment = new NewFramgent();
        Bundle bundle = new Bundle();
        bundle.putInt("LISTTYPE", listType);
        fragment.setArguments(bundle);
        return fragment;
    }

}
