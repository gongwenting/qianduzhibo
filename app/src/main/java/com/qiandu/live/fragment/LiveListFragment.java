package com.qiandu.live.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.qiandu.live.R;
import com.qiandu.live.activity.LivePlayerActivity;
import com.qiandu.live.adapter.LiveListAdapter;
import com.qiandu.live.model.LiveInfo;
import com.qiandu.live.presenter.LiveListPresenter;
import com.qiandu.live.presenter.ipresenter.ILiveListPresenter;
import com.qiandu.live.ui.list.ListFootView;
import com.qiandu.live.ui.list.ListHeadView;
import com.qiandu.live.ui.listload.ProgressBarHelper;
import com.qiandu.live.utils.Constants;
import com.qiandu.live.utils.LogUtil;
import com.qiandu.live.utils.ToastUtils;

import java.util.ArrayList;

/**
 * @description: 直播列表页面，展示当前直播及回放视频
 * 界面展示使用：ListView+SwipeRefreshLayout
 * 列表数据Adapter：LiveListAdapter
 * 数据获取接口： LiveListPresenter
 * @author: Andruby
 * @time: 2016/9/3 16:19
 */
public class LiveListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, ILiveListPresenter.ILiveListView
        , ProgressBarHelper.ProgressBarClickListener, AbsListView.OnScrollListener {
    public static final int START_LIVE_PLAY = 100;
    private static final String TAG = "LiveListFragment";
    private ListView mVideoListView;
    private LiveListAdapter mVideoListViewAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    //避免连击
    private long mLastClickTime = 0;
    private int mListType;
    private LiveListPresenter mLiveListPresenter;
    protected ProgressBarHelper pbHelp;

    ListFootView mListFootView;
    ListHeadView mListHeadView;

    public LiveListFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        mListType = arguments.getInt("LISTTYPE", 0);
    }

    public static LiveListFragment newInstance(int listType) {
        LiveListFragment fragment = new LiveListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("LISTTYPE", listType);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_live_list;
    }

    @Override
    protected void initView(View view) {
        mSwipeRefreshLayout = obtainView(R.id.swipe_refresh_layout_list);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mLiveListPresenter = new LiveListPresenter(this, mListType);
        mVideoListView = obtainView(R.id.live_list);
        mVideoListViewAdapter = new LiveListAdapter(getActivity(),
                (ArrayList<LiveInfo>) mLiveListPresenter.getLiveListFormCache().clone());
        mVideoListView.setAdapter(mVideoListViewAdapter);
        pbHelp = new ProgressBarHelper(getActivity(), obtainView(R.id.ll_data_loading));
        mListFootView = new ListFootView(mContext);
        mListHeadView = new ListHeadView(mContext);
        mListHeadView.initView();
        mListFootView.initView();
        mVideoListView.addFooterView(mListFootView);
        mVideoListView.addHeaderView(mListHeadView);

    }

    @Override
    protected void initData() {
        refreshListView();

    }

    @Override
    protected void setListener(View view) {
        mVideoListView.setOnScrollListener(this);
        mVideoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (0 == mLastClickTime || System.currentTimeMillis() - mLastClickTime > 1000) {
                    if(mVideoListViewAdapter.getCount()>i){
                        LiveInfo item = mVideoListViewAdapter.getItem(i-1);
                        if (item == null) {
                            Log.e(TAG, "live list item is null at icon_position:" + i);
                            return;
                        }
                        startLivePlay(item);
                    }

                }
                mLastClickTime = System.currentTimeMillis();

            }
        });
        pbHelp.setProgressBarClickListener(this);
    }

    @Override
    public void onRefresh() {
        refreshListView();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        LogUtil.e(TAG, "onHiddenChanged:" + hidden + ",mListType:" + mListType);
        if (!hidden) {
            refreshListView();
        }
    }

    /**
     * 刷新直播列表
     */
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (START_LIVE_PLAY == requestCode) {
            if (0 != resultCode) {
                //观看直播返回错误信息后，刷新列表，但是不显示动画
                mLiveListPresenter.reloadLiveList();
            } else {
                if (data == null) {
                    return;
                }
                //更新列表项的观看人数和点赞数
                String userId = data.getStringExtra(Constants.PUSHER_ID);
                for (int i = 0; i < mVideoListViewAdapter.getCount(); i++) {
                    LiveInfo info = mVideoListViewAdapter.getItem(i);
                    if (info != null && info.userId.equalsIgnoreCase(userId)) {
                        info.viewCount = (int) data.getLongExtra(Constants.MEMBER_COUNT, info.viewCount);
                        info.likeCount = (int) data.getLongExtra(Constants.HEART_COUNT, info.likeCount);
                        mVideoListViewAdapter.notifyDataSetChanged();
                        break;
                    }
                }
            }
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
    public void onLiveList(int retCode, ArrayList<LiveInfo> result, boolean refresh) {
        if (retCode == 0) {
            mVideoListViewAdapter.clear();
            if (result != null && result.size() > 0) {
                mVideoListViewAdapter.addAll((ArrayList<LiveInfo>) result.clone());
                pbHelp.goneLoading();
            } else {
                pbHelp.showNoData();
            }
            if (refresh) {
                mVideoListViewAdapter.notifyDataSetChanged();
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

    private int visibleLast;

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        int itemsLastIndex = mVideoListViewAdapter.getCount();
        if (itemsLastIndex < 0) {
            return;
        }
        int lastIndex = itemsLastIndex;
        LogUtil.e(TAG, "visibleLast:" + visibleLast + ",lastIndex:" + lastIndex);
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && visibleLast >= lastIndex && !mLiveListPresenter.isLoading()) {
            mLiveListPresenter.loadDataMore();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        visibleLast = firstVisibleItem + visibleItemCount -1;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void dismissLoading() {

    }

    @Override
    public void showMsg(String msg) {
        ToastUtils.showShort(mContext, msg);
    }

    @Override
    public void showMsg(int msg) {
        ToastUtils.showShort(mContext, msg);
    }

    @Override
    public void clickRefresh() {
        refreshListView();
    }
}