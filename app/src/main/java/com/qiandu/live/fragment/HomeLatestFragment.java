package com.qiandu.live.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.qiandu.live.R;
import com.qiandu.live.activity.LivePlayerActivity;
import com.qiandu.live.adapter.HomeLatestAdapter;
import com.qiandu.live.model.LiveInfo;
import com.qiandu.live.presenter.LiveListPresenter;
import com.qiandu.live.presenter.ipresenter.ILiveListPresenter;
import com.qiandu.live.ui.list.ListFootView;
import com.qiandu.live.ui.listload.ProgressBarHelper;
import com.qiandu.live.utils.LogUtil;
import com.qiandu.live.utils.ToastUtils;

import java.util.ArrayList;

public class HomeLatestFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, ILiveListPresenter.ILiveListView, ProgressBarHelper.ProgressBarClickListener {
    public static final int START_LIVE_PLAY = 100;
    private static final String TAG = "HomeHotFragment";
    private RecyclerView mVideoListView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private HomeLatestAdapter homeHotAdapter;
    private Context context;
    //避免连击
    private long mLastClickTime = 0;
    private int mListType;
    private LiveListPresenter mLiveListPresenter;
    protected ProgressBarHelper pbHelp;

    ListFootView mListFootView;

    private ArrayList<LiveInfo> liveInfoList;

    public HomeLatestFragment() {
    }

    public static HomeLatestFragment newInstance(int listType) {
        HomeLatestFragment fragment = new HomeLatestFragment();
        Bundle args = new Bundle();
        args.putInt("LISTTYPE", listType);
        fragment.setArguments(args);
        return fragment;
    }

    public static HomeLatestFragment newInstance(String param1, String param2) {
        HomeLatestFragment fragment = new HomeLatestFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        mListType = arguments.getInt("LISTTYPE", 2);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home_latest;
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
        homeHotAdapter = new HomeLatestAdapter(getActivity(), R.layout.home_hot_live_item_view, liveInfoList);
        mVideoListView.setAdapter(homeHotAdapter);
        mVideoListView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        pbHelp = new ProgressBarHelper(mContext, obtainView(R.id.ll_data_loading));
        mListFootView = new ListFootView(mContext);
        mListFootView.initView();
    }

    @Override
    protected void initData() {
        refreshListView();
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
                homeHotAdapter = new HomeLatestAdapter(mContext, R.layout.home_hot_live_item_view,result);
                mVideoListView.setAdapter(homeHotAdapter);
                mVideoListView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

                homeHotAdapter.setOnItemClickListener(new HomeLatestAdapter.OnRecyclerViewItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

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
                homeHotAdapter.notifyDataSetChanged();
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
    public void clickRefresh() {
        refreshListView();
    }

    /**
     * 开始播放视频
     *
     * @param item 视频数据
     */
    private void startLivePlay(final LiveInfo item) {
        LivePlayerActivity.invoke(getActivity(),item);
    }
}
