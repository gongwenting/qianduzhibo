package com.qiandu.live.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.qiandu.live.R;
import com.qiandu.live.activity.LivePlayerActivity;

import com.qiandu.live.adapter.HomeHotAdapter;
import com.qiandu.live.model.LiveInfo;
import com.qiandu.live.presenter.LiveListPresenter;
import com.qiandu.live.presenter.ipresenter.ILiveListPresenter;
import com.qiandu.live.ui.list.ListFootView;
import com.qiandu.live.ui.listload.ProgressBarHelper;
import com.qiandu.live.utils.LogUtil;
import com.qiandu.live.utils.ToastUtils;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeHotFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, ILiveListPresenter.ILiveListView, ProgressBarHelper.ProgressBarClickListener {

    public static final int START_LIVE_PLAY = 100;
    private static final String TAG = "HomeHotFragment";
    private RecyclerView mVideoListView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private HomeHotAdapter homeHotAdapter;
    private Context context;
    //避免连击
    private long mLastClickTime = 0;
    private int mListType;
    private LiveListPresenter mLiveListPresenter;
    protected ProgressBarHelper pbHelp;

    ListFootView mListFootView;
    
    private ArrayList<LiveInfo> liveInfoList;

    public HomeHotFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        Bundle arguments = getArguments();
        mListType = arguments.getInt("LISTTYPE", 1);
    }

    public static HomeHotFragment newInstance(int listType) {
        HomeHotFragment fragment = new HomeHotFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("LISTTYPE", listType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home_hot;
    }

    @Override
    protected void initView(View view) {
        mSwipeRefreshLayout = obtainView(R.id.swipe_refresh_layout_list);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mLiveListPresenter = new LiveListPresenter(this, mListType);
        mVideoListView = obtainView(R.id.live_list);

        liveInfoList = mLiveListPresenter.getLiveListFormCache();
        LogUtil.i(TAG, "SIZE="+liveInfoList.size()+"");
        homeHotAdapter = new HomeHotAdapter(getActivity(), R.layout.live_item_view, liveInfoList);
        mVideoListView.setAdapter(homeHotAdapter);
        mVideoListView.setLayoutManager(new LinearLayoutManager(context));

        pbHelp = new ProgressBarHelper(mContext, obtainView(R.id.ll_data_loading));
        mListFootView = new ListFootView(mContext);
        mListFootView.initView();
//        mVideoListView.addFooterView(mListFootView);
    }

    @Override
    protected void initData() {
        // TODO: 2017/5/7
        refreshListView();
    }

    private void refreshListView() {
        if (mLiveListPresenter.reloadLiveList()) {
            mSwipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(true);
                }
            });
        }
    }

    @Override
    protected void setListener(View view) {
        pbHelp.setProgressBarClickListener(this);
    }

    @Override
    public void onRefresh() {
        refreshListView();
    }

    @Override
    public void onLiveList(int retCode, final ArrayList<LiveInfo> result, boolean refresh) {
        if (retCode == 0) {
            homeHotAdapter = null;
            if (result != null && result.size() > 0) {
                homeHotAdapter = new HomeHotAdapter(mContext, R.layout.live_item_view,result);
                mVideoListView.setAdapter(homeHotAdapter);
                mVideoListView.setLayoutManager(new LinearLayoutManager(getActivity()));

                homeHotAdapter.setOnItemClickListener(new HomeHotAdapter.OnRecyclerViewItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
//                        Toast.makeText(getActivity(), "ITEM="+position, Toast.LENGTH_SHORT).show();

                        if (0 == mLastClickTime || System.currentTimeMillis() - mLastClickTime > 1000) {
                            if (homeHotAdapter.getItemCount() > position) {
                                LiveInfo item = result.get(position);
                                if (item == null) {
                                    Log.e(TAG, "live list item is null at icon_position:" + position);
                                    return;
                                }
                                startLivePlay(item);
                            }
                        }
                    }
                });

                pbHelp.goneLoading();
            } else {
                pbHelp.showNoData();
            }

            if (refresh) {
//                homeHotAdapter.notifyDataSetChanged();
            }
        } else {
            ToastUtils.showShort(mContext, "刷新列表失败");
            pbHelp.showNetError();
        }
        mSwipeRefreshLayout.setRefreshing(false);
        if (!mLiveListPresenter.isHasMore()) {
            mListFootView.setLoadDone();
        }
    }

    /**
     * 开始播放视频
     *
     * @param item 视频数据
     */
    private void startLivePlay(final LiveInfo item) {
        LivePlayerActivity.invoke(getActivity(),item);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void dismissLoading() {

    }

    @Override
    public void showMsg(String msg) {

    }

    @Override
    public void showMsg(int msg) {

    }

    @Override
    public void clickRefresh() {
        refreshListView();
    }
}
