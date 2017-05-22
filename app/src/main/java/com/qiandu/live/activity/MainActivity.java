package com.qiandu.live.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.qiandu.live.R;
import com.qiandu.live.presenter.MainPresenter;
import com.qiandu.live.presenter.ipresenter.IMainPresenter;
import com.qiandu.live.utils.TabDb;

/**
 * @Description:  主界面， 包括直播列表，用户信息页
 *                  UI使用FragmentTabHost+Fragment
 *                  直播列表：LiveMainFragment
 *                  个人信息页：UserInfoFragment
 * @author: Andruby
 * @date: 2016年7月8日 下午4:46:44
 */
public class MainActivity extends IMBaseActivity implements IMainPresenter.IMainView,TabHost.OnTabChangeListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private FragmentTabHost mTabHost;
    private MainPresenter mMainPresenter;

    public static final void invoke(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        mTabHost = (FragmentTabHost) obtainView(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.contentPanel);
    }

    @Override
    protected void initData() {
        mMainPresenter = new MainPresenter(this);
        mTabHost=(FragmentTabHost)super.findViewById(android.R.id.tabhost);
        mTabHost.setup(this,super.getSupportFragmentManager()
                ,R.id.contentPanel);
        mTabHost.getTabWidget().setDividerDrawable(null);
        mTabHost.setOnTabChangedListener(this);
        initTab();
    }
    private void initTab() {
        String tabs[]= TabDb.getTabsTxt();
        for(int i=0;i<tabs.length;i++){
            TabHost.TabSpec tabSpec=mTabHost.newTabSpec(tabs[i]).setIndicator(getTabView(i));
            mTabHost.addTab(tabSpec,TabDb.getFragments()[i],null);
            mTabHost.setTag(i);
        }

    }
    /**
     * 动态获取tabicon
     * @param
     * @return
     */
    private View getTabView(int idx){
        View view=LayoutInflater.from(this).inflate(R.layout.tab_button,null);
        ((TextView)view.findViewById(R.id.tvTab)).setText(TabDb.getTabsTxt()[idx]);
        if(idx==0){
            ((TextView)view.findViewById(R.id.tvTab)).setTextColor(Color.rgb(217,28,96));
            ((ImageView)view.findViewById(R.id.ivImg)).setImageResource(TabDb.getTabsImgLight()[idx]);
        }else{
            ((ImageView)view.findViewById(R.id.ivImg)).setImageResource(TabDb.getTabsImg()[idx]);
        }
        return view;
    }
    private void updateTab(){
        TabWidget tabw=mTabHost.getTabWidget();
        for(int i=0;i<tabw.getChildCount();i++){
            View view=tabw.getChildAt(i);
            ImageView iv=(ImageView)view.findViewById(R.id.ivImg);
            if(i==mTabHost.getCurrentTab()){
                ((TextView)view.findViewById(R.id.tvTab)).setTextColor(Color.rgb(217,28,96));
                iv.setImageResource(TabDb.getTabsImgLight()[i]);
            }else{        ((TextView)view.findViewById(R.id.tvTab)).setTextColor(getResources().getColor(R.color.C10));
                iv.setImageResource(TabDb.getTabsImg()[i]);
            }

        }
    }
    @Override
    protected void setListener() {
        mTabHost.getTabWidget().getChildTabViewAt(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PublishSettingActivity.invoke(MainActivity.this);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    protected void onStart() {
        super.onStart();
        mMainPresenter.checkCacheAndLogin();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
    public Context getContext() {
        return this;
    }

    @Override
    public void onTabChanged(String s) {
        updateTab();
    }
}
