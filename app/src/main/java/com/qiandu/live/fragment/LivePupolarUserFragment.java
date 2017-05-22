package com.qiandu.live.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.qiandu.live.R;
//import com.qiandu.live.adapter.RecyclerViewadapterPaly;

/**
 * Created by admin on 2017/5/6.
 */
public class LivePupolarUserFragment extends BaseFragment{
    //常量
    public static final int START_LIVE_PLAY = 100;
    //适配器
//    private RecyclerViewadapterPaly mVideoListViewAdapter;
    //recyclerview
    private RecyclerView recyclerView;
    //下拉刷新
    private SwipeRefreshLayout mSwipeRefreshLayout;
    //避免连击
    private long mLastClickTime = 0;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home_2;
    }

    @Override
    protected void initView(View view) {
//        mSwipeRefreshLayout = obtainView(R.id.swipe_refresh_layout_list);
//        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
//                android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
//        mSwipeRefreshLayout.setOnRefreshListener(this);
    }
    public static LivePupolarUserFragment newInstance(int listType) {
        LivePupolarUserFragment fragment = new LivePupolarUserFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("LISTTYPE", listType);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    protected void initData() {
//        refreshListView();
    }

    @Override
    protected void setListener(View view) {

    }
    /**
     * 刷新直播列表
     */
//    private void refreshListView() {
//        if (mLiveListPresenter.reloadLiveList()) {
//            mSwipeRefreshLayout.post(new Runnable() {
//                @Override
//                public void run() {
//                    mSwipeRefreshLayout.setRefreshing(true);
//                }
//            });
//        }
//    }

}
